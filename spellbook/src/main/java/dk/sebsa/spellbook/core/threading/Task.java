package dk.sebsa.spellbook.core.threading;

import dk.sebsa.spellbook.math.Time;

public interface Task {
    String name();
    void run();
    long startTime = Time.getTime();
}
