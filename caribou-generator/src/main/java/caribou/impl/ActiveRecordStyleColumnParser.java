package caribou.impl;

import caribou.ColumnParser;
import caribou.ColumnSpecification;
import caribou.NameFormatter;
import caribou.values.ColumnImpl;

public class ActiveRecordStyleColumnParser implements ColumnParser {

    private final NameFormatter nameFormatter;

    public ActiveRecordStyleColumnParser(NameFormatter nameFormatter) {
        this.nameFormatter = nameFormatter;
    }

    @Override
    public ColumnSpecification parse(String column) {
        String[] pieces = column.split(":");

        String name = nameFormatter.format(pieces[0]);
        String type = "";

        boolean isIndexed = false;

        for (int i = 1; i < pieces.length; ++i) {
            if ("index".equals(pieces[i])) {
                isIndexed = true;
            } else if ("references".equals(pieces[i])) {
                isIndexed = true;
                name += "_id";
                type = "INTEGER";
            } else {
                type = pieces[i];
            }
        }

        return new ColumnImpl(name, type, isIndexed);
    }
}
