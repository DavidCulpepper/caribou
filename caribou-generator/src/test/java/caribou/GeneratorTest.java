package caribou;

import caribou.impl.GeneratorImpl;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeneratorTest {

    @Tested GeneratorImpl sut;
    @Injectable Interpreter mockInterpreter;
    @Injectable RequestParser mockParser;

    @Test
    public void shouldCreateMigrationWithCorrectName() throws Exception {
        new Expectations() {{
            mockParser.parse("AddPartNumberToProducts title:text");
        }};

        sut.generate("AddPartNumberToProducts title:text");
    }
}