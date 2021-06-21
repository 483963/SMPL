package lang.values;

public class ValueInt extends Value<Long> {
    public ValueInt(Long v) {
        super(v);
    }

    public static ValueInt parse(String value) {
        int radix = 10;

        if (value.startsWith("0x")) {
            value = value.substring(2);
            radix = 16;
        } else if (value.startsWith("0o")) {
            value = value.substring(2);
            radix = 7;
        }
        return new ValueInt(Long.parseLong(value, radix));
    }

    public String getType() {
        return "int";
    }


    public Integer compare(Value<?> v) {
        if (v.value instanceof Long) {
            return Long.compare(value, (long) v.value);
        }
        if (v.value instanceof Double) {
            return Double.compare(value, (double) v.value);
        }
        return super.compare(v);
    }

    public Value<?> add(Value<?> v) {
        if (v.value instanceof Long) {
            return new ValueInt(value + (long) v.value);
        }
        if (v.value instanceof Double) {
            return new ValueFloat(value + (double) v.value);
        }
        return super.add(v);
    }

    public Value<?> sub(Value<?> v) {
        if (v.value instanceof Long) {
            return new ValueInt(value - (long) v.value);
        }
        if (v.value instanceof Double) {
            return new ValueFloat(value - (double) v.value);
        }
        return super.sub(v);
    }

    public Value<?> mul(Value<?> v) {
        if (v.value instanceof Long) {
            return new ValueInt(value * (long) v.value);
        }
        if (v.value instanceof Double) {
            return new ValueFloat(value * (double) v.value);
        }
        return super.mul(v);
    }

    public Value<?> div(Value<?> v) {
        if (v.value instanceof Long) {
            return new ValueInt(value / (long) v.value);
        }
        if (v.value instanceof Double) {
            return new ValueFloat(value / (double) v.value);
        }
        return super.div(v);
    }

    public Value<?> rem(Value<?> v) {
        if (v.value instanceof Long) {
            return new ValueInt(value % (long) v.value);
        }
        if (v.value instanceof Double) {
            return new ValueFloat(value % (double) v.value);
        }
        return super.rem(v);
    }

    public Value<?> unary_plus() {
        return this;
    }

    public Value<?> unary_minus() {
        return new ValueInt(-value);
    }
}
