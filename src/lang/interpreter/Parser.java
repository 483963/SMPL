package lang.interpreter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import lang.parser.ast.*;
import lang.values.*;

public class Parser implements SMPLVisitor {

    // Scope display handler
    private Scope scope = new Scope(null, Stdlib.vars);

    // Get the ith child of a given node.
    private static SimpleNode getChild(SimpleNode node, int childIndex) {
        return (SimpleNode) node.jjtGetChild(childIndex);
    }

    // Get the token value of the ith child of a given node.
    private static String getTokenOfChild(SimpleNode node, int childIndex) {
        return getChild(node, childIndex).tokenValue;
    }

    // Execute a given child of the given node
    private Object doChild(SimpleNode node, int childIndex, Object data) {
        return node.jjtGetChild(childIndex).jjtAccept(this, data);
    }

    // Execute a given child of a given node, and return its value as a Value.
    // This is used by the expression evaluation nodes.
    Value<?> doChild(SimpleNode node, int childIndex) {
        return (Value<?>) doChild(node, childIndex, null);
    }

    // Execute all children of the given node
    Object doChildren(SimpleNode node, Object data) {
        return node.childrenAccept(this, data);
    }

    // Called if one of the following methods is missing...
    public Object visit(SimpleNode node, Object data) {
        System.out.println(node + ": acceptor not implemented in subclass?");
        return data;
    }

    // Execute a SMPL program
    public Object visit(ASTCode node, Object data) {
        return doChildren(node, data);
    }

    // Execute a statement
    public Object visit(ASTStatement node, Object data) {
        return doChildren(node, data);
    }

    public Object visit(ASTBreak node, Object data) {
        throw new Flow.Break();
    }

    public Object visit(ASTContinue node, Object data) {
        throw new Flow.Continue();
    }

    // Execute a block in new scope
    @SuppressWarnings("unchecked")
    public Object visit(ASTBlock node, Object data) {
        scope = new Scope(scope, (HashMap<String, Value<?>>) data);
        try {
            doChildren(node, data);
        } catch (Throwable e) {
            scope = scope.parent;
            throw e;
        }
        scope = scope.parent;
        return data;
    }

    // Function definition
    public Object visit(ASTFnDef node, Object data) {
        // Already defined?
        if (node.optimised == null) {
            // Child 0 - identifier (fn name)
            String name = getTokenOfChild(node, 0);
            if (scope.getVal(name) != null)
                throw new ExceptionSemantic("Function " + name + " already exists.");

            String[] params = (String[]) doChild(node, 1, null);
            ASTBlock body = (ASTBlock) getChild(node, 2);
            ValueFn fn = new ValueFn(name, scope, params, body);
            scope.setVal(name, fn);
            node.optimised = fn;
            return fn;
        }

        // return create new function each time with current scope
        ValueFn fn = (ValueFn) node.optimised;
        ValueFn newFn = new ValueFn(fn.name, scope, fn.params, fn.body);
        scope.setVal(newFn.name, newFn);
        return newFn;
    }

    // Function definition parameter list
    public Object visit(ASTParmlist node, Object data) {
        int n = node.jjtGetNumChildren();
        String[] params = new String[n];
        for (int i = 0; i < n; i++)
            params[i] = getTokenOfChild(node, i);
        return params;
    }

    // Function return expression
    public Object visit(ASTReturnExpression node, Object data) {
        Value<?> r = (Value<?>) doChild(node, 0);
        if (r == null) {
            r = ValueNull.singleton;
        }
        throw new Flow.Return(r);
    }

    // Function call
    public Object visit(ASTFnCall node, Object data) {
        ValueFn fn;
        Value<?> v = doChild(node, 0);
        if (!(v instanceof ValueFn)) {
            throw v.expected("<fn>", v);
        }

        fn = (ValueFn) v;
        Value<?>[] args = (Value<?>[]) doChild(node, 1, null);
        if (fn.params.length != args.length) {
            throw new ExceptionSemantic(String.format(
                    "Expected %s args got %s", fn.params.length, args.length));
        }

        Value<?> retVal = ValueNull.singleton;

        Scope currentScope = scope;
        scope = fn.scope;

        try {
            retVal = fn.call(this, args);
            scope = currentScope;
        } catch (Flow.Return r) {
            scope = currentScope;
            retVal = r.result;
        } catch (Throwable r) {
            scope = currentScope;
        }
        return retVal;
    }

