package lang.values;

public class ValueFloat extends Value<Double> {

    public ValueFloat(Double v) {
        super(v);
    }

    public static ValueFloat parse(String value) {
        return new ValueFloat(Double.parseDouble(value));
    }

    public String getType() {
        return "float";
    }

    public Integer compare(Value<?> v) {
        if (v.value instanceof Double) {
            return Double.compare( value, (double) v.value);
        }
        if (v.value instanceof Long) {
            return Double.compare( value, (long) v.value);
        }
        return super.compare(v);
    }

    public Value<?> add(Value<?> v) {
        if (v.value instanceof Double) {
            return new ValueFloat(value + (double) v.value);
        }
        if (v.value instanceof Long) {
            return new ValueFloat( value + (long) v.value);
        }
        return super.add(v);
    }

    public Value<?> sub(Value<?> v) {
        if (v.value instanceof Double) {
            return new ValueFloat(value - (double) v.value);
        }
        if (v.value instanceof Long) {
            return new ValueFloat(value - (long) v.value);
        }
        return super.sub(v);
    }

    public Value<?> mul(Value<?> v) {
        if (v.value instanceof Double) {
            return new ValueFloat(value * (double) v.value);
        }
        if (v.value instanceof Long) {
            return new ValueFloat(value * (long) v.value);
        }
        return super.mul(v);
    }

    public Value<?> div(Value<?> v) {
        if (v.value instanceof Double) {
            return new ValueFloat(value / (double) v.value);
        }
        if (v.value instanceof Long) {
            return new ValueFloat(value / (long) v.value);
        }
        return super.div(v);
    }

    public Value<?> rem(Value<?> v) {
        if (v.value instanceof Double) {
            return new ValueFloat(value % (double) v.value);
        }
        if (v.value instanceof Long) {
            return new ValueFloat(value % (long) v.value);
        }
        return super.rem(v);
    }

    public Value<?> unary_plus() {
        return this;
    }

    public Value<?> unary_minus(){
        return new ValueFloat(-value);
    }

    public String toStr() {
        // lowercase for E to e
        return value.toString().replace('E','e');
    }
}
