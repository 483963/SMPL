package lang.values;

public class ValueNull extends Value<ValueNull.Null> {
    static class Null {
        public String toString() {
            return "null";
        }
    }

    // there should be single instance of null
    public static ValueNull singleton = new ValueNull();

    private ValueNull() {
        super(new Null());
    }

    public String getType() {
        return "null";
    }

    public boolean isTruthy() {
        return false;
    }
}
