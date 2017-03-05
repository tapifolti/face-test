import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 3/2/2017.
 */
public class ApacheHttpIdentifyAPICallTest {
    @Test
    public void readResponseJson() throws Exception {
        String jsonResp =  "    [\n" +
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
        String jsonResp1 = "{\"error\":{\"code\":\"Unspecified\",\"message\":\"Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key.\"}}";
        String jsonResp2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        assertTrue(ApacheHttpIdentifyAPICall.readResponseJson(jsonResp).equals("25985303-c537-4467-b41d-bdb45cd95ca1"));
        assertTrue(ApacheHttpIdentifyAPICall.readResponseJson(jsonResp1).isEmpty());
        assertTrue(ApacheHttpIdentifyAPICall.readResponseJson(jsonResp2).isEmpty());
    }

}