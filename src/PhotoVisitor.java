import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by tapifolti on 2/17/2017.
 */
public class PhotoVisitor extends SimpleFileVisitor<Path> {

    public PhotoVisitor(FolderProcessor processor) {
        this.processor = processor;
    }

    private FolderProcessor processor;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // TODO
        // exclude folder
        // check if it contains files =>
        //   order by date modified
        //   call processor.forFirstFile -> result.addSuccessfullyProcessed or result.addFailedToProcess
        //   call processor.forRandomFile -> result.addSuccessfullyProcessed or result.addFailedToProcess
        // return FileVisitResult.SKIP_SUBTREE;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        // TODO
        // exclude file
        if (processor.forEachFile(file)) {
            processor.getResult().addSuccessfullyProcessed(file);
        } else {
            processor.getResult().addFailedToProcess(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        // TODO
        processor.getResult().addFailedToProcess(file);
        return FileVisitResult.CONTINUE;
    }
}
