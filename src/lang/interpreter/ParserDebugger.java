package lang.interpreter;

import lang.parser.ast.*;
import lang.values.Value;

public class ParserDebugger implements SMPLVisitor {

    private int indent = 0;

    private String indentString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; ++i) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /** Debugging dump of a node. */
    private Object dump(SimpleNode node, Object data) {
        System.out.println(indentString() + node);
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;
        return data;
    }

    public Object visit(SimpleNode node, Object data) {
        System.out.println(node + ": acceptor not implemented in subclass?");
        return data;
    }

    // Execute a SMPL program
    public Object visit(ASTCode node, Object data) {
        dump(node, data);
        return data;
    }

    // Execute a statement
    public Object visit(ASTStatement node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTBreak node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTContinue node, Object data) {
        dump(node, data);
        return data;
    }

    // Execute a block
    public Object visit(ASTBlock node, Object data) {
        dump(node, data);
        return data;
    }

    // Execute an IF
    public Object visit(ASTIfStatement node, Object data) {
        dump(node, data);
        return data;
    }

    // Function definition parameter list
    public Object visit(ASTParmlist node, Object data) {
        dump(node, data);
        return data;
    }

    // Function definition
    public Object visit(ASTFnDef node, Object data) {
        dump(node, data);
        return data;
    }

    // Function return expression
    public Object visit(ASTReturnExpression node, Object data) {
        dump(node, data);
        return data;
    }

    // Function argument list
    public Object visit(ASTArgList node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTAnonymousFn node, Object data) {
        dump(node, data);
        return data;

    }

    // Function call
    public Object visit(ASTFnCall node, Object data) {
        dump(node, data);
        return data;
    }

    // Dereference a variable, and push its value onto the stack
    public Object visit(ASTGet node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTWhileLoop node, Object data) {
        dump(node, data);
        return data;
    }

    // Execute a FOR loop
    public Object visit(ASTForLoop node, Object data) {
        dump(node, data);
        return data;
    }

    // Process an identifier
    // This doesn't do anything, but needs to be here because we need an ASTIdentifier node.
    public Object visit(ASTIdentifier node, Object data) {
        dump(node, data);
        return data;
    }

    // Execute the WRITE statement
    public Object visit(ASTWrite node, Object data) {
        dump(node, data);
        return data;
    }

    // Execute an assignment statement, by popping a value off the stack and assigning it
    // to a variable.
    public Object visit(ASTSet node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTPropGet node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTPropSet node, Object data) {
        dump(node, data);
        return data;
    }

    // OR
    public Object visit(ASTOr node, Object data) {
        dump(node, data);
        return data;
    }

    // AND
    public Object visit(ASTAnd node, Object data) {
        dump(node, data);
        return data;
    }

    // ==
    public Object visit(ASTCompEqual node, Object data) {
        dump(node, data);
        return data;
    }

    // !=
    public Object visit(ASTCompNequal node, Object data) {
        dump(node, data);
        return data;
    }

    // >=
    public Object visit(ASTCompGTE node, Object data) {
        dump(node, data);
        return data;
    }

    // <=
    public Object visit(ASTCompLTE node, Object data) {
        dump(node, data);
        return data;
    }

    // >
    public Object visit(ASTCompGT node, Object data) {
        dump(node, data);
        return data;
    }

    // <
    public Object visit(ASTCompLT node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTNullCoalesce node, Object data) {
        dump(node,data);
        return data;
    }

    // +
    public Object visit(ASTAdd node, Object data) {
        dump(node, data);
        return data;
    }

    // -
    public Object visit(ASTSub node, Object data) {
        dump(node, data);
        return data;
    }

    // *
    public Object visit(ASTMul node, Object data) {
        dump(node, data);
        return data;
    }

    // /
    public Object visit(ASTDiv node, Object data) {
        dump(node, data);
        return data;
    }

    // %
    public Object visit(ASTRem node, Object data) {
        dump(node, data);
        return data;
    }

    // NOT
    public Object visit(ASTUnaryNot node, Object data) {
        dump(node, data);
        return data;
    }

    // + (unary)
    public Object visit(ASTUnaryPlus node, Object data) {
        dump(node, data);
        return data;
    }

    // - (unary)
    public Object visit(ASTUnaryMinus node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTNewProp node, Object data) {
        dump(node, data);
        return data;
    }

    public Object visit(ASTNew node, Object data) {
        dump(node, data);
        return data;
    }

    // Push string literal to stack
    public Object visit(ASTCharacter node, Object data) {
        dump(node, data);
        return data;
    }

    // Push integer literal to stack
    public Object visit(ASTInt node, Object data) {
        dump(node, data);
        return data;
    }

    // Push floating point literal to stack
    public Object visit(ASTFloat node, Object data) {
        dump(node, data);
        return data;
    }

    // Push true literal to stack
    public Object visit(ASTTrue node, Object data) {
        dump(node, data);
        return data;
    }

    // Push false literal to stack
    public Object visit(ASTFalse node, Object data) {
        dump(node, data);
        return data;
    }

    // Push null literal to stack
    public Object visit(ASTNull node, Object data) {
        dump(node, data);
        return data;
    }

    @Override
    public Object visit(ASTList node, Object data) {
        dump(node, data);
        return data;
    }

}
