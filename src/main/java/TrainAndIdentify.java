import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tapifolti on 3/2/2017.
 */
public class TrainAndIdentify {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 3) {
            System.out.println("Detect rootFolder [excludeFolderPattern] [excludeFilePattern]");
            return;
        }
        String excludeFolderPattern = "";
        String excludeFilePattern = "";
        if (args.length >= 2) {
            excludeFolderPattern = args[1];
        }
        if (args.length == 3) {
            excludeFilePattern = args[2];
        }
        TrainAndIdentifyFolderProcessor processor = new TrainAndIdentifyFolderProcessor(excludeFolderPattern, excludeFilePattern);
        // TODO
        // create PersonGroup
        // takes one random photo from each not excluded folder
        //  - create Person -> stores PersonId for folder
        //  - calls AddPersonFace
        // TrainPersonGroup -> GetPersonGroupTrainingStatus (duration?)
        // walks all folders and for each photo calls Face:Identify
        //  - checks if the correct Person was found
        //  - for excluded folder -> nobody
        //  - for non-excluded folder -> the Person of the folder
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]));
    }
}
