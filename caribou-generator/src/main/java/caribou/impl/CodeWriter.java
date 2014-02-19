package caribou.impl;

/**
 * Created by dculpepper on 2/18/14.
 */
public class CodeWriter {

    private final int indentationSteps;
    private String indentation = "";
    private final StringBuilder code = new StringBuilder();

    public CodeWriter(int indentationSteps) {
        this.indentationSteps = indentationSteps;
    }

    public CodeWriter writeLine(String line) {
        code.append(indentation).append(line).append('\n');
        return this;
    }

    public CodeWriter indent() {
        for (int i = 0; i < indentationSteps; ++i) {
            indentation += ' ';
        }
        return this;
    }

    public CodeWriter unIndent() {
        indentation = indentation.substring(indentationSteps);
        return this;
    }

    public CodeWriter backspace() {
        code.delete(code.length() - 1, code.length());
        return this;
    }

    public String compile() {
        return code.toString();
    }
}
