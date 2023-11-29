package dk.sebsa.spellbook.platform.util;

import dk.sebsa.spellbook.util.PathUtils;

/**
 * MaxOSX implementation of path utils
 *
 * @author sebs
 * @since 1.0.0
 */
public class MacOSXPathUtils extends PathUtils {
    @Override
    public String getUserLocalDataDir(String appname, String namespace) {
        return buildPath(home(), "/Library/Preferences", appname, namespace);
    }
}
