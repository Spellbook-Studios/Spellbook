package dk.sebsa.spellbook;

import dk.sebsa.spellbook.extensions.SpellbookExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaApplication;

import java.util.List;

public class ApplicationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        // Application
        target.getPluginManager().apply("application");
        var application = (JavaApplication) target.getExtensions().findByName("application");

        // Common
        SpellbookExtension ext = target.getExtensions().create("spellbook", SpellbookExtension.class);
        target.afterEvaluate(project -> {
            DependencyManager.addRepositories(project);
            DependencyManager.addCommonDependencies(project, ext, ext.targetOs);
            if (ext.targetOs.isMacOsX()) application.setApplicationDefaultJvmArgs(List.of("-XstartOnFirstThread"));

            JavaUtil.setJavaTarget(project, ext.targetJava);
        });
    }
}