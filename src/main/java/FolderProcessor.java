import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tapifolti on 2/17/2017.
 */

/***
 * walks all one-level sub-folders looking for files to process
 * omits sub-folders which match to the exclude pattern
 */
public interface FolderProcessor {

    public static class Face {

        public Face(Path filePath, String faceId) {
            this.filePath = filePath;
            this.faceId = faceId;
        }

        private Path    filePath;
        public Path getFilePath() {
            return filePath;
        }
        public Path getFilePathParent() {
            return filePath.getParent();
        }

        private String  faceId;
        public String getFaceId() {
            return faceId;
        }

    }
    public static class ProcessedResult {
        private List<Face> successfullyProcessed = new ArrayList<Face>();
        public List<Face> getSuccessfullyProcessed() {
            return successfullyProcessed;
        }

        public void addSuccessfullyProcessed(Face face) {
            successfullyProcessed.add(face);
        }

        private List<Path> failedToProcess = new ArrayList<Path>();
        public List<Path> getFailedToProcess() {
            return failedToProcess;
        }

        public void addFailedToProcess(Path failedToProcessPath) {
            failedToProcess.add(failedToProcessPath);
        }
    }

    public String getExcludeFolderPattern();
    public String getExcludeFilePattern();

    /***
     * call to get processor's result object
     * @return
     */
    public ProcessedResult getResult();

    /***
     * call it for each file in each subfolder
     * @param file
     * @return
     */
    public String forEachFile(Path file);

    /***
     * call it for the first file (ordered by 'date modified') in each sub-folder
     * @param file
     * @return
     */
    public String forFirstFile(Path file);

    /***
     * call it on a randomly chosen file
     * @param file
     * @return
     */
    public String forRandomFile(Path file);

    /***
     * process files in the sub-folders of rootFolder
     * excludes sub-folders if match to excludeFolderPattern
     * excludes file if matches to the excludeFilePattern
     * enlists files ordered by 'date modified'
     * @param rootFolder
     * @param excludeFolderPattern
     * @param excludeFilePattern
     */
    public ProcessedResult process(Path rootFolder, String excludeFolderPattern, String excludeFilePattern);
}
