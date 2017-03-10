package com.tapifolti.facetest.folder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tapifolti on 2/17/2017.
 */

/***
 * walks all one-level sub-folders looking for files to process
 * omits sub-folders which match to the exclude pattern
 */
public interface FolderProcessor {

    public static class Item {

        public Item(Path filePath, String id) {
            this.filePath = filePath;
            this.id = id;
        }

        private Path filePath;
        public Path getFilePath() {
            return filePath;
        }

        private String id;
        public String getId() {
            return id;
        }

        private int notIdenticalCnt = 0;
        public int getNotIdenticalCnt() {
            return notIdenticalCnt;
        }
        public void addNotIdenticalCnt() {
            notIdenticalCnt++;
        }


    }
    public static class ProcessedResult {
        private Map<String, List<Item>> successfullyProcessed = new HashMap<>();
        private Map<String, String> idToPerson = new HashMap<>();
        public Map<String, List<Item>> getSuccessfullyProcessed() {
            return successfullyProcessed;
        }

        public void addSuccessfullyProcessed(Item face) {
            String person = face.getFilePath().getParent().getFileName().toString();
            List<Item> list = null;
            if (successfullyProcessed.containsKey(person)) {
                list = successfullyProcessed.get(person);
            } else {
                list = new ArrayList<>();
                successfullyProcessed.put(person, list);
            }
            list.add(face);
            idToPerson.put(face.getId(), person);
        }

        public List<Item> getItemsForPerson(String person) {
            return successfullyProcessed.get(person);
        }

        public String getPersonById(String id) {
            return idToPerson.get(id);
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

    ProcessedResult result = new ProcessedResult();
    public default ProcessedResult getResult() {
        return result;
    }

    /***
     * createGroup it for each file in each subfolder
     * @param file
     * @return
     */
    public String forEachFile(Path file);

    /***
     * createGroup it for the first file (ordered by 'date modified') in each sub-folder
     * @param file
     * @return
     */
    public String forFirstFile(Path file);

    /***
     * createGroup it on a randomly chosen file
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
     */
    public default ProcessedResult process(Path rootFolder) {
        try {
            Files.walkFileTree(rootFolder, new PhotoVisitor(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
