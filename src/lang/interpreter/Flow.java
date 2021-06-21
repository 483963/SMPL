package lang.interpreter;

import lang.values.Value;

public class Flow {
    static class Return extends RuntimeException {
        public final Value<?> result;

        Return(Value<?> result) {
            this.result = result;
        }
    }

    static class Break extends RuntimeException {
    }

    static class Continue extends RuntimeException {
    }
}
