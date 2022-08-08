package server.network;

import shared.messages.message.Message;
import shared.messages.message.MessageStatus;

public class Analyser {
    private static Analyser analyser;

    private Analyser(){

    }

    public static Analyser getInstance(){
        if (analyser==null)
            analyser = new Analyser();
        return analyser;
    }

    public void analyse(Message message){
        if (message.getStatus()== MessageStatus.Captcha)
            Handler.getInstance().sendCaptcha(message.getAuthToken());
    }
}
