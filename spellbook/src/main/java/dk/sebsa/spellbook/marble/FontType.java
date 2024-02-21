package dk.sebsa.spellbook.marble;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.CustomLog;
import lombok.Getter;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * A font type that can be used to genereate fonts
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
@Getter
public class FontType extends Asset {
    private STBTTFontinfo info;
    public ByteBuffer ttf;

    @Getter
    private int ascent;
    @Getter
    private int descent;
    @Getter
    private int lineGap;

    @Override
    protected void load() {
        Spellbook.instance.getRenderer().generateFontType(this);
    }

    public void loadSTBTTFontInfo() throws IOException {
        ttf = FileUtils.isToBB(FileUtils.loadFile(location), 512 * 1024);
        info = STBTTFontinfo.create();

        if(!stbtt_InitFont(info, ttf)) {
            Spellbook.instance.error("Failed to initialize STBTTFontInfo!! (" + location + ")", false);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pAscent  = stack.mallocInt(1);
            IntBuffer pDescent = stack.mallocInt(1);
            IntBuffer pLineGap = stack.mallocInt(1);

            stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap);

            ascent = pAscent.get(0);
            descent = pDescent.get(0);
            lineGap = pLineGap.get(0);
        }
    }

    @Override
    protected void destroy() {
        Spellbook.instance.getRenderer().queue(info::free);
    }
}
