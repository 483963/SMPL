package lang.values;

import java.util.ArrayList;
import java.util.Arrays;

import lang.interpreter.ExceptionSemantic;
import lang.interpreter.Parser;

public class ValueList extends Value<ArrayList<Value<?>>> {
    public ValueList(ArrayList<Value<?>> value) {
        super(value);

        ArrayList<Value<?>> _v = value;

        addFn(new ValueFn("len") {
            public Value<?> call() {
                return new ValueInt((long) _v.size());
            }
        });

        addFn(new ValueFn("at", "index") {
            public Value<?> call(Value<?> index) {
                return _v.get((int) index._int());
            }
        });

        addFn(new ValueFn("set", "index", "value") {
            public Value<?> call(Value<?> _index, Value<?> value) {
                int index = (int) _index._int();
                if (index < 0 || index >= _v.size()) {
                    throw new ExceptionSemantic(String.format(
                            "Index %s out of bound 0..%s", index, _v.size()));
                }
                _v.set(index, value);
                return ValueNull.singleton;
            }
        });

        addFn(new ValueFn("push", "value") {
            public Value<?> call(Value<?> value) {
                _v.add(value);
                return ValueNull.singleton;
            }
        });

        addFn(new ValueFn("pop") {
            public Value<?> call() {
                return _v.remove(_v.size() - 1);
            }
        });
    }

    public String getType() {
        return "list";
    }

    public String toStr() {
        StringBuilder b = new StringBuilder();
        boolean start = true;
        b.append('[');
        for (Value<?> i : value) {
            if (!start) b.append(',');
            start = false;
            b.append(i.toStr());
        }
        b.append(']');
        return b.toString();
    }
}
