package caribou.impl;

import caribou.*;

public class GeneratorImpl implements Generator {

    private final RequestParser parser;
    private final Interpreter interpreter;

    public GeneratorImpl(RequestParser parser, Interpreter interpreter) {
        this.parser = parser;
        this.interpreter = interpreter;
    }

    @Override
    public Migration generate(String command) {
        Request request = parser.parse(command);
        return interpreter.interpret(request);
    }
}