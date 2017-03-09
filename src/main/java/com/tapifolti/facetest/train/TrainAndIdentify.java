package com.tapifolti.facetest.train;

import com.tapifolti.facetest.apicall.ApacheHttpCreateGroupAPICall;
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
        String groupId = ApacheHttpCreateGroupAPICall.createGroup(group, "");
        TrainAndIdentifyFolderProcessor processor = new TrainAndIdentifyFolderProcessor(excludeFolderPattern, excludeFilePattern, groupId);
        // takes one random photo from each not excluded folder
        //  - create Person -> stores PersonId for folder
        //  - calls AddPersonFace
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]));
        // TODO
        // TrainPersonGroup -> GetPersonGroupTrainingStatus (duration?)
        // walks all (excluded ones too) folders and for each photo calls Item:Identify
        //  - checks if the correct Person was found
        //  - for excluded folder -> nobody
        //  - for non-excluded folder -> the Person of the folder
    }
}
