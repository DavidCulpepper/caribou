package caribou;

import java.util.List;

/**
 * Created by dculpepper on 2/18/14.
 */
public interface Request {
    String getPrimaryRequest();
    List<ColumnSpecification> getColumns();
}
