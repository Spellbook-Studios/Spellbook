/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dk.sebsa.spellbook;

import dk.sebsa.Spellbook;
import dk.sebsa.SpellbookCapabilities;
import dk.sebsa.spellbook.core.Application;

public class Sandbox extends Application {
    public static void main(String[] args) {
        Spellbook.start(new Sandbox(), SpellbookCapabilities.builder()
                        .spellbookDebug(true)
                        .build());
    }

    @Override
    public String name() {
        return "Sandbox";
    }

    @Override
    public String author() {
        return "Sebsa";
    }

    @Override
    public String version() {
        return "1.0a";
    }
}
