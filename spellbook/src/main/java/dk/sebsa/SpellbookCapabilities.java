package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.AssetProvider;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebsn
 * @since 0.0.1
 */
@Builder
public class SpellbookCapabilities {
    /**
     * Weather logging and other debugging functionalities should be enabled
     * This should not be enabled in production unless the user requests it
     */
    @Builder.Default public final boolean spellbookDebug = true;
    /**
     * What rendering framework Spellbook uses
     */
    @Builder.Default public final Rendering renderingProvider = Rendering.opengl;

    /**
     * A list of locations to load assets from.
     * Per default, it only loads the spellbook library assets
     */
    @Getter private final List<AssetProvider> assetsProviders = new ArrayList<>();

    /**
     * If logStorageMode is not "dont" where should the logs be stored
     */
    @Builder.Default public final String logStoreTarget = "./logs/latest.log";
    @Builder.Default public final LogStorageModes logStorageMode = LogStorageModes.zipped;
    /**
     * Weather to disable the functionality that appends ASCIIEscapeCharacters to all log messages
     * If false the logs should be coloured according to their severity
     */
    @Builder.Default public final boolean logDisableASCIIEscapeCharacters = true;


    /**
     * Adds an AssetProvider to Spellbook
     * Must be done before starting Spellbook
     * @param provider The AssetProvider to add
     * @return This
     */
    public SpellbookCapabilities addAssetProvider(AssetProvider provider) {
        this.assetsProviders.add(provider);
        return this;
    }

    public enum Rendering {
        none,
        opengl
    }

    public enum LogStorageModes {
        dont,
        txt,
        zipped,
    }
}
