package dk.sebsa.spellbook.audio;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.loading.AssetLocation;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Contains an OGG Vorbis sound
 * @author sebs
 * @since 1.0.0
 */
@Getter
public class Sound extends Asset {
    private int soundBufferId;

    @Override
    public void load() {
        soundBufferId = AL11.alGenBuffers();

        // Load vorbis file in PCM format
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(location, info);
            alBufferData(soundBufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        } catch (IOException e) {
            Spellbook.instance.error("Failed to load Sound, IOException: " + location, false);
        }
    }

    @Override
    public void destroy() {
        AL11.alDeleteBuffers(soundBufferId);
    }

    private ShortBuffer readVorbis(AssetLocation file, STBVorbisInfo info) throws IOException {
        // Load file into memory
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer vorbis = FileUtils.isToBB(FileUtils.loadFile(file.location()), 32 * 1024);
            IntBuffer error = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);

            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            ShortBuffer pcm = MemoryUtil.memAllocShort(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }
}
