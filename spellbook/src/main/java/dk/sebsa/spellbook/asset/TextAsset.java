package dk.sebsa.spellbook.asset;

import dk.sebsa.Spellbook;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

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
            Spellbook.instance.getLogger().err("TextAsset: Failed to load file at location: " + reference);
        }
        text = sb.toString();
    }

    @Override
    public void destroy() {

    }
}
