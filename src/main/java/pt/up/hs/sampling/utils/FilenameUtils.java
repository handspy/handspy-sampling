package pt.up.hs.sampling.utils;

/**
 * Utilities to handle file names.
 *
 * @author J
 */
public class FilenameUtils {

    /**
     * Get extension from file name/path.
     *
     * @param filename {@link String} file name/path.
     * @return {@link String} extension.
     *
     * @see <a href="https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java/3571239#15998870">SO answer</a>
     */
    public static String getFileExtension(String filename) {
        String afterLastSlash = filename.substring(filename.lastIndexOf('/') + 1);
        int afterLastBackslash = afterLastSlash.lastIndexOf('\\') + 1;
        int dotIndex = afterLastSlash.indexOf('.', afterLastBackslash);
        return (dotIndex == -1) ? "" : afterLastSlash.substring(dotIndex + 1);
    }
}
