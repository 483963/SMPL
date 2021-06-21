package lang.values;

public class ValueNew extends Value<Object> {
    public ValueNew(String name) {
        super(new Object());
        if (name != null) {
            addProp("__name", new ValueString(name));
        }
    }

    public String getType() {
        return toStr();
    }

    public String toStr() {
        Value<?> v = getProp("__name");
        if (v instanceof ValueString) {
            return String.format("{%s}", (String) v.value);
        }
        return "{new}";
    }
}
