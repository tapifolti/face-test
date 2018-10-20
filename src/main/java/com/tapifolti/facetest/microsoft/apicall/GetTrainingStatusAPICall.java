package com.tapifolti.facetest.microsoft.apicall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class GetTrainingStatusAPICall {

    public enum TrainingStatus {notstarted, running, succeeded, failed, unspecified};
    public static TrainingStatus getTrainingStatus(String group)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/persongroups/" + group + "/training");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);

            // Request body
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
            String messageStr = "";
            TrainingStatus ret = TrainingStatus.valueOf(statusStr);
            if (ret.equals(TrainingStatus.failed)) {
                messageStr = resp.getString("message");
            }
            System.out.println("Status: " + statusStr + ", Message: " + messageStr);
            return ret;
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
