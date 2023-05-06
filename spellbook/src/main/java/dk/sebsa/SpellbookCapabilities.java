package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.AssetProvider;
import dk.sebsa.spellbook.math.Rect;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebsn
 * @since 1.0.0
 */
@Builder
public class SpellbookCapabilities {
    /**
     * Weather logging and other debugging functionalities should be enabled
     * This should not be enabled in production unless the user requests it
     */
    @Builder.Default public final boolean spellbookDebug = true;

    /**
     * Weather IMGUI rendering for debug menus should be enabled, and added to the layer stack
     * Should not be used in production
     * Should not be used in production
     * Should not be used in production
     */
    @Builder.Default public final boolean debugIMGUI = false;

    /**
     * What rendering framework Spellbook uses
     */
    @Builder.Default public final Rendering renderingProvider = Rendering.opengl;

    /**
     * Enables / disables rendering of 2d sprites
     */
    @Builder.Default public final boolean render2D = true;

    /**
     * Must be > 0
     * The amount of layers to prepare for placing spriterenderes when rendering 2d sprites
     */
    @Builder.Default public final int spriteMaxLayer = 3;

    /**
     * Weather to enable ECS updates
     */
    @Builder.Default public final boolean ecs = true;

    /**
     * Weather the OpenAL audioModule should be enabled;
     */
    @Builder.Default public final boolean audio = true;

    /**
     * Render Resolution
     */
    @Builder.Default public final Rect renderResolution = new Rect(0,0,960,540);

    /**
     * A list of locations to load assets from.
     * Per default, it only loads the spellbook library assets
     */
    @Getter private final List<AssetProvider> assetsProviders = new ArrayList<>();

    /**
     * Enable / Disable the 2d physics system
     */
    public final boolean newton2D = true;

    /**
     * If logStorageMode is not "dont" where should the logs be stored
     */
    @Builder.Default public final String logStoreTarget = "./logs/latest.log";
    /**
     * Denotes how logs are stored
     */
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

    /**
     * Denotes which rendering library is used
     */
    public enum Rendering {
        /**
         * Doesn't render?????
         */
        none,
        /**
         * Uses OpenGL for rendering
         */
        opengl
    }

    /**
     * Denotes how logs are stored on disk
     */
    public enum LogStorageModes {
        /**
         * Doesn't store log on disc
         */
        dont,
        /**
         * Writes the log to latest.log
         * Overwriting previous logs
         */
        txt,
        /**
         * Zips the log file into a zip in the log directory
         */
        zipped,
    }
}
