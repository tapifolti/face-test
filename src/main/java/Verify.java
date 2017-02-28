import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by tapifolti on 2/24/2017.
 */
public class Verify {
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
        System.out.println("Comparing Successfully processed items:");
        // FolderProcessor.Face[] succResult = result.getSuccessfullyProcessed().toArray(new FolderProcessor.Face[result.getSuccessfullyProcessed().size()]);
        // processSimilars(succResult);
        // TODO result.getSuccessfullyProcessed() split to arrays of same folder
        Map<Path, List<FolderProcessor.Face>> resultMap = result.getSuccessfullyProcessed().stream()
                .collect(Collectors.groupingBy(FolderProcessor.Face::getFilePathParent));
        // for each folder call:
        for ( Map.Entry<Path, List<FolderProcessor.Face>> item: resultMap.entrySet()) {
            System.out.println("Comparing folder: " + item.getKey().toString());
            processSimilars(item.getValue().toArray(new FolderProcessor.Face[item.getValue().size()]));
        }
    }

    private static void processSimilars(FolderProcessor.Face[] similars) {
        for (int i = 0; i < similars.length - 1; i++) {
            if (isItNotUnique(similars, similars[i], i + 1)) {
                System.out.println("It is not unique!: " + similars[i].getFilePath());
            }
        }
    }

    private static boolean isItNotUnique(FolderProcessor.Face[] succResult, FolderProcessor.Face face, int startVerify) {
        boolean same = false;
        for (int j=startVerify; j<succResult.length; j++) {
            System.out.println("Comparing: " + face.getFilePath() + " AND " + succResult[j].getFilePath());
            boolean ret = ApacheHttpVerifyAPICall.call(face.getFaceId(), succResult[j].getFaceId());
            same = same || ret;
            try {
                Thread.sleep(2500); // otherwise the free quota is breached (20req/min, 30K/month)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return same;
    }
}

