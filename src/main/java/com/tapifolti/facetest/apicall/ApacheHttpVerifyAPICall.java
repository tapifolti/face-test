package com.tapifolti.facetest.apicall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
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
public class ApacheHttpVerifyAPICall {

    static final String FACEID1 = "FACEID1";
    static final String FACEID2 = "FACEID2";
    static String BODY = "{\"faceId1\":\"" + FACEID1 + "\",\"faceId2\":\"" + FACEID2 +"\"}";
    public static boolean checkIfSame(String faceId1, String faceId2)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/verify");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);

            // Request body
            // {"faceId1":"c5c24a82-6845-4031-9d5d-978df9175426","faceId2":"c5c24a82-6845-4031-9d5d-978df9020202"}
            StringEntity reqEntity = new StringEntity(BODY.replace(FACEID1, faceId1).replace(FACEID2, faceId2));
            request.setEntity(reqEntity);
            long beforeConnectTime = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(request);
            long afterConnectTime = System.currentTimeMillis();
            System.out.print("HTTP" + response.getStatusLine().getStatusCode() + ": ");
            System.out.print((afterConnectTime-beforeConnectTime) + "msec: ");
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return false;
            }
            return readResponseJson(EntityUtils.toString(entity));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean readResponseJson(String jsonResp) {
        // {"isIdentical":true,"confidence":0.9}
        // {"error":{"code": "Unspecified", "message": "Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key."}}
        // {"error":{"statusCode": 403, "message": "Out of createGroup volume quota. Quota will be replenished in 2.12 days."}}
        JSONObject resp = new JSONObject(jsonResp);
        try {
            boolean isIdentical = resp.getBoolean("isIdentical");
            double confidence = resp.getDouble("confidence");
            if (!isIdentical) {
                System.out.println("notIdentical - confidence: " + confidence);
            } else {
                if (confidence < 0.5) {
                    System.out.println("mayBeIdentical - confidence: " + confidence);
                } else {
                    System.out.println("isIdentical - confidence: " + confidence);
                    return true;
                }
            }
        } catch (JSONException ex) {
            try {
                JSONObject error = resp.getJSONObject("error");
                String message = error.getString("message");
                System.out.println(message);
            } catch(JSONException eex) {
                System.out.println(jsonResp);
            }
        }
        return false;
    }
}
