package caribou;

/**
 * Created by dculpepper on 2/18/14.
 */
public interface Generator {
    Migration generate(String command);
}
