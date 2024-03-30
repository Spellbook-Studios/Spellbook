package dk.sebsa.spellbook;

import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

public class JavaUtil {
    public static void setJavaTarget(Project target, int javaVersion) {
        String javaString = "1." + javaVersion;

        target.getTasks().withType(JavaCompile.class).configureEach(task -> {
            task.setSourceCompatibility(javaString);
            task.setTargetCompatibility(javaString);
            task.getOptions().getRelease().set(javaVersion);
            task.getOptions().setEncoding("UTF-8");
        });
    }
}
