import com.tapifolti.facetest.apicall.ApacheHttpDetectAPICall;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 2/23/2017.
 */
public class ApacheHttpDetectAPICallTest {
    @Test
    public void getFaceIdJson() throws Exception {
        String jsonResp = "[{\"faceId\":\"c5c24a82-6845-4031-9d5d-978df9175426\",\"faceRectangle\":{\"width\": 78,\"height\": 78,\"left\": 394,\"top\": 54}}]";
        String jsonRespErr1 = "{\"error\":{\"code\":\"BadArgument\",\"message\":\"Request body is invalid.\"}}";
        String jsonRespErr2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        String id = ApacheHttpDetectAPICall.getFaceIdJson(jsonResp);
        assertTrue(id.equals("c5c24a82-6845-4031-9d5d-978df9175426"));
        id = ApacheHttpDetectAPICall.getFaceIdJson(jsonRespErr1);
        assertTrue(id.isEmpty());
        id = ApacheHttpDetectAPICall.getFaceIdJson(jsonRespErr2);
        assertTrue(id.isEmpty());
    }


}