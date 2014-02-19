package caribou;

import caribou.impl.SnakeCaseNameFormatter;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SnakeCaseNameFormatterTest {

    private NameFormatter sut = new SnakeCaseNameFormatter();

    @Test
    public void shouldLowercaseSingleWords() {
        String formattedName = sut.format("testing");
        assertThat(formattedName, is("testing"));
    }

    @Test
    public void shouldPlaceUnderscoreBetweenWords() {
        String formattedName = sut.format("testing_multi_word");
        assertThat(formattedName, is("testing_multi_word"));
    }

    @Test
    public void shouldConvertDashesToUnderscores() {
        String formattedName = sut.format("testing-multi-word");
        assertThat(formattedName, is("testing_multi_word"));
    }

    @Test
    public void shouldDetectCapitalsAsNewWord() {
        String formattedName = sut.format("TestingMultiWord");
        assertThat(formattedName, is("testing_multi_word"));
    }
}
