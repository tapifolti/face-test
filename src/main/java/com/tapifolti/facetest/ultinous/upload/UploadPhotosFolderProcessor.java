package com.tapifolti.facetest.ultinous.upload;

import com.tapifolti.facetest.folder.FolderProcessor;

import java.nio.file.Path;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class UploadPhotosFolderProcessor implements FolderProcessor {

    public UploadPhotosFolderProcessor(String excludeFolderPattern, String excludeFilePattern) {
        this.excludeFolderPattern = excludeFolderPattern;
        this.excludeFilePattern = excludeFilePattern;
    }

    private String excludeFolderPattern;
    private String excludeFilePattern;

    @Override
    public String getExcludeFolderPattern() {return excludeFolderPattern;}
    public void setExcludeFolderPattern(String pattern) {excludeFolderPattern = pattern;}

    @Override
    public String getExcludeFilePattern() {
        return excludeFilePattern;
    }
    public void setExcludeFilePattern(String pattern) {excludeFilePattern = pattern;}


    @Override
    public String forEachFile(Path file)
    {
        String person = file.getParent().getFileName().toString();
        // TODO upload to storage
        return "";
    }

    @Override
    public String forFirstFile(Path file) {
        // do nothing
        String person = file.getParent().getFileName().toString();
        System.out.println("Processing folder: " + person);
        return "";
    }

    @Override
    public String forRandomFile(Path file) {
        return "";
    }

}
