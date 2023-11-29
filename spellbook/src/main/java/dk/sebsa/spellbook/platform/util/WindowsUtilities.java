package dk.sebsa.spellbook.platform.util;

import com.sun.jna.platform.win32.*;
import dk.sebsa.spellbook.platform.PlatformException;

/**
 * Utilities for windows
 *
 * @author sebs
 * @since 1.0.0
 */
public class WindowsUtilities {
    /**
     * ID's for common Windows folders
     */
    public enum FolderId {
        /**
         * Located under C:\Users\<usr>\AppData\Roaming
         * This data is shared between the user's pc's
         */
        APPDATA,
        /**
         * Located under C:\Users\<usr>\AppData\Local
         * This data is not shared across multiple pc's
         */
        LOCAL_APPDATA;
    }

    /**
     * Resolves a windows directory using Shell32Util
     *
     * @param folderId The folder to resolve path for
     * @return The path to the folder
     */
    public static String resolveFolder(FolderId folderId) {
        try {
            return Shell32Util.getKnownFolderPath(convertFolderIdToGuid(folderId));
        } catch (Win32Exception e) {
            throw new PlatformException(
                    "SHGetKnownFolderPath returns an error: " + e.getErrorCode());
        } catch (UnsatisfiedLinkError e) {
            // Fallback for pre-vista OSes. #5
            try {
                int folder = convertFolderIdToCsidl(folderId);
                return Shell32Util.getFolderPath(folder);
            } catch (Win32Exception e2) {
                throw new PlatformException(
                        "SHGetFolderPath returns an error: " + e2.getErrorCode());
            }
        }
    }

    private static Guid.GUID convertFolderIdToGuid(FolderId folderId) {
        return switch (folderId) {
            case APPDATA -> KnownFolders.FOLDERID_RoamingAppData;
            case LOCAL_APPDATA -> KnownFolders.FOLDERID_LocalAppData;
            default -> throw new PlatformException(
                    "Unknown folder ID " + folderId + " was specified.");
        };
    }

    protected static int convertFolderIdToCsidl(FolderId folderId) {
        return switch (folderId) {
            case APPDATA -> ShlObj.CSIDL_APPDATA;
            case LOCAL_APPDATA -> ShlObj.CSIDL_LOCAL_APPDATA;
            default -> throw new PlatformException(
                    "Unknown folder ID " + folderId + " was specified.");
        };
    }
}
