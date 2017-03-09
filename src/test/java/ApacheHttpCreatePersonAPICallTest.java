import com.tapifolti.facetest.apicall.ApacheHttpCreatePersonAPICall;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 3/5/2017.
 */
public class ApacheHttpCreatePersonAPICallTest {
    @Test
    public void getPersonIdJson() throws Exception {
        String jsonResp = "{\"personId\":\"25985303-c537-4467-b41d-bdb45cd95ca1\"}";
        String jsonRespErr1 = "{\"error\":{\"code\":\"BadArgument\",\"message\":\"Request body is invalid.\"}}";
        String jsonRespErr2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        String id = ApacheHttpCreatePersonAPICall.getPersonIdJson(jsonResp);
        assertTrue(id.equals("25985303-c537-4467-b41d-bdb45cd95ca1"));
        id = ApacheHttpCreatePersonAPICall.getPersonIdJson(jsonRespErr1);
        assertTrue(id.isEmpty());
        id = ApacheHttpCreatePersonAPICall.getPersonIdJson(jsonRespErr2);
        assertTrue(id.isEmpty());
    }

}