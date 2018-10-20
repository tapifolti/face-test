package microsoft;

import com.tapifolti.facetest.microsoft.apicall.VerifyAPICall;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 2/26/2017.
 */
public class VerifyAPICallTest {
    @Test
    public void readResponseJson() throws Exception {
        String jsonResp = "{\"isIdentical\":true,\"confidence\":0.9}";
        String jsonResp1 = "{\"error\":{\"code\":\"Unspecified\",\"message\":\"Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to createGroup and provide the right key.\"}}";
        String jsonResp2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        assertTrue(VerifyAPICall.readResponseJson(jsonResp));
        assertFalse(VerifyAPICall.readResponseJson(jsonResp1));
        assertFalse(VerifyAPICall.readResponseJson(jsonResp2));
    }

}