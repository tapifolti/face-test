package com.tapifolti.facetest.apicall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Random;


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class ApacheHttpCreateGroupAPICall {

    static final String NAME = "NAME";
    static final String DATA = "DATA";
    static String BODY = "{\"name\":\"" + NAME + "\",\"userData\":\"" + DATA + "\"}";
    private static Random rand = new Random(System.currentTimeMillis());
    // returns groupID
    public static String createGroup(String name, String data)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            // generate: numbers, English letters in lower case, '-' and '_'. The maximum length of the personGroupId is 64
            String groupId = "persongroup_" + rand.nextInt(100000);
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/persongroups/" + groupId );

            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);

            // Request body
            StringEntity reqEntity = new StringEntity(BODY.replace(NAME, name).replace(DATA, data));
            request.setEntity(reqEntity);
            long beforeConnectTime = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(request);
            long afterConnectTime = System.currentTimeMillis();
            System.out.print("HTTP" + response.getStatusLine().getStatusCode() + ": ");
            System.out.print((afterConnectTime-beforeConnectTime) + "msec: ");
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("'" + groupId + "' group successfully created");
                return groupId;
            } else if (entity != null) {
                readResponseJson(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static void readResponseJson(String jsonResp) {
        // {"error":{"code": "Unspecified", "message": "Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key."}}
        // {"error":{"statusCode": 403, "message": "Out of createGroup volume quota. Quota will be replenished in 2.12 days."}}
        try {
            JSONObject resp = new JSONObject(jsonResp);
            JSONObject error = resp.getJSONObject("error");
            String message = error.getString("message");
            System.out.println(message);
        } catch(JSONException eex) {
            System.out.println(jsonResp);
        }
    }
}
