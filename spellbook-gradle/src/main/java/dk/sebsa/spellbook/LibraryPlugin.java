package dk.sebsa.spellbook;

import dk.sebsa.spellbook.extensions.SpellbookExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class LibraryPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        // Library
        target.getPluginManager().apply("java-library");
        SpellbookExtension ext = target.getExtensions().create("spellbook", SpellbookExtension.class);

        // Common
        target.afterEvaluate(project -> {
            DependencyManager.addRepositories(project);
            DependencyManager.addCommonDependencies(project, ext, ext.targetOs);

            JavaUtil.setJavaTarget(project, ext.targetJava);
        });
    }
}