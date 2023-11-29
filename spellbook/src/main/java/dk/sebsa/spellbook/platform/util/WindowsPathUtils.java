package dk.sebsa.spellbook.platform.util;

import dk.sebsa.spellbook.util.PathUtils;

/**
 * Windows path utils implementation
 *
 * @author sebs
 * @since 1.0.0
 */
public class WindowsPathUtils extends PathUtils {
    @Override
    public String getUserLocalDataDir(String appname, String namespace) {
        return buildPath(WindowsUtilities.resolveFolder(WindowsUtilities.FolderId.LOCAL_APPDATA), "Spellbook", appname, namespace);
    }
}
