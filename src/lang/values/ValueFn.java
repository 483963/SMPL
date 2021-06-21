package lang.values;

import java.util.Arrays;
import java.util.HashMap;

import lang.interpreter.ExceptionSemantic;
import lang.interpreter.Parser;
import lang.interpreter.Scope;
import lang.parser.ast.ASTBlock;
import lang.parser.ast.SimpleNode;

public class ValueFn extends Value<Object> {
    public final String name;
    public final Scope scope;
    public final String[] params;
    public final ASTBlock body;

    public ValueFn(String name, Scope scope, String[] params, ASTBlock body) {
        // new Object for equality
        super(new Object());
        this.params = params;
        this.scope = scope;
        this.name = name;
        this.body = body;
    }

    public ValueFn(String name, String... params) {
        this(name, null, params, null);
    }

    public Value<?> call(Parser parser, Value<?>[] args) {
        Value<?> res = null;
        switch (args.length) {
            case 0:
                res = call();
                break;
            case 1:
                res = call(args[0]);
                break;
            case 2:
                res = call(args[0], args[1]);
                break;
            case 3:
                res = call(args[0], args[1], args[2]);
                break;
        }

        if (res != null) {
            return res;
        }

        HashMap<String, Value<?>> vars = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            vars.put(params[i], args[i]);
        }

        body.jjtAccept(parser, vars);
        return ValueNull.singleton;
    }

    protected Value<?> call() {
        return null;
    }

    protected Value<?> call(Value<?> arg1) {
        return null;
    }

    protected Value<?> call(Value<?> arg1, Value<?> arg2) {
        return null;
    }

    protected Value<?> call(Value<?> arg1, Value<?> arg2, Value<?> arg3) {
        return null;
    }

    public String getType() {
        return String.format("<fn %s>", name);
    }

    public String toStr() {
        return String.format("<fn %s(%s)>", name, String.join(", ", params));
    }
}
