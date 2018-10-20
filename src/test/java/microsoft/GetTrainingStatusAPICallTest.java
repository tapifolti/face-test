package microsoft;

import com.tapifolti.facetest.microsoft.apicall.GetTrainingStatusAPICall;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tapifolti on 3/5/2017.
 */
public class GetTrainingStatusAPICallTest {
    @Test
    public void readResponseJson() throws Exception {
        String jsonResp = "{\"status\":\"succeeded\",\"createdDateTime\": \"2015-05-15T13:45:30\",\"lastActionDateTime\": null,\"message\": null}";
        String jsonRespErr1 = "{\"error\":{\"code\":\"BadArgument\",\"message\":\"Request body is invalid.\"}}";
        String jsonRespErr2 = "{\"error\":{\"statusCode\":403,\"message\":\"Out of createGroup volume quota. Quota will be replenished in 2.12 days.\"}}";
        GetTrainingStatusAPICall.TrainingStatus result = GetTrainingStatusAPICall.readResponseJson(jsonResp);
        assertTrue(result.equals(GetTrainingStatusAPICall.TrainingStatus.succeeded));
        result = GetTrainingStatusAPICall.readResponseJson(jsonRespErr1);
        assertTrue(result.equals(GetTrainingStatusAPICall.TrainingStatus.unspecified));
        result = GetTrainingStatusAPICall.readResponseJson(jsonRespErr2);
        assertTrue(result.equals(GetTrainingStatusAPICall.TrainingStatus.unspecified));

    }

}