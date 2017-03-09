package com.tapifolti.facetest.folder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class PhotoVisitor extends SimpleFileVisitor<Path> {

    public PhotoVisitor(FolderProcessor processor) {
        this.processor = processor;
    }

    private FolderProcessor processor;
    private long fileVisited = 0;
    private long randomChoosen = 0;
    private Random rand = new Random(System.currentTimeMillis());

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // exclude folder
        if (dir.getFileName().toString().matches(processor.getExcludeFolderPattern())) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        long numberOfItems = Files.list(dir).count();
        fileVisited = 0;
        randomChoosen = rand.nextInt((int)numberOfItems);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        // TODO exclude file
        if (fileVisited == 0) {
            procResult(processor.forFirstFile(file), file);
        }
        if (fileVisited == randomChoosen) {
            procResult(processor.forRandomFile(file), file);
        }
        procResult(processor.forEachFile(file), file);
        fileVisited++;
        return FileVisitResult.CONTINUE;
    }

    private void procResult(String result, Path file) {
        if (!result.isEmpty()) {
            processor.getResult().addSuccessfullyProcessed(new FolderProcessor.Item(file, result));
        } else {
            processor.getResult().addFailedToProcess(file);
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        // TODO
        processor.getResult().addFailedToProcess(file);
        return FileVisitResult.CONTINUE;
    }
}
