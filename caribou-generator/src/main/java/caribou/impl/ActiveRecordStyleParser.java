package caribou.impl;

import caribou.ColumnParser;
import caribou.ColumnSpecification;
import caribou.Request;
import caribou.RequestParser;
import caribou.values.RequestImpl;

import java.util.ArrayList;
import java.util.List;

public class ActiveRecordStyleParser implements RequestParser {

    private final ColumnParser columnParser;

    public ActiveRecordStyleParser(ColumnParser columnParser) {
        this.columnParser = columnParser;
    }

    @Override
    public Request parse(String request) {
        String[] parts = request.split("(\\s)+");
        String primaryRequest = parts[0];

        List<ColumnSpecification> columns = new ArrayList<ColumnSpecification>();

        for (int i = 1; i < parts.length; ++i) {
            columns.add(columnParser.parse(parts[i]));
        }

        return new RequestImpl(primaryRequest, columns);
    }
}
