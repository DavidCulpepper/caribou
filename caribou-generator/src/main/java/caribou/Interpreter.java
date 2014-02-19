package caribou;

import caribou.Migration;

public interface Interpreter {
    Migration interpret(Request request);
}
