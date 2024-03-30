package dk.sebsa.spellbook;

import dk.sebsa.spellbook.extensions.SpellbookExtension;
import org.gradle.api.Project;
import org.gradle.internal.os.OperatingSystem;

import java.util.HashMap;
import java.util.Map;

public class DependencyManager {
    public static final String REPO_IMGUI_URL = "https://maven.realrobotix.me/imgui-java";
    public static final String REPO_SPELLBOOK_R_URL = "https://maven.sebsa.dk/releases";
    public static final String REPO_SPELLBOOK_S_URL = "https://maven.sebsa.dk/snapshots";

    public static void addRepositories(Project target) {
        target.getRepositories().mavenCentral();
        target.getRepositories().maven(repo -> {
            repo.setName("ImGUI realrobotix");
            repo.setUrl(REPO_IMGUI_URL);
        });
        target.getRepositories().maven(repo -> {
            repo.setName("Spellbook Repo Releases");
            repo.setUrl(REPO_SPELLBOOK_R_URL);
        });
        target.getRepositories().maven(repo -> {
            repo.setName("Spellbook Repo Snapshots");
            repo.setUrl(REPO_SPELLBOOK_S_URL);
        });
    }

    public static void addCommonDependencies(Project target, SpellbookExtension ext, OperatingSystem targetOs) {
        target.getDependencies().add("implementation", "dk.sebsa:mana:" + ext.manaVersion);

        target.getDependencies().add("compileOnly", "org.projectlombok:lombok:" + ext.lombokVersion);
        target.getDependencies().add("annotationProcessor", "org.projectlombok:lombok:" + ext.lombokVersion);

        target.getDependencies().add("implementation", "org.joml:joml:" + ext.jomlVersion);
        target.getDependencies().add("implementation", "net.java.dev.jna:jna-platform:5.13.0");

        // OS Specific
        String imguiNatives = "";
        String lwjglNatives = "";
        if (targetOs.isWindows()) {
            lwjglNatives = "natives-windows";
            imguiNatives = "natives-windows";
        } else if (targetOs.isLinux()) {
            lwjglNatives = "natives-linux";
            imguiNatives = "natives-linux";
        } else if (targetOs.isMacOsX()) {
            lwjglNatives = "natives-macos-arm64";
            imguiNatives = "natives-macos";
        }

        // Imgui
        target.getDependencies().add("implementation", "io.github.spair:imgui-java-binding:" + ext.imguiVersion);
        target.getDependencies().add("implementation", "io.github.spair:imgui-java-lwjgl3:" + ext.imguiVersion);
        target.getDependencies().add("implementation", "io.github.spair:imgui-java-" + imguiNatives + ":" + ext.imguiVersion);

        // Lwjgl
        String finalLwjglNatives = lwjglNatives; // Fuck IDEA
        target.getDependencies().platform("org.lwjgl:lwjgl-bom:" + ext.lwjglVersion);

        target.getDependencies().add("implementation",
                "org.lwjgl:lwjgl:"+ext.lwjglVersion);
        target.getDependencies().add("runtimeOnly",
                "org.lwjgl:lwjgl::"+ finalLwjglNatives);
        ext.lwjglModules.forEach(m -> {
            target.getDependencies().add("implementation",
                    "org.lwjgl:lwjgl-"+m+":"+ext.lwjglVersion);
            target.getDependencies().add("runtimeOnly",
                    "org.lwjgl:lwjgl-"+m+"::"+ finalLwjglNatives);
        });



        // Spellbook
        if(ext.getSpellbookVersion().isEmpty()) return;
        if(target.getRootProject().findProject(":spellbook") != null) {
            Map<String, String> pMap = new HashMap<>();
            pMap.put("path", ":spellbook");
            target.getDependencies().add("implementation", target.getDependencies().project(pMap));

        } else
            target.getDependencies().add("implementation", "dk.sebsa:spellbook:" + ext.getSpellbookVersion());
    }
}
