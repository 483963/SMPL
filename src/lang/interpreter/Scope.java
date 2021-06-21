package lang.interpreter;

import java.util.HashMap;

import lang.values.Value;

/**
 * A display manages run-time access to variable and parameter scope where
 * functions may be nested.
 */
public class Scope {
    private final HashMap<String, Value<?>> vars;
    public final Scope parent;

    Scope(Scope parent, HashMap<String, Value<?>> vars) {
        if (vars == null) {
            vars = new HashMap<>();
        }
        this.parent = parent;
        this.vars = vars;
    }

    public Scope findScope(String name) {
        for (Scope s = this; s != null; s = s.parent) {
            if (s.vars.containsKey(name)) {
                return s;
            }
        }
        return null;
    }

    public Value<?> getVal(String name) {
        return vars.get(name);
    }

    public void setVal(String name, Value<?> v) {
        vars.put(name, v);
    }
}
