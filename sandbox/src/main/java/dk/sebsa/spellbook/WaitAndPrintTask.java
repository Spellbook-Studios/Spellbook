package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.threading.Task;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Waits for 1-10 seconds and prints an input string afterwars
 *
 * @author sebs
 * @since 1.0.0
 */
public class WaitAndPrintTask extends Task {
    private final String out;

    /**
     * @param out Output of print
     */
    public WaitAndPrintTask(String out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Wait & Print Task";
    }

    @Override
    public void execute() {
        try {
            Thread.sleep((long) (ThreadLocalRandom.current().nextFloat() * 10 * 1000));
        } catch (InterruptedException ignored) {
            // IDK THIS IS JUST A TEST TASK
        }

        System.out.println(out);
    }
}
