package com.tapifolti.facetest.train;

import com.tapifolti.facetest.apicall.ApacheHttpAddPersonFaceAPICall;
import com.tapifolti.facetest.apicall.ApacheHttpCreatePersonAPICall;
import com.tapifolti.facetest.apicall.ApacheHttpIdentifyAPICall;
import com.tapifolti.facetest.detect.DetectFaceAPI;
import com.tapifolti.facetest.folder.FolderProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class TrainAndIdentifyFolderProcessor implements FolderProcessor {

    public TrainAndIdentifyFolderProcessor(String excludeFolderPattern, String excludeFilePattern, String groupId) {
        this.excludeFolderPattern = excludeFolderPattern;
        this.excludeFilePattern = excludeFilePattern;
        this.groupId = groupId;
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
    private String groupId;
    @Override
    public String getExcludeFolderPattern() {return excludeFolderPattern;}
    public void setExcludeFolderPattern(String pattern) {excludeFolderPattern = pattern;}

    @Override
    public String getExcludeFilePattern() {
        return excludeFilePattern;
    }
    public void setExcludeFilePattern(String pattern) {excludeFilePattern = pattern;}

    private void checkIfFound(String person, String personID) {
        //  checks if the correct Person was found
        if (personID == null || personID.isEmpty()) {
            //  nobody was identified => ok if not trained folder
            List<Item> trained = getResult().getItemsForPerson(person);
            if (trained == null) {
                System.out.println("OK: Successfully Unidentified!");
            } else {
                System.out.println("NOT_OK: Failed to identify! person: '" + person);
            }
            return;
        }
        // if the correct person was identified
        String identifiedPerson = getResult().getPersonById(personID);
        if (identifiedPerson != null) {
            if (identifiedPerson.equals(person)) {
                System.out.println("OK: Successfully identified!");
            } else {
                System.out.println("NOT_OK: Unsuccessfully identified! person: '" + person + "' but identified: '" + identifiedPerson + "'");
            }
        } else {
            System.out.println("ERROR: Unknown personId!");
        }
    }

    @Override
    public String forEachFile(Path file)
    {
        if (getPhase().equals(Phases.IDENTIFY)) {
            String person = file.getParent().getFileName().toString();
            // calls detect for the photo
            String faceId = DetectFaceAPI.detect(file);
            // calls Identify
            System.out.print("Identify: ");
            String personID = ApacheHttpIdentifyAPICall.checkIfSame(faceId, groupId);
            checkIfFound(person, personID);
        }
        return "";
    }

    @Override
    public String forFirstFile(Path file) {
        // do nothing
        if (getPhase().equals(Phases.IDENTIFY)) {
            String person = file.getParent().getFileName().toString();
            System.out.println("Processing folder: " + person);
        }
        return "";
    }

    @Override
    public String forRandomFile(Path file) {
        if (getPhase().equals(Phases.SETUP_PERSON)) {
            //  - create Person -> stores PersonId for folder
            String person = file.getParent().getFileName().toString();
            System.out.print("Person: " + person + ": ");
            String personId = ApacheHttpCreatePersonAPICall.createPerson(groupId, person, "");
            //  - calls AddPersonFace
            try {
                System.out.print(file.toString() + ": ");
                byte[] imageData = Files.readAllBytes(file);
                String persistedFaceId = ApacheHttpAddPersonFaceAPICall.addPersonFace(groupId, personId, imageData);
                return personId;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return "";
    }

}
