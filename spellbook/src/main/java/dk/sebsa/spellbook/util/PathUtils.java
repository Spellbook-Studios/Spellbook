package dk.sebsa.spellbook.util;

import dk.sebsa.spellbook.platform.util.MacOSXPathUtils;
import dk.sebsa.spellbook.platform.util.UnixPathUtils;
import dk.sebsa.spellbook.platform.util.WindowsPathUtils;

import java.nio.file.FileSystems;

/**
 * Platform abstract function for getting paths to spercific kinds of directories
 *
 * @author sebs
 * @since 1.0.0
 */
public abstract class PathUtils {
    /**
     * Generates the correct platform specific PathUtils instance
     *
     * @return Correct platform pathutils
     */
    public static PathUtils getInstance() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac os x")) {
            return new MacOSXPathUtils();
        } else if (os.startsWith("windows")) {
            return new WindowsPathUtils();
        } else {
            // Assume other *nix.
            return new UnixPathUtils();
        }
    }

    /**
     * Gets the Users Local(as in not roaming) data directory
     *
     * @param appname   The name of the application
     * @param namespace Namespace files will be founder under folder with this name
     * @return The path to the directory
     */
    public abstract String getUserLocalDataDir(String appname, String namespace);

    protected String home() {
        return System.getProperty("user.home");
    }

    protected String buildPath(String... elems) {
        String separator = FileSystems.getDefault().getSeparator();
        StringBuilder buffer = new StringBuilder();
        String lastElem = null;
        for (String elem : elems) {
            if (elem == null) {
                continue;
            }

            if (lastElem == null) {
                buffer.append(elem);
            } else if (lastElem.endsWith(separator)) {
                buffer.append(elem.startsWith(separator) ? elem.substring(1) : elem);
            } else {
                if (!elem.startsWith(separator)) {
                    buffer.append(separator);
                }
                buffer.append(elem);
            }
            lastElem = elem;
        }
        return buffer.toString();
    }
}
