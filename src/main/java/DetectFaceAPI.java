/**
 * Created by tapifolti on 2/17/2017.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URI;
import java.nio.file.Paths;

/***
 * Detects face on the photo
 *  - if succ then collects them in a succ folder
 *  - if un-succ then collects them in un-succ folder
 */
public class DetectFaceAPI {


    public String detect(Path imageFile) {
        String faceID = "";

        try {
            byte[] imageData = Files.readAllBytes(imageFile);
            System.out.print(imageFile.toString() + ": ");
            faceID = ApacheHttpAPICall.call(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return faceID;
    }

}
