package dk.sebsa.spellbook.asset;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Text loaded from a file
 * @since 1.0.0
 * @author sebs
 */
public class TextAsset implements Asset {
    @Getter private String text;

    @Override
    public void load(AssetReference reference) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = FileUtils.loadFile(reference.location);
            sb.append(is);
            is.close();
        } catch (IOException e) {
            Spellbook.instance.error("TextAsset: Failed to load file at location: " + reference, false);
        }
        text = sb.toString();
    }

    @Override
    public void destroy() {

    }
}