    // Function invocation argument list.
    public Object visit(ASTArgList node, Object data) {
        int n = node.jjtGetNumChildren();
        Value<?>[] args = new Value<?>[n];
        for (int i = 0; i < n; i++) {
            args[i] = doChild(node, i);
        }
        return args;
    }

    public Object visit(ASTAnonymousFn node, Object data) {
        // Already defined?
        if (node.optimised == null) {
            String[] params = (String[]) doChild(node, 0, null);
            ASTBlock body = (ASTBlock) getChild(node, 1);
            ValueFn fn = new ValueFn("", scope, params, body);
            node.optimised = fn;
            return fn;
        }

        // return create new function each time with current scope
        ValueFn fn = (ValueFn) node.optimised;
        return new ValueFn("", scope, fn.params, fn.body);
    }

    // Execute an IF
    public Object visit(ASTIfStatement node, Object data) {
        // evaluate boolean expression
        if (doChild(node, 0).isTruthy())
            doChild(node, 1);                            // if(true), therefore do 'if' statement
        else if (node.ifHasElse)                        // does it have an else statement?
            doChild(node, 2);                            // if(false), therefore do 'else' statement
        return data;
    }

    // Execute a While loop
    public Object visit(ASTWhileLoop node, Object data) {
        while (
            // loop test
                doChild(node, 0).isTruthy()
        ) {
            try {
                // loop body
                doChild(node, 1);
            } catch (Flow.Continue e) {
                continue;
            } catch (Flow.Break e) {
                break;
            }
        }
        return data;
    }

    // Execute a FOR loop
    public Object visit(ASTForLoop node, Object data) {
        for (
            // loop initialisation
                doChild(node, 0);
            // loop test
                doChild(node, 1).isTruthy();
            // loop inc
                doChild(node, 2)
        ) {
            try {
                // loop body
                doChild(node, 3);
            } catch (Flow.Continue e) {
                continue;
            } catch (Flow.Break e) {
                break;
            }
        }
        return data;
    }

    // Process an identifier
    // This doesn't do anything, but needs to be here because we need an ASTIdentifier node.
    public Object visit(ASTIdentifier node, Object data) {
        return data;
    }

    // Execute the WRITE statement
    public Object visit(ASTWrite node, Object data) {
        System.out.println(doChild(node, 0).toStr());
        return data;
    }

    // Dereference a variable or parameter, and return its value.
    public Object visit(ASTGet node, Object data) {
        // don't optimize
        String name = node.tokenValue;
        Scope s = scope.findScope(name);
        if (s == null)
            throw new ExceptionSemantic("Variable " + name + " is undefined.");
        return s.getVal(name);
    }

    // Execute an assignment statement.
    public Object visit(ASTSet node, Object data) {
        // don't optimize
        String name = getTokenOfChild(node, 0);
        Scope s = scope.findScope(name);
        if (s == null)
            s = scope;
        Value<?> r = doChild(node, 1);
        s.setVal(name, r);
        return data;
    }

    public Object visit(ASTPropGet node, Object data) {
        Value<?> l = doChild(node, 0);
        String name = getTokenOfChild(node, 1);
        Value<?> v = l.getProp(name);
        if (v == null)
            throw new ExceptionSemantic("Property " + name + " is undefined.");
        return v;
    }

    public Object visit(ASTPropSet node, Object data) {
        ASTPropGet lnode = (ASTPropGet) getChild(node, 0);
        Value<?> l = doChild(lnode, 0);
        String prop = getTokenOfChild(lnode, 1);
        Value<?> r = doChild(node, 1);
        l.setProp(prop, r);
        return data;
    }

    // or short-circuit
    public Object visit(ASTOr node, Object data) {
        Value<?> l = doChild(node, 0);
        if (l.isTruthy()) {
            // return early if possible
            return l;
        }
        return doChild(node, 1);
    }

