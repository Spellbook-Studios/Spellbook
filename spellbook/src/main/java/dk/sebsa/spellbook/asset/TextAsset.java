package dk.sebsa.spellbook.asset;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Text loaded from a file
 *
 * @author sebs
 * @since 1.0.0
 */
@Getter
public class TextAsset extends Asset {
    private String text;

    @Override
    public void load() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = FileUtils.loadFile(location);
            sb.append(is);
            is.close();
        } catch (IOException e) {
            Spellbook.instance.error("TextAsset: Failed to load file at location: " + location, false);
        }
        text = sb.toString();
    }

    @Override
    public void destroy() {

    }
}
