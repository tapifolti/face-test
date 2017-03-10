package com.tapifolti.facetest.detect;

import com.tapifolti.facetest.apicall.ApacheHttpVerifyAPICall;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.tapifolti.facetest.folder.FolderProcessor;

/**
 * Created by tapifolti on 2/24/2017.
 */
public class Verify {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 3) {
            System.out.println("com.tapifolti.facetest.detect.Detect rootFolder [excludeFolderPattern] [excludeFilePattern]");
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
        DetectFaceFolderProcessor processor = new DetectFaceFolderProcessor(excludeFolderPattern, excludeFilePattern);
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]));
        System.out.println("Failed to detect:");
        for (Path p : result.getFailedToProcess()) {
            System.out.println(p.toString());
        }
        System.out.println("Comparing Successfully processed items:");
        Map<String, List<FolderProcessor.Item>> resultMap = result.getSuccessfullyProcessed();
        // for each folder
        for ( Map.Entry<String, List<FolderProcessor.Item>> item: resultMap.entrySet()) {
            System.out.println("Comparing folder: " + item.getKey());
            processSimilars(item.getValue().toArray(new FolderProcessor.Item[item.getValue().size()]));
            item.getValue().sort((f1,f2)-> -1*Integer.compare(f1.getNotIdenticalCnt(),f2.getNotIdenticalCnt()));
            System.out.println("Worst matches:");
            for (FolderProcessor.Item f : item.getValue()) {
                System.out.println(Integer.toString(f.getNotIdenticalCnt()) + " : " +f.getFilePath());
            }
        }
    }

    private static void processSimilars(FolderProcessor.Item[] similars) {
        for (int i = 0; i < similars.length - 1; i++) {
            if (isItNotUnique(similars, similars[i], i + 1)) {
                System.out.println("It is not unique!: " + similars[i].getFilePath());
            }
        }
    }

    private static boolean isItNotUnique(FolderProcessor.Item[] succResult, FolderProcessor.Item face, int startVerify) {
        boolean same = false;
        for (int j=startVerify; j<succResult.length; j++) {
            System.out.println("Comparing: " + face.getFilePath().getFileName() + " -AND- " + succResult[j].getFilePath().getFileName());
            boolean isSame = ApacheHttpVerifyAPICall.checkIfSame(face.getId(), succResult[j].getId());
            if (!isSame) {
                face.addNotIdenticalCnt();
                succResult[j].addNotIdenticalCnt();
            }
            same = same || isSame;
            try {
                Thread.sleep(2500); // otherwise the free quota is breached (20req/min, 30K/month)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return same;
    }
}

