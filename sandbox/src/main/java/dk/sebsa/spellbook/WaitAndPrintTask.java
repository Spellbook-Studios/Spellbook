package dk.sebsa.spellbook;

import dk.sebsa.spellbook.core.threading.Task;
import lombok.CustomLog;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Waits for 1-10 seconds and prints an input string afterwadrs
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
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
    public void execute() throws InterruptedException {
        Thread.sleep((long) (ThreadLocalRandom.current().nextFloat() * 10 * 1000));

        log.log(out);
    }
}
