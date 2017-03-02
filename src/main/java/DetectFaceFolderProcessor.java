import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class DetectFaceFolderProcessor implements FolderProcessor {

    public DetectFaceFolderProcessor(String excludeFolderPattern, String excludeFilePattern) {
        this.excludeFolderPattern = excludeFolderPattern;
        this.excludeFilePattern = excludeFilePattern;
    }

    private String excludeFolderPattern;
    @Override
    public String getExcludeFolderPattern() {
        return excludeFolderPattern;
    }

    private String excludeFilePattern;
    @Override
    public String getExcludeFilePattern() {
        return excludeFilePattern;
    }

    DetectFaceAPI faceAPI = new DetectFaceAPI();

    @Override
    public String forEachFile(Path file) {
        return faceAPI.detect(file);
    }

    @Override
    public String forFirstFile(Path file) {
        // does nothing
        return "";
    }

    @Override
    public String forRandomFile(Path file) {
        // does nothing
        return "";
    }
}
