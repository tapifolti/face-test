package microsoft;

import com.tapifolti.facetest.microsoft.apicall.TrainGroupAPICall;
import org.junit.Test;

/**
 * Created by tapifolti on 3/5/2017.
 */
public class TrainGroupAPICallTest {
    @Test
    public void readResponseJson() throws Exception {
        String jsonRespErr1 = "{\"error\":{\"code\":\"BadArgument\",\"message\":\"Request body is invalid.\"}}";
        String jsonRespErr2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        TrainGroupAPICall.readResponseJson(jsonRespErr1);
        TrainGroupAPICall.readResponseJson(jsonRespErr2);

    }

}