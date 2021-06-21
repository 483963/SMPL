package lang.values;

import lang.interpreter.ExceptionSemantic;
import lang.interpreter.Parser;

public class ValueString extends Value<String> {

    public ValueString(String v) {
        super(v);

        String _v = value;

        addFn(new ValueFn("len") {
            public Value<?> call() {
                return new ValueInt((long) _v.length());
            }
        });

        addFn(new ValueFn("at", "index") {
            public Value<?> call(Value<?> index) {
                return new ValueString(String.valueOf(_v.charAt((int) index._int())));
            }
        });

        addFn(new ValueFn("sub", "from", "to") {
            public Value<?> call(Value<?> _from, Value<?> _to) {
                int from = (int) _from._int();
                int to = (int) _to._int();
                if (from < 0) from = 0;
                if (to < 0) to = 0;
                if (from > _v.length()) from = _v.length();
                if (to > _v.length()) to = _v.length();
                return new ValueString(_v.substring(from, to));
            }
        });

        addFn(new ValueFn("upper") {
            public Value<?> call() {
                return new ValueString(_v.toUpperCase());
            }
        });

        addFn(new ValueFn("lower") {
            public Value<?> call() {
                return new ValueString(_v.toLowerCase());
            }
        });
    }

    public static ValueString parse(String value) {
        // unquotes
        value = value.substring(1, value.length() - 1);

        StringBuilder b = new StringBuilder();
        boolean escape = false;
        for (char c : value.toCharArray()) {
            if (escape) {
                switch (c) {
                    case '\\':
                        b.append('\\');
                        break;
                    case '"':
                        b.append('"');
                        break;
                    case 'r':
                        b.append('\r');
                        break;
                    case 'n':
                        b.append('\n');
                        break;
                    case 't':
                        b.append('\t');
                        break;
                    case 'b':
                        b.append('\b');
                        break;
                    default:
                        throw new ExceptionSemantic("Undefined escape: " + "'" + c + "'");
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else {
                b.append(c);
            }
        }

        return new ValueString(b.toString());
    }

    public String getType() {
        return "str";
    }


    public Integer compare(Value<?> v) {
        if (v.value instanceof String) {
            return value.compareTo((String) v.value);
        }
        return super.compare(v);
    }

    /** Add performs string concatenation. */
    public Value<?> add(Value<?> v) {
        return new ValueString(value + v.toStr());
    }

    public Value<?> mul(Value<?> v) {
        if (v.value instanceof Long) {
            long n = (long) v.value;
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < n; i++) {
                b.append(value);
            }
            return new ValueString(b.toString());
        }
        return super.mul(v);
    }
}
