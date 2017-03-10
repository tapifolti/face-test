package com.tapifolti.facetest.detect; /**
 * Created by tapifolti on 2/17/2017.
 */

import com.tapifolti.facetest.apicall.ApacheHttpDetectAPICall;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/***
 * Detects face on the photo
 *  - if succ then collects them in a succ folder
 *  - if un-succ then collects them in un-succ folder
 */
public class DetectFaceAPI {


    public static String detect(Path imageFile) {
        String faceID = "";

        try {
            byte[] imageData = Files.readAllBytes(imageFile);
            System.out.print(imageFile.toString() + ": ");
            faceID = ApacheHttpDetectAPICall.detectFace(imageData);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return faceID;
    }

}
