package shared.messages.message;


import shared.gsonSerializers.LocalDateTimeDeserializer;
import shared.gsonSerializers.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Message {

    private String authToken;
    private MessageStatus status;
    private HashMap <String , Object> data;

    public Message (MessageStatus messageStatus, String authToken){
        this.status = messageStatus;
        this.authToken= authToken;
        data = new HashMap<>();
    }

    public String getAuthToken() {
        return authToken;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void addData(String dataName, Object inputData){
        this.data.put(dataName,inputData);
    }

    public Object getData(String dataName){
        return this.data.get(dataName);
    }

    public MessageStatus getStatus() {
        return status;
    }

    public static String toJson(Message message){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(message)+'\n'+"over";
    }

    public static Message fromJson(String messageString){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(messageString,Message.class);
    }
}
