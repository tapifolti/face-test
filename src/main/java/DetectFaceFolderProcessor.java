import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class DetectFaceFolderProcessor implements FolderProcessor {

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

    private ProcessedResult result = new ProcessedResult();
    @Override
    public ProcessedResult getResult() {
        return result;
    }

    @Override
    public String forEachFile(Path file) {
        return "xxx"; // faceAPI.detect(file);
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

    @Override
    public ProcessedResult process(Path rootFolder, String excludeFolderPattern, String excludeFilePattern) {
        this.excludeFolderPattern = excludeFolderPattern;
        this.excludeFilePattern = excludeFilePattern;
        try {
            Files.walkFileTree(rootFolder, new PhotoVisitor(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
