package com.tapifolti.facetest.microsoft.apicall;

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


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class CreatePersonAPICall {

    static final String NAME = "NAME";
    static final String DATA = "DATA";
    static String BODY = "{\"name\":\"" + NAME + "\",\"userData\":\"" + DATA + "\"}";
    // returns personId
    public static String createPerson(String group, String personName, String data)
    {
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/persongroups/" +group + "/persons");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);

            // Request body
            StringEntity reqEntity = new StringEntity(BODY.replace(NAME, personName).replace(DATA, data));
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
            return getPersonIdJson(EntityUtils.toString(entity));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public static String getPersonIdJson(String jsonResp) {
        // {"personId":"25985303-c537-4467-b41d-bdb45cd95ca1"}
        // {"error":{"code": "Unspecified", "message": "Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key."}}
        // {"error":{"statusCode": 403, "message": "Out of createGroup volume quota. Quota will be replenished in 2.12 days."}}
        JSONObject resp = null;
        try {
            resp = new JSONObject(jsonResp);
            String personId = resp.getString("personId");
            System.out.println(personId);
            return personId;
        } catch (JSONException ex) {
            try {
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
