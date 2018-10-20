package microsoft;

import com.tapifolti.facetest.microsoft.apicall.CreatePersonAPICall;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 3/5/2017.
 */
public class CreatePersonAPICallTest {
    @Test
    public void getPersonIdJson() throws Exception {
        String jsonResp = "{\"personId\":\"25985303-c537-4467-b41d-bdb45cd95ca1\"}";
        String jsonRespErr1 = "{\"error\":{\"code\":\"BadArgument\",\"message\":\"Request body is invalid.\"}}";
        String jsonRespErr2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        String id = CreatePersonAPICall.getPersonIdJson(jsonResp);
        assertTrue(id.equals("25985303-c537-4467-b41d-bdb45cd95ca1"));
        id = CreatePersonAPICall.getPersonIdJson(jsonRespErr1);
        assertTrue(id.isEmpty());
        id = CreatePersonAPICall.getPersonIdJson(jsonRespErr2);
        assertTrue(id.isEmpty());
    }

}