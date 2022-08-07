package elements.messages.message;


import Savers.LocalDateTimeDeserializer;
import Savers.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Message {
    private MessageStatus status;
    private HashMap <String , Object> data;

    public Message (MessageStatus messageStatus){
        this.status = messageStatus;
    }

    public void addData(String dataName,Object inputData){
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
        return gson.toJson(message);
    }

    public static Message fromJson(String messageString){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(messageString,Message.class);
    }
}
