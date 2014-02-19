package caribou.impl;

import caribou.NameFormatter;

public class SnakeCaseNameFormatter implements NameFormatter {
    @Override
    public String format(String name) {
        StringBuilder builder = new StringBuilder(name.length());

        for (int i = 0; i < name.length(); ++i) {
            char letter = name.charAt(i);
            if (Character.isUpperCase(letter)) {
                builder.append(' ');
            }
            builder.append(letter);
        }
        name = builder.toString().trim().toLowerCase();
        return name.replace(' ', '_').replace('-', '_');
    }
}
