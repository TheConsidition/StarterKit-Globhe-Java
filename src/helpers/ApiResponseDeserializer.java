package helpers;

import com.google.gson.*;
import helpers.models.ApiResponse;
import helpers.models.ErrorApiResponse;
import helpers.models.GameIdApiResponse;

import java.lang.reflect.Type;

public class ApiResponseDeserializer implements JsonDeserializer<ApiResponse> {

    public ApiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

       // System.out.println("test:" + jsonObject);
       

        ApiResponse model = null;

        if (jsonObject.isJsonNull()) {  //Check if response contains something
            model = new ErrorApiResponse();
            
        }
        

        else if (jsonObject.get("gameId") != null) {
            model = new GameIdApiResponse();
        }
     
      
        if (model != null) {
            model = context.deserialize(json, model.getClass());
        }

        return model;
    }

}
