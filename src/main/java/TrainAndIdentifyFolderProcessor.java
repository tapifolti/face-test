import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class TrainAndIdentifyFolderProcessor implements FolderProcessor {

    public TrainAndIdentifyFolderProcessor(String excludeFolderPattern, String excludeFilePattern) {
        this.excludeFolderPattern = excludeFolderPattern;
        this.excludeFilePattern = excludeFilePattern;
    }

    public enum Phases {SETUP_PERSON, IDENTIFY};
    private Phases phase = Phases.SETUP_PERSON;
    public void setPhase(Phases phase) {
        this.phase = phase;
    }
    public Phases getPhase() {
        return phase;
    }

    private String excludeFolderPattern;
    private String excludeFilePattern;
    @Override
    public String getExcludeFolderPattern() {return excludeFolderPattern;}
    @Override
    public String getExcludeFilePattern() {
        return excludeFilePattern;
    }

    @Override
    public String forEachFile(Path file)
    {
        // TODO
        return "";
    }

    @Override
    public String forFirstFile(Path file) {
        // TODO
        return "";
    }

    @Override
    public String forRandomFile(Path file) {
        // TODO
        return "";
    }

}
