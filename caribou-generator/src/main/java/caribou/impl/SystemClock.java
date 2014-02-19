package caribou.impl;

import caribou.Clock;

public class SystemClock implements Clock {
    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }
}
