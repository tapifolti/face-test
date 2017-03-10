package com.tapifolti.facetest.apicall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


/**
 * Created by tapifolti on 2/22/2017.
 */
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
public class ApacheHttpAddPersonFaceAPICall {
    // returns persistedFaceId
    public static String addPersonFace(String group, String person, byte[] imageData)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/persongroups/" + group +
                    "/persons/" + person + "/persistedFaces");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", APICall.SubscriptionKey);


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


    public static String getFaceIdJson(String jsonResp) {
        //  "{"persistedFaceId": "B8D802CF-DD8F-4E61-B15C-9E6C5844CCBA"}"
        // "{error":{"code":"InvalidImageSize","message":"Image size is too small or too big."}}"
        // {"error":{"code":"BadArgument","message":"Request body is invalid."}}
        // {"error":{"statusCode": 403,"message": "Out of createGroup volume quota. Quota will be replenished in 2.12 days."}}
        JSONObject resp = null;
        try {
            resp = new JSONObject(jsonResp);
            String persistedFaceId = resp.getString("persistedFaceId");
            System.out.println(persistedFaceId);
            return persistedFaceId;
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
