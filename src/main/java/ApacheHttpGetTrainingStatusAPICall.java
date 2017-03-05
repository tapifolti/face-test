import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class ApacheHttpGetTrainingStatusAPICall {

    enum TrainingStatus {notstarted, running, succeeded, failed, unspecified};
    public static TrainingStatus getTrainingStatus(String group)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/persongroups/{personGroupId}/training");

            builder.setParameter("personGroupId", group);
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);

            // Request body
            StringEntity reqEntity = new StringEntity("");
            request.setEntity(reqEntity);
            long beforeConnectTime = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(request);
            long afterConnectTime = System.currentTimeMillis();
            System.out.print("HTTP" + response.getStatusLine().getStatusCode() + ": ");
            System.out.print((afterConnectTime-beforeConnectTime) + "msec: ");
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return TrainingStatus.unspecified;
            }
            return readResponseJson(EntityUtils.toString(entity));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return TrainingStatus.unspecified;
        }
    }


    public static TrainingStatus readResponseJson(String jsonResp) {
        // {"status":"succeeded","createdDateTime": "2015-05-15T13:45:30","lastActionDateTime": null,"message": null}
        // {"error":{"code": "Unspecified", "message": "Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key."}}
        // {"error":{"statusCode": 403, "message": "Out of createGroup volume quota. Quota will be replenished in 2.12 days."}}
        JSONObject resp = null;
        try {
            resp = new JSONObject(jsonResp);
            String statusStr = resp.getString("status");
            String messageStr = resp.getString("message");
            System.out.println("Status: " + statusStr + ((messageStr != null)? (", Message: " + messageStr): ""));
            return TrainingStatus.valueOf(statusStr);
        } catch (JSONException ex) {
            try {
                JSONObject error = resp.getJSONObject("error");
                String message = error.getString("message");
                System.out.println(message);
            } catch(JSONException eex) {
                System.out.println(jsonResp);
            }
        }
        return TrainingStatus.unspecified;
    }
}
