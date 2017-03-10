package com.tapifolti.facetest.train;

import com.tapifolti.facetest.apicall.ApacheHttpCreateGroupAPICall;
import com.tapifolti.facetest.apicall.ApacheHttpGetTrainingStatusAPICall;
import com.tapifolti.facetest.apicall.ApacheHttpTrainGroupAPICall;
import com.tapifolti.facetest.folder.FolderProcessor;
import org.apache.http.HttpResponse;

import java.nio.file.Paths;

import static com.tapifolti.facetest.apicall.ApacheHttpGetTrainingStatusAPICall.TrainingStatus.running;
import static com.tapifolti.facetest.apicall.ApacheHttpGetTrainingStatusAPICall.TrainingStatus.succeeded;

/**
 * Created by tapifolti on 3/2/2017.
 */
public class TrainAndIdentify {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 3) {
            System.out.println("TrainAndIdentify rootFolder [excludeFolderPattern] [excludeFilePattern]");
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
        // create PersonGroup
        String group = "allfriends";
        System.out.print("Group: " + group + ": ");
        String groupId = ApacheHttpCreateGroupAPICall.createGroup(group, "");
        TrainAndIdentifyFolderProcessor processor = new TrainAndIdentifyFolderProcessor(excludeFolderPattern, excludeFilePattern, groupId);
        // takes one random photo from each not excluded folder
        //  - create Person -> stores PersonId for folder
        //  - calls AddPersonFace
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]));
        // TrainPersonGroup -> GetPersonGroupTrainingStatus (duration?)
        System.out.print("Start Training for Group: " + groupId + ": ");
        long beforeConnectTime = System.currentTimeMillis();
        boolean succ= ApacheHttpTrainGroupAPICall.trainGroup(groupId);
        if (!succ) {
            return;
        }
        boolean pending = true;
        while (pending) {
            System.out.print("Get Training Satus ");
            ApacheHttpGetTrainingStatusAPICall.TrainingStatus status = ApacheHttpGetTrainingStatusAPICall.getTrainingStatus(groupId);
            switch (status) {
                case notstarted:
                case succeeded:
                case failed:
                case unspecified:
                    pending = false;
                    break;
                case running:
                    break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        long afterConnectTime = System.currentTimeMillis();
        System.out.println("Training took for: " + (afterConnectTime-beforeConnectTime) + "msec");
        processor.setPhase(TrainAndIdentifyFolderProcessor.Phases.IDENTIFY);
        processor.setExcludeFolderPattern("");
        processor.setExcludeFilePattern("");
        // walks all (excluded ones too) folders and for each photo calls Item:Identify
        //  - checks if the correct Person was found
        processor.process(Paths.get(args[0]));
    }
}
