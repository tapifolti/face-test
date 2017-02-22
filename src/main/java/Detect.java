import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by tapifolti on 2/18/2017.
 */
public class Detect {

    public static void main(String[] args) {

        if (args.length < 1 || args.length > 3) {
            System.out.println("Detect rootFolder [excludeFolderPattern] [excludeFilePattern]");
            return;
        }
        DetectFaceFolderProcessor processor = new DetectFaceFolderProcessor();
        String excludeFolderPattern = "";
        String excludeFilePattern = "";
        if (args.length >= 2) {
            excludeFolderPattern = args[1];
        }
        if (args.length == 3) {
            excludeFilePattern = args[2];
        }
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]), excludeFolderPattern, excludeFilePattern);
        System.out.println("Failed to detect:");
        for (Path p : result.getFailedToProcess()) {
            System.out.println(p.toString());
        }
    }

}
