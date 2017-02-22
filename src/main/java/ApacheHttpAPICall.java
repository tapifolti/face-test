import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class ApacheHttpAPICall {
    public static String call(byte[] imageData)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/detect");

            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            // builder.setParameter("returnFaceAttributes", "{string}");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", "a605d381618e4f108f376f8a9503bf09");


            // Request body
            //StringEntity reqEntity = new StringEntity("{body}");
            ByteArrayEntity reqBinary = new ByteArrayEntity(imageData);
            request.setEntity(reqBinary);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
//            InputStream content = entity.getContent();
//            if (content == null) {
//                return "";
//            }
//            String result = new BufferedReader(new InputStreamReader(content))
//                    .lines().collect(Collectors.joining("\n"));
            String jsonResp = EntityUtils.toString(entity);
            System.out.println(jsonResp);
            return getFaceId(jsonResp);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
    private static String getFaceId(String jsonResp) {
        //  "faceId": "c5c24a82-6845-4031-9d5d-978df9175426"
        String idPattern = "\"faceId\": \"([0-9a-f-]){36}\"";
        Pattern p = Pattern.compile(idPattern);
        Matcher m = p.matcher(jsonResp);
        if (m.find()) {
            int start = m.start();
            int end = m.end();
            return jsonResp.substring(start, end).substring(12, 12+36);
        }
        return "";
    }
}
