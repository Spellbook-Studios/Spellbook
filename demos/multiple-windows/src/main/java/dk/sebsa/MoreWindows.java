package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.FolderAssetProvider;
import dk.sebsa.spellbook.core.Application;
import dk.sebsa.spellbook.core.events.EngineBuildRenderPipelineEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import dk.sebsa.spellbook.math.Color;
import dk.sebsa.spellbook.opengl.stages.SpriteStage;

import java.io.File;

public class MoreWindows implements Application {
    public static MoreWindows instance;

    public static void main(String[] args) {
        instance = new MoreWindows();

        Spellbook.start(instance, SpellbookCapabilities.builder()
                .spellbookDebug(true)
                .logDisableASCIIEscapeCharacters(false)
                .clearColor(Color.azure)
                .build().addAssetProvider(new FolderAssetProvider(new File("assets/"), "mw")));
    }

    @EventListener
    public void engineBuildRenderPipeline(EngineBuildRenderPipelineEvent e) {
        e.builder.appendStage(new SpriteStage(e));
        e.appendUIStage();
    }

    @Override
    public String name() {
        return "MoreWindows";
    }

    @Override
    public String author() {
        return "Sebsa";
    }

    @Override
    public String version() {
        return "1";
    }
}
