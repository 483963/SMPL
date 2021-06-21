package lang.interpreter;

import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import lang.values.Value;
import lang.values.ValueBoolean;
import lang.values.ValueFloat;
import lang.values.ValueFn;
import lang.values.ValueInt;
import lang.values.ValueNew;
import lang.values.ValueNull;
import lang.values.ValueString;

public class Stdlib {
    static HashMap<String, Value<?>> vars = new HashMap<>();

    static {
        vars.put("math", math());
        vars.put("print", new ValueFn("print", "data") {
            protected Value<?> call(Value<?> data) {
                System.out.println(data.toStr());
                return ValueNull.singleton;
            }
        });
        vars.put("eprint", new ValueFn("eprint", "data") {
            protected Value<?> call(Value<?> data) {
                System.err.println(data.toStr());
                return ValueNull.singleton;
            }
        });
        vars.put("map", new ValueFn("map") {
            protected Value<?> call() {
                return map();
            }
        });
        vars.put("input", new ValueFn("input") {
            protected Value<?> call() {
                try {
                    return new ValueString(new Scanner(System.in).next());
                } catch (Exception e) {
                    e.printStackTrace();
                    return ValueNull.singleton;
                }
            }
        });
        vars.put("time", new ValueFn("time") {
            protected Value<?> call() {
                return new ValueInt(System.currentTimeMillis());
            }
        });
        vars.put("int", new ValueFn("int", "value") {
            protected Value<?> call(Value<?> v) {
                if (v instanceof ValueInt) return v;
                if (v instanceof ValueFloat) return new ValueInt((long) (double) v.value);
                if (v instanceof ValueString) {
                    try {
                        return new ValueInt(Long.parseLong((String) v.value));
                    } catch (NumberFormatException e) {
                        return ValueNull.singleton;
                    }
                }
                throw expected("num|str", v);
            }
        });
        vars.put("float", new ValueFn("float", "value") {
            protected Value<?> call(Value<?> v) {
                if (v instanceof ValueFloat) return v;
                if (v instanceof ValueInt) return new ValueFloat((double) (long) v.value);
                if (v instanceof ValueString) {
                    try {
                        return new ValueFloat(Double.parseDouble((String) v.value));
                    } catch (NumberFormatException e) {
                        return ValueNull.singleton;
                    }
                }
                throw expected("num|str", v);
            }
        });
        vars.put("str", new ValueFn("str", "data") {
            protected Value<?> call(Value<?> data) {
                return new ValueString(data.toStr());
            }
        });
        vars.put("bool", new ValueFn("bool", "data") {
            protected Value<?> call(Value<?> data) {
                return new ValueBoolean(data.isTruthy());
            }
        });
    }


    static ValueNew map() {
        HashMap<Object, Value<?>> v = new HashMap<>();
        ValueNew map = new ValueNew("map");
        map.addFn(new ValueFn("contains", "key") {
            protected Value<?> call(Value<?> key) {
                return new ValueBoolean(v.containsKey(key.value));
            }
        });
        map.addFn(new ValueFn("get", "key") {
            protected Value<?> call(Value<?> key) {
                Value<?> r = v.get(key.value);
                return r != null ? r : ValueNull.singleton;
            }
        });
        map.addFn(new ValueFn("set", "key", "value") {
            protected Value<?> call(Value<?> key, Value<?> value) {
                v.put(key.value, value);
                return value;
            }
        });
        return map;
    }

    static ValueNew math() {
        ValueNew math = new ValueNew("math");
        math.addFn(new ValueFn("max", "a", "b") {
            protected Value<?> call(Value<?> a, Value<?> b) {
                if (a instanceof ValueInt && b instanceof ValueInt)
                    return new ValueInt(Math.max(a._int(), b._int()));
                return new ValueFloat(Math.max(a._num(), b._num()));
            }
        });
        math.addFn(new ValueFn("min", "a", "b") {
            protected Value<?> call(Value<?> a, Value<?> b) {
                if (a instanceof ValueInt && b instanceof ValueInt)
                    return new ValueInt(Math.min(a._int(), b._int()));
                return new ValueFloat(Math.min(a._num(), b._num()));
            }
        });
        math.addFn(new ValueFn("floor", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.floor(a._float()));
            }
        });
        math.addFn(new ValueFn("ceil", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.ceil(a._float()));
            }
        });
        math.addFn(new ValueFn("pow", "a", "b") {
            protected Value<?> call(Value<?> a, Value<?> b) {
                return new ValueFloat(Math.pow(a._num(), b._num()));
            }
        });
        math.addFn(new ValueFn("sqrt", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.sqrt(a._num()));
            }
        });
        math.addFn(new ValueFn("sin", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.sin(a._num()));
            }
        });
        math.addFn(new ValueFn("cos", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.cos(a._num()));
            }
        });
        math.addFn(new ValueFn("tan", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.tan(a._num()));
            }
        });
        math.addFn(new ValueFn("asin", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.asin(a._num()));
            }
        });
        math.addFn(new ValueFn("acos", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.acos(a._num()));
            }
        });
        math.addFn(new ValueFn("atan", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.atan(a._num()));
            }
        });
        math.addFn(new ValueFn("log", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.log(a._num()));
            }
        });
        math.addFn(new ValueFn("log10", "a") {
            protected Value<?> call(Value<?> a) {
                return new ValueFloat(Math.log10(a._num()));
            }
        });
        return math;
    }
}