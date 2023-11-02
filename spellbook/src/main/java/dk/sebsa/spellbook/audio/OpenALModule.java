package dk.sebsa.spellbook.audio;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.core.Module;
import dk.sebsa.spellbook.core.events.EngineInitEvent;
import dk.sebsa.spellbook.core.events.EventListener;
import lombok.CustomLog;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Module that handles the setup and lifetime of OpenAL
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class OpenALModule implements Module {
    private long device;
    private long context;

    @EventListener
    public void engineInit(EngineInitEvent e) {
        logger.log("Creating OpenAL context & device");

        this.device = ALC10.alcOpenDevice((ByteBuffer) null); // Gets default audio device
        if (device == MemoryUtil.NULL) {
            Spellbook.instance.error("Failed to open the default OpenAL device", true);
            return;
        }

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        this.context = ALC10.alcCreateContext(device, (IntBuffer) null); // Create AL context with no extra specified attribute tokens
        if (context == MemoryUtil.NULL) {
            Spellbook.instance.error("Failed to create OpenAL context", true);
            return;
        }

        logger.log("Setup done!");
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    @Override
    public void cleanup() {
        logger.log("Cleanup OpenAL context & device");
        if (context != MemoryUtil.NULL) ALC10.alcDestroyContext(context);
        if (device != MemoryUtil.NULL) ALC10.alcCloseDevice(device);
    }

    @Override
    public String name() {
        return "Audio<OpenAL>";
    }
}
