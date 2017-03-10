package com.tapifolti.facetest.apicall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class ApacheHttpIdentifyAPICall {

    static final String FACEID = "FACEID";
    static final String GROUPID = "GROUPID";
    static String BODY = "{\"personGroupId\":\"" + GROUPID + "\",\"faceIds\":[\"" + FACEID + "\"]}";
    public static String checkIfSame(String faceId, String personGroupId) {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/identify");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);

            // Request body
            // {"personGroupId":"sample_group","faceIds":["c5c24a82-6845-4031-9d5d-978df9175426"]}
            StringEntity reqEntity = new StringEntity(BODY.replace(FACEID, faceId).replace(GROUPID, personGroupId));
            request.setEntity(reqEntity);
            long beforeConnectTime = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(request);
            long afterConnectTime = System.currentTimeMillis();
            System.out.print("HTTP" + response.getStatusLine().getStatusCode() + ": ");
            System.out.print((afterConnectTime-beforeConnectTime) + "msec: ");
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
            return readResponseJson(EntityUtils.toString(entity));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public static String readResponseJson(String jsonResp) {
        String sample = "    [\n" +
                "        {\n" +
                "            \"faceId\":\"c5c24a82-6845-4031-9d5d-978df9175426\",\n" +
                "            \"candidates\":[\n" +
                "                {\n" +
                "                    \"personId\":\"25985303-c537-4467-b41d-bdb45cd95ca1\",\n" +
                "                    \"confidence\":0.92\n" +
                "                }\n" +
                "            ]\n" +
                "        }" +
                "]\n";
        // {"error":{"code": "Unspecified", "message": "Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key."}}
        // {"error":{"statusCode": 403, "message": "Out of createGroup volume quota. Quota will be replenished in 2.12 days."}}
        try {
            JSONArray array = new JSONArray(jsonResp);
            JSONObject item = array.getJSONObject(0);
            JSONArray candidates = item.getJSONArray("candidates");
            JSONObject candidate = candidates.getJSONObject(0);
            String personId = candidate.getString("personId");
            double confidence = candidate.getDouble("confidence");

            System.out.println("personId:" + personId + " confidence:" + confidence);
            return personId;
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
