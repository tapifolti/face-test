package com.tapifolti.facetest.ultinous.upload;

import com.tapifolti.facetest.folder.FolderProcessor;

import java.nio.file.Paths;

public class UploadPhotos {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 3) {
            System.out.println("UploadPhotos rootFolder [excludeFolderPattern] [excludeFilePattern]");
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
        UploadPhotosFolderProcessor processor = new UploadPhotosFolderProcessor(excludeFolderPattern, excludeFilePattern);
        FolderProcessor.ProcessedResult result = processor.process(Paths.get(args[0]));
        // TODO check if succ 
    }
}

