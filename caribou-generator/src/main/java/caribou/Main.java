package caribou;

import caribou.impl.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class Main implements Runnable {

    public static void main(String[] args) {
        new Main(args).run();
    }

    private Generator generator;
    private String[] arguments;
    private Properties properties;

    public Main(String[] args) {
        NameFormatter formatter = new SnakeCaseNameFormatter();
        ColumnParser columnParser = new ActiveRecordStyleColumnParser(formatter);
        RequestParser parser = new ActiveRecordStyleParser(columnParser);
        CodeWriter code = new CodeWriter(4);
        Interpreter interpreter = new SqlInterpreter(formatter, code);
        this.generator = new GeneratorImpl(parser, interpreter);
        this.arguments = args;
    }

    @Override
    public void run() throws RuntimeException {
        try {
            loadProperties();

            String command = "";
            for (String s : arguments) {
                command += s + " ";
            }

            Migration m = generator.generate(command.trim());

            Date now = new Date();
            String timestamp = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", now);

            if (m.getName().trim().isEmpty()) {
                System.out.println("Please specify a migration to create");
            } else {
                File file = new File("test");
                file = new File(file, properties.getProperty("name"));
                file = new File(file, "migrations");
                file.mkdirs();
                FileWriter writer = new FileWriter(new File(file, timestamp + "_" + m.getName() + ".sql"));
                writer.write(m.getContent());
                writer.close();
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadProperties() throws IOException {
        properties = new Properties();
        loadDefaultProperties();
        loadFileProperties();
        loadCommandLineProperties();
    }

    private void loadDefaultProperties() {
        properties.setProperty("name", "data");
        properties.setProperty("path", "test");
    }

    private void loadFileProperties() throws IOException {
        if (new File("caribou.properties").exists()) {
            FileInputStream propertyStream = new FileInputStream("caribou.properties");
            properties.load(propertyStream);
            propertyStream.close();
        }
    }

    private void loadCommandLineProperties() {
        for (int i = 0; i < arguments.length; ++i) {
            String arg = arguments[i];
            if (arg.startsWith("--")) {
                arg = arg.substring(2);
                String[] split = arg.split("=");
                properties.setProperty(split[0], split[1]);
                arguments[i] = "";
            }
        }
    }
}
