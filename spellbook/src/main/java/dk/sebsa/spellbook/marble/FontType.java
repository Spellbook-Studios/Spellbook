package dk.sebsa.spellbook.marble;

import dk.sebsa.spellbook.asset.Asset;
import dk.sebsa.spellbook.util.FileUtils;
import lombok.CustomLog;
import lombok.Getter;

@CustomLog
@Getter
public class FontType extends Asset {
    private java.awt.Font font;

    @Override
    protected void load() {
        if (font == null)
            try {
                font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, FileUtils.loadFile(location));
            } catch (Exception e) {
                logger.err("Failed to load font: " + location.location());
                logger.err(logger.stackTrace(e));
            }
    }

    @Override
    protected void destroy() {

    }
}
