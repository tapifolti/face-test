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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class ApacheHttpDetectAPICall {
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

            System.out.print(imageData.length/1024 + "KB: ");
            long beforeConnectTime = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(request);
            long afterConnectTime = System.currentTimeMillis();
            System.out.print("HTTP" + response.getStatusLine().getStatusCode() + ": ");
            System.out.print((afterConnectTime-beforeConnectTime) + "msec: ");
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
            return getFaceIdJson(EntityUtils.toString(entity));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }


    public static String getFaceId(String jsonResp) {
        //  [{"faceId":"c5c24a82-6845-4031-9d5d-978df9175426"}]
        // "{error":{"code":"InvalidImageSize","message":"Image size is too small or too big."}}
        // {"error":{"code":"BadArgument","message":"Request body is invalid."}}
        // {"error":{"statusCode": 403,"message": "Out of call volume quota. Quota will be replenished in 2.12 days."}}
        String idPattern = "\"faceId\":\"([0-9a-f-]){36}\"";
        String errPattern = "\"error\":\\{\"(code|statusCode)\":(\"([A-Za-z]){1,100}\"|([0-9]){1,5}),\"message\":\"";
        Pattern pId = Pattern.compile(idPattern);
        Pattern pErr = Pattern.compile(errPattern);
        Matcher mId = pId.matcher(jsonResp);
        Matcher mErr = pErr.matcher(jsonResp);
        if (mId.find()) {
            int start = mId.start();
            int end = mId.end();
            String id = jsonResp.substring(start, end).substring(10, 10+36);
            System.out.println(id);
            return id;
        } else if (mErr.find()) {
            int start = mErr.end();
            int end = jsonResp.indexOf("\"", start);
            String error = jsonResp.substring(start, end);
            System.out.println(error);
        } else {
            System.out.println(jsonResp);
        }
        return "";
    }

    public static String getFaceIdJson(String jsonResp) {
        //  "[{faceId":"c5c24a82-6845-4031-9d5d-978df9175426","faceRectangle": {"width": 78,"height": 78,"left": 394,"top": 54}}]"
        // "{error":{"code":"InvalidImageSize","message":"Image size is too small or too big."}}"
        // {"error":{"code":"BadArgument","message":"Request body is invalid."}}
        // {"error":{"statusCode": 403,"message": "Out of call volume quota. Quota will be replenished in 2.12 days."}}
        try {
            JSONArray resp = new JSONArray(jsonResp);
            JSONObject item = (JSONObject)resp.get(0);
            String faceId = item.getString("faceId");
            System.out.println(faceId);
            return faceId;
        } catch (JSONException ex) {
            try {
                JSONObject resp = new JSONObject(jsonResp);
                JSONObject error = resp.getJSONObject("error");
                String message = error.getString("message");
                System.out.println(message);
            } catch(JSONException eex) {
                System.out.println(jsonResp);
            }
        }
        return "";
    }

}