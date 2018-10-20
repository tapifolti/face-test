package com.tapifolti.facetest.microsoft.train;

import com.tapifolti.facetest.microsoft.apicall.CreateGroupAPICall;
import com.tapifolti.facetest.microsoft.apicall.CreatePersonAPICall;
import com.tapifolti.facetest.microsoft.apicall.GetTrainingStatusAPICall;
import com.tapifolti.facetest.microsoft.apicall.TrainGroupAPICall;
import com.tapifolti.facetest.folder.FolderProcessor;

import java.nio.file.Paths;

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
        String groupId = CreateGroupAPICall.createGroup(group, "");
        TrainAndIdentifyFolderProcessor processor = new TrainAndIdentifyFolderProcessor(excludeFolderPattern, excludeFilePattern, groupId);
        // takes one random photo from each not excluded folder
        //  - create Person -> stores PersonId for folder
        //  - calls AddPersonFace
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]));
        // Add empty Persons for test
        for (int i=0; i<5; i++) {
            String person = "testperson_" + CreateGroupAPICall.rand.nextInt(100000);
            System.out.print("Person: " + person + ": ");
            String personId = CreatePersonAPICall.createPerson(groupId, person, "");
        }
        // TrainPersonGroup -> GetPersonGroupTrainingStatus (duration?)
        try { // rate limit
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Start Training for Group: " + groupId + ": ");
        long beforeConnectTime = System.currentTimeMillis();
        boolean succ= TrainGroupAPICall.trainGroup(groupId);
        if (!succ) {
            return;
        }
        boolean pending = true;
        while (pending) {
            System.out.print("Get Training Satus ");
            GetTrainingStatusAPICall.TrainingStatus status = GetTrainingStatusAPICall.getTrainingStatus(groupId);
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
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        long afterConnectTime = System.currentTimeMillis();
        System.out.println("Training took for: " + (afterConnectTime-beforeConnectTime) + "msec");
        try { // rate limit
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processor.setPhase(TrainAndIdentifyFolderProcessor.Phases.IDENTIFY);
        processor.setExcludeFolderPattern("");
        processor.setExcludeFilePattern("");
        // walks all (excluded ones too) folders and for each photo calls Item:Identify
        //  - checks if the correct Person was found
        processor.process(Paths.get(args[0]));
    }
}
