package lang.values;

import java.util.ArrayList;
import java.util.HashMap;

import lang.interpreter.ExceptionSemantic;

public abstract class Value<T> {
    private final HashMap<String, Value<?>> prop = new HashMap<>();

    public T value;

    public Value(T v) {
        value = v;
    }

    public ExceptionSemantic expected(String type, Value<?> v) {
        return new ExceptionSemantic(
                String.format("Expected %s got %s", type, v.getType())
        );
    }

    public ExceptionSemantic unsupported(String op) {
        return new ExceptionSemantic(
                String.format("Unsupported operation %s on %s", op, getType())
        );
    }

    public ExceptionSemantic unsupported(String op, Value<?> v) {
        return new ExceptionSemantic(
                String.format("Unsupported operation %s on %s and %s", op, getType(), v.getType())
        );
    }

    public double _num() {
        if (this instanceof ValueInt) return (double) (long) value;
        if (this instanceof ValueFloat) return (double) value;
        throw expected("num", this);
    }

    public long _int() {
        if (this instanceof ValueInt) return (long) value;
        throw expected("int", this);
    }

    public double _float() {
        if (this instanceof ValueFloat) return (double) value;
        throw expected("float", this);
    }

    public String _str() {
        if (this instanceof ValueString) return (String) value;
        throw expected("str", this);
    }

    public abstract String getType();


    public Value<?> not() {
        return new ValueBoolean(!isTruthy());
    }

    public Value<?> add(Value<?> v) {
        throw unsupported("+", v);
    }

    public Value<?> sub(Value<?> v) {
        throw unsupported("-", v);
    }

    public Value<?> mul(Value<?> v) {
        throw unsupported("*", v);
    }

    public Value<?> div(Value<?> v) {
        throw unsupported("/", v);
    }

    public Value<?> rem(Value<?> v) {
        throw unsupported("%", v);
    }

    public Value<?> unary_plus() {
        throw unsupported("unary +");
    }

    public Value<?> unary_minus() {
        throw unsupported("unary -");
    }

    public boolean isTruthy() {
        return true;
    }

    /** Convert this to a primitive string. */
    public String toStr() {
        return value.toString();
    }

    public Integer compare(Value<?> v) {
        return null;
    }

    /** Test this value and another for equality. */
    public Value<?> eq(Value<?> v) {
        Integer cmp = compare(v);
        if (cmp != null) {
            return new ValueBoolean(cmp == 0);

        }

        return new ValueBoolean(this.value.equals(v.value));
    }

    /** Test this value and another for non-equality. */
    public Value<?> neq(Value<?> v) {
        Integer cmp = compare(v);
        if (cmp != null) {
            return new ValueBoolean(cmp != 0);
        }

        return new ValueBoolean(!this.value.equals(v.value));
    }


    /** Test this value and another for > */
    public Value<?> gt(Value<?> v) {
        Integer cmp = compare(v);
        if (cmp == null)
            throw unsupported(">", v);
        return new ValueBoolean(cmp > 0);
    }

    /** Test this value and another for >= */
    public Value<?> gte(Value<?> v) {
        Integer cmp = compare(v);
        if (cmp == null)
            throw unsupported(">=", v);
        return new ValueBoolean(cmp >= 0);
    }

    /** Test this value and another for < */
    public Value<?> lt(Value<?> v) {
        Integer cmp = compare(v);
        if (cmp == null)
            throw unsupported("<", v);
        return new ValueBoolean(cmp < 0);
    }

    /** Test this value and another for <= */
    public Value<?> lte(Value<?> v) {
        Integer cmp = compare(v);
        if (cmp == null)
            throw unsupported("<=", v);
        return new ValueBoolean(cmp <= 0);
    }

    public Value<?> getProp(String name) {
        return prop.get(name);
    }

    public void setProp(String name, Value<?> v) {
        if (!prop.containsKey(name)) {
            throw new ExceptionSemantic(String.format(
                    "Can't set property %s", name));
        }
        prop.put(name, v);
    }

    public void addProp(String name, Value<?> v) {
        if (prop.containsKey(name)) {
            throw new ExceptionSemantic(String.format(
                    "Property %s already exists", name));
        }
        prop.put(name, v);
    }

    public void addFn(ValueFn fn) {
        addProp(fn.name, fn);
    }
}