    // and short-circuit
    public Object visit(ASTAnd node, Object data) {
        Value<?> l = doChild(node, 0);
        if (!l.isTruthy()) {
            // return early if possible
            return l;
        }
        return doChild(node, 1);
    }

    // ==
    public Object visit(ASTCompEqual node, Object data) {
        return doChild(node, 0).eq(doChild(node, 1));
    }

    // !=
    public Object visit(ASTCompNequal node, Object data) {
        return doChild(node, 0).neq(doChild(node, 1));
    }

    // >=
    public Object visit(ASTCompGTE node, Object data) {
        return doChild(node, 0).gte(doChild(node, 1));
    }

    // <=
    public Object visit(ASTCompLTE node, Object data) {
        return doChild(node, 0).lte(doChild(node, 1));
    }

    // >
    public Object visit(ASTCompGT node, Object data) {
        return doChild(node, 0).gt(doChild(node, 1));
    }

    // <
    public Object visit(ASTCompLT node, Object data) {
        return doChild(node, 0).lt(doChild(node, 1));
    }

    // ?
    public Object visit(ASTNullCoalesce node, Object data) {
        Value<?> l = doChild(node, 0);
        if (l != ValueNull.singleton) {
            return l;
        }
        return doChild(node, 1);
    }

    // +
    public Object visit(ASTAdd node, Object data) {
        return doChild(node, 0).add(doChild(node, 1));
    }

    // -
    public Object visit(ASTSub node, Object data) {
        return doChild(node, 0).sub(doChild(node, 1));
    }

    // *
    public Object visit(ASTMul node, Object data) {
        return doChild(node, 0).mul(doChild(node, 1));
    }

    // /
    public Object visit(ASTDiv node, Object data) {
        return doChild(node, 0).div(doChild(node, 1));
    }

    // %
    public Object visit(ASTRem node, Object data) {
        return doChild(node, 0).rem(doChild(node, 1));
    }

    // not
    public Object visit(ASTUnaryNot node, Object data) {
        return doChild(node, 0).not();
    }

    // + (unary)
    public Object visit(ASTUnaryPlus node, Object data) {
        return doChild(node, 0).unary_plus();
    }

    // - (unary)
    public Object visit(ASTUnaryMinus node, Object data) {
        return doChild(node, 0).unary_minus();
    }

    public Object visit(ASTNewProp node, Object data) {
        ValueNew v = (ValueNew) (data);
        String name = getTokenOfChild(node, 0);
        Value<?> r = doChild(node, 1);
        v.addProp(name, r);
        return data;
    }

    public Object visit(ASTNew node, Object data) {
        ValueNew v = new ValueNew(null);
        doChildren(node, v);
        return v;
    }

    // Return string literal
    public Object visit(ASTCharacter node, Object data) {
        if (node.optimised == null)
            node.optimised = ValueString.parse(node.tokenValue);
        return node.optimised;
    }

    // Return integer literal
    public Object visit(ASTInt node, Object data) {
        if (node.optimised == null) {
            node.optimised = ValueInt.parse(node.tokenValue);
        }
        return node.optimised;
    }

    // Return floating point literal
    public Object visit(ASTFloat node, Object data) {
        if (node.optimised == null)
            node.optimised = ValueFloat.parse(node.tokenValue);
        return node.optimised;
    }

    // Return true literal
    public Object visit(ASTTrue node, Object data) {
        if (node.optimised == null)
            node.optimised = new ValueBoolean(true);
        return node.optimised;
    }

    // Return false literal
    public Object visit(ASTFalse node, Object data) {
        if (node.optimised == null)
            node.optimised = new ValueBoolean(false);
        return node.optimised;
    }

    // Return null literal
    public Object visit(ASTNull node, Object data) {
        return ValueNull.singleton;
    }

    @Override
    public Object visit(ASTList node, Object data) {
        int n = node.jjtGetNumChildren();

        if (node.optimised == null) {
            ArrayList<Value<?>> list = new ArrayList<>(n);
            node.optimised = new ValueList(list);
        }

        ArrayList<Value<?>> list = ((ValueList) node.optimised).value;
        list.clear();
        for (int i = 0; i < n; i++) {
            list.add(doChild(node, i));
        }

        return node.optimised;
    }

}
