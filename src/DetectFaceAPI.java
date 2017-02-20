/**
 * Created by tapifolti on 2/17/2017.
 */

import java.nio.file.Path;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/***
 * Detects face on the photo
 *  - if succ then collects them in a succ folder
 *  - if un-succ then collects them in un-succ folder
 */
public class DetectFaceAPI {


    public String detect(Path imageFile) {
        String faceID = null;

        // TODO call CS API
        return faceID;
        // failed:
        // return null
    }

    private void sample() {
// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)

        public class JavaSample
        {
            public static void main(String[] args)
            {
                HttpClient httpclient = HttpClients.createDefault();

                try
                {
                    URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/detect");

                    builder.setParameter("returnFaceId", "true");
                    builder.setParameter("returnFaceLandmarks", "false");
                    builder.setParameter("returnFaceAttributes", "{string}");

                    URI uri = builder.build();
                    HttpPost request = new HttpPost(uri);
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Ocp-Apim-Subscription-Key", "{subscription key}");


                    // Request body
                    StringEntity reqEntity = new StringEntity("{body}");
                    request.setEntity(reqEntity);

                    HttpResponse response = httpclient.execute(request);
                    HttpEntity entity = response.getEntity();

                    if (entity != null)
                    {
                        System.out.println(EntityUtils.toString(entity));
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

}
