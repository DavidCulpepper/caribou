package caribou.values;

import caribou.ColumnSpecification;

/**
 * Created by dculpepper on 2/18/14.
 */
public class ColumnImpl implements ColumnSpecification {

    private final String name;
    private final String type;
    private final boolean isIndexed;

    public ColumnImpl(String name, String type, boolean isIndexed) {
        this.name = name;
        this.type = type;
        this.isIndexed = isIndexed;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public boolean isIndexed() {
        return isIndexed;
    }
}
