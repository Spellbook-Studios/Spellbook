package dk.sebsa.spellbook.audio;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.asset.AssetReference;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Contains an OGG Vorbis sound
 * @author sebs
 * @since 1.0.0
 */
public class Sound implements Asset {
    @Getter private int soundBufferId;

    @Override
    public void load(AssetReference location) {
        soundBufferId = AL11.alGenBuffers();

        // Load vorbis file in PCM format
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(location, info);
        } catch (IOException e) {
            Spellbook.instance.error("Failed to load Sound, IOException: " + location, false);
        }
    }

    @Override
    public void destroy() {
        AL11.alDeleteBuffers(soundBufferId);
    }

    private ShortBuffer readVorbis(AssetReference file, STBVorbisInfo info) throws IOException {
        // Load texture from stream
        InputStream is = FileUtils.loadFile(file.location);
        byte[] bytes = new byte[8000];
        int curByte;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while((curByte = is.read(bytes)) != -1) { bos.write(bytes, 0, curByte);}
        is.close();

        bytes = bos.toByteArray();
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
        buffer.put(bytes).flip();

        // Load file into memory
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer error = stack.mallocInt(1);

            long decoder = stb_vorbis_open_memory(buffer, error, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            ShortBuffer result = MemoryUtil.memAllocShort(lengthSamples * channels);

            result.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, result) * channels);
            stb_vorbis_close(decoder);

            return result;
        }
    }
}
