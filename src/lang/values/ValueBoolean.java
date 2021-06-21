package lang.values;

public class ValueBoolean extends Value<Boolean> {

    public ValueBoolean(Boolean v) {
        super(v);
    }

    public String getType() {
        return "bool";
    }

    public boolean isTruthy() {
        return (boolean) value;
    }

}
