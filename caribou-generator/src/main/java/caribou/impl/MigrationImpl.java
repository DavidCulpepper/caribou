package caribou.impl;

import caribou.Migration;

public class MigrationImpl implements Migration {
    private final String name;
    private final String content;

    public MigrationImpl(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContent() {
        return content;
    }
}
