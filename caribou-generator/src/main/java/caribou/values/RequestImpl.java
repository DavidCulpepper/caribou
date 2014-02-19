package caribou.values;

import caribou.ColumnSpecification;
import caribou.Request;

import java.util.Collections;
import java.util.List;

public class RequestImpl implements Request {

    private final String primaryRequest;
    private final List<ColumnSpecification> columns;

    public RequestImpl(String primaryRequest, List<ColumnSpecification> columns) {
        this.primaryRequest = primaryRequest;
        this.columns = columns;
    }

    @Override
    public String getPrimaryRequest() {
        return primaryRequest;
    }

    @Override
    public List<ColumnSpecification> getColumns() {
        return Collections.unmodifiableList(columns);
    }
}
