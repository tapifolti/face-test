import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 2/26/2017.
 */
public class ApacheHttpVerifyAPICallTest {
    @Test
    public void readResponseJson() throws Exception {
        String jsonResp = "{\"isIdentical\":true,\"confidence\":0.9}";
        String jsonResp1 = "{\"error\":{\"code\":\"Unspecified\",\"message\":\"Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to call and provide the right key.\"}}";
        String jsonResp2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of call volume quota. Quota will be replenished in 2.12 days.\"}}";
        assertTrue(ApacheHttpVerifyAPICall.readResponseJson(jsonResp));
        assertFalse(ApacheHttpVerifyAPICall.readResponseJson(jsonResp1));
        assertFalse(ApacheHttpVerifyAPICall.readResponseJson(jsonResp2));
    }

    @Test
    public void readResponse() throws Exception {
        String jsonResp = "{\"isIdentical\":true,\"confidence\":0.9}";
        String jsonResp1 = "{\"error\":{\"code\":\"Unspecified\",\"message\":\"Access denied due to invalid subscription key. Make sure you are subscribed to an API you are trying to call and provide the right key.\"}}";
        String jsonResp2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of call volume quota. Quota will be replenished in 2.12 days.\"}}";
        assertTrue(ApacheHttpVerifyAPICall.readResponse(jsonResp));
        assertFalse(ApacheHttpVerifyAPICall.readResponse(jsonResp1));
        assertFalse(ApacheHttpVerifyAPICall.readResponse(jsonResp2));
    }

}