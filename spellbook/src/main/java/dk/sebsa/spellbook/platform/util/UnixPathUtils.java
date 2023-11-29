package dk.sebsa.spellbook.platform.util;

import dk.sebsa.spellbook.util.PathUtils;

import java.util.Map;

/**
 * Unix implementation of path utils
 *
 * @author sebs
 * @since 1.0.0
 */
public class UnixPathUtils extends PathUtils {
    private final Map<String, String> enviroment;

    /**
     * Creates UnixPathUtils with System.getenv() for enviroment variables
     */
    public UnixPathUtils() {
        this.enviroment = System.getenv();
    }

    @Override
    public String getUserLocalDataDir(String appname, String namespace) {
        String dir = enviroment.getOrDefault("XDG_DATA_HOME", buildPath(home(), "/.local/share"));
        return buildPath(dir, appname, namespace);
    }
}
