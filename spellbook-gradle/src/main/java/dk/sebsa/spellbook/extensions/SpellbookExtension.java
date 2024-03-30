package dk.sebsa.spellbook.extensions;

import org.gradle.internal.os.OperatingSystem;

import java.util.Arrays;
import java.util.List;

public class SpellbookExtension {
    public OperatingSystem targetOs = OperatingSystem.current();
    public int targetJava = 21;

    public String jomlVersion = "1.10.5";
    public String lwjglVersion = "3.3.3";
    public String imguiVersion = "1.86.11-10-g0dbf36c";
    public String lombokVersion = "1.18.30";
    public String manaVersion = "1.0d-SNAPSHOT";
    public List<String> lwjglModules = Arrays.asList("glfw", "opengl", "stb", "openal");
    private String spellbookVersion = "1.0.0-SNAPSHOT";

    public String getSpellbookVersion() {
        return spellbookVersion;
    }

    public void setSpellbookVersion(String spellbookVersion) {
        this.spellbookVersion = spellbookVersion;
    }
}
