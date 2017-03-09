import com.tapifolti.facetest.apicall.ApacheHttpAddPersonFaceAPICall;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 3/5/2017.
 */
public class ApacheHttpAddPersonFaceAPICallTest {
    @Test
    public void getFaceIdJson() throws Exception {
        String jsonResp = "{\"persistedFaceId\": \"B8D802CF-DD8F-4E61-B15C-9E6C5844CCBA\"}";
        String jsonRespErr1 = "{\"error\":{\"code\":\"BadArgument\",\"message\":\"Request body is invalid.\"}}";
        String jsonRespErr2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        String id = ApacheHttpAddPersonFaceAPICall.getFaceIdJson(jsonResp);
        assertTrue(id.equals("B8D802CF-DD8F-4E61-B15C-9E6C5844CCBA"));
        id = ApacheHttpAddPersonFaceAPICall.getFaceIdJson(jsonRespErr1);
        assertTrue(id.isEmpty());
        id = ApacheHttpAddPersonFaceAPICall.getFaceIdJson(jsonRespErr2);
        assertTrue(id.isEmpty());

    }

}