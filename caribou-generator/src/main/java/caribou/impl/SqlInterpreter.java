package caribou.impl;

import caribou.*;

import java.util.List;

public class SqlInterpreter implements Interpreter {
    private final NameFormatter formatter;
    private final CodeWriter codeWriter;

    public SqlInterpreter(NameFormatter formatter, CodeWriter codeWriter) {
        this.formatter = formatter;
        this.codeWriter = codeWriter;
    }

    @Override
    public Migration interpret(Request request) {
        String name = formatter.format(request.getPrimaryRequest());
        String content = "";

        if (name.matches("add_(\\w)*_to_(\\w)*")) {
            content = addColumns(name.split("add_(\\w)*_to_")[1], request.getColumns());
        } else if (name.matches("create_join_table_(\\w)*")) {
            content = createJoinTable(request.getColumns());
        } else if (name.matches("create_(\\w)*")) {
            content = createTable(name.split("create_")[1], request.getColumns());
        }

        return new MigrationImpl(name, content);
    }

    private String addColumns(String tableName, List<ColumnSpecification> columns) {
        for (ColumnSpecification column : columns) {
            codeWriter.writeLine("ALTER TABLE " + tableName).indent()
                    .writeLine("ADD " + column.getName() + " " + column.type() + ";").unIndent();
            if (column.isIndexed()) {
                codeWriter.writeLine("CREATE INDEX IF NOT EXISTS").indent()
                        .writeLine("index_" + tableName + "_" + column.getName() + " ON " + tableName + "(" + column.getName() + ");").unIndent();
            }
        }
        return codeWriter.compile();
    }

    private String createJoinTable(List<ColumnSpecification> columns) {
        String table = "join";
        for (ColumnSpecification c : columns) {
            table += "_" + c.getName();
        }

        codeWriter.writeLine("CREATE TABLE " + table + " (").indent();

        for (ColumnSpecification column : columns) {
            codeWriter.writeLine(column.getName() + "_id INTEGER,");
        }
        codeWriter.backspace().backspace().unIndent().writeLine("").writeLine(");");

        for (ColumnSpecification column : columns) {
            codeWriter.writeLine("CREATE INDEX IF NOT EXISTS").indent()
                    .writeLine("index_" + table + "_" + column.getName() + "_id ON " + table + "(" + column.getName() + "_id);").unIndent();
        }

        return codeWriter.compile();
    }

    private String createTable(String table, List<ColumnSpecification> columns) {
        codeWriter.writeLine("CREATE TABLE " + table + " (").indent()
                .writeLine("_id INTEGER PRIMARY KEY AUTOINCREMENT,")
                .writeLine("created_at INTEGER,")
                .writeLine("updated_at INTEGER,");

        for (ColumnSpecification column : columns) {
            codeWriter.writeLine(column.getName() + " " + column.type() + ",");
        }

        codeWriter.backspace().backspace().unIndent().writeLine("").writeLine(");");

        for (ColumnSpecification column : columns) {
            if (column.isIndexed()) {
                codeWriter.writeLine("CREATE INDEX IF NOT EXISTS").indent()
                        .writeLine("index_" + table + "_" + column.getName() + " ON " + table + "(" + column.getName() + ");").unIndent();
            }
        }

        return codeWriter.compile();
    }
}