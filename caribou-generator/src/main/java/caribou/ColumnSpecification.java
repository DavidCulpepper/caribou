package caribou;

public interface ColumnSpecification {
    String getName();
    String type();
    boolean isIndexed();
}
