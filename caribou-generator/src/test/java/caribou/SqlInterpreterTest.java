package caribou;

import caribou.impl.CodeWriter;
import caribou.impl.SnakeCaseNameFormatter;
import caribou.impl.SqlInterpreter;
import caribou.values.ColumnImpl;
import caribou.values.RequestImpl;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlInterpreterTest {

    private SqlInterpreter sut;

    private CodeWriter writer = new CodeWriter(4);
    private NameFormatter formatter = new SnakeCaseNameFormatter();

    @Test
    public void shouldCreateSingleAlterTableStatement() throws Exception {
        sut = new SqlInterpreter(formatter, writer);
        Request request = new RequestImpl("add_part_number_to_products", new ArrayList<ColumnSpecification>() {{
            add(new ColumnImpl("part_number", "text", false));
        }});
        Migration m = sut.interpret(request);
        assertThat(m.getContent(), is(
                "ALTER TABLE products\n" +
                "    ADD part_number text;\n"
        ));
    }

    @Test
    public void shouldCreateSingleAlterTableStatementWithIndex() throws Exception {
        sut = new SqlInterpreter(formatter, writer);
        Request request = new RequestImpl("add_part_number_to_products", new ArrayList<ColumnSpecification>() {{
            add(new ColumnImpl("part_number", "text", true));
        }});
        Migration m = sut.interpret(request);
        assertThat(m.getContent(), is(
                "ALTER TABLE products\n" +
                "    ADD part_number text;\n" +
                        "CREATE INDEX IF NOT EXISTS\n" +
                        "    index_products_part_number ON products(part_number);\n"
        ));
    }

    @Test
    public void shouldCreateMultipleAlterTableStatements() throws Exception {
        sut = new SqlInterpreter(formatter, writer);
        Request request = new RequestImpl("add_part_number_to_products", new ArrayList<ColumnSpecification>() {{
            add(new ColumnImpl("part_number", "text", false));
            add(new ColumnImpl("price", "real", false));
        }});
        Migration m = sut.interpret(request);
        assertThat(m.getContent(), is(
                "ALTER TABLE products\n" +
                        "    ADD part_number text;\n" +
                        "ALTER TABLE products\n" +
                        "    ADD price real;\n"
        ));
    }

    @Test
    public void shouldCreateCreateTableStatement() throws Exception {
        sut = new SqlInterpreter(formatter, writer);
        Request request = new RequestImpl("create_products", new ArrayList<ColumnSpecification>() {{
            add(new ColumnImpl("part_number", "text", false));
            add(new ColumnImpl("price", "real", false));
        }});
        Migration m = sut.interpret(request);
        assertThat(m.getContent(), is(
                "CREATE TABLE products (\n" +
                        "    _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    created_at INTEGER,\n" +
                        "    updated_at INTEGER,\n" +
                        "    part_number text,\n" +
                        "    price real\n" +
                        ");\n"
        ));
    }

    @Test
    public void shouldCreateJoinTable() throws Exception {
        sut = new SqlInterpreter(formatter, writer);
        Request request = new RequestImpl("create_join_table_users_products", new ArrayList<ColumnSpecification>() {{
            add(new ColumnImpl("users", null, false));
            add(new ColumnImpl("products", null, false));
        }});
        Migration m = sut.interpret(request);
        assertThat(m.getContent(), is(
                "CREATE TABLE join_users_products (\n" +
                        "    users_id INTEGER,\n" +
                        "    products_id INTEGER\n" +
                        ");\n" +
                        "CREATE INDEX IF NOT EXISTS\n" +
                        "    index_join_users_products_users_id ON join_users_products(users_id);\n" +
                        "CREATE INDEX IF NOT EXISTS\n" +
                        "    index_join_users_products_products_id ON join_users_products(products_id);\n"
        ));
    }
}
