package elements.messages.response;


import Savers.LocalDateTimeDeserializer;
import Savers.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Response {
    private ResponseStatus status;
    private HashMap<String, Object> data;
    private String errorMessage;
    // Another field for messages from server to client

    public Response() {}

    public Response(ResponseStatus status) {
        this.status = status;
        data = new HashMap<>();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void addData(String dataName, Object data) {
        this.data.put(dataName, data);
    }

    public Object getData(String dataName) {
        return this.data.get(dataName);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static String toJson(Response response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(response);
    }

    public static Response fromJson(String responseJson){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(responseJson,Response.class);
    }

}
