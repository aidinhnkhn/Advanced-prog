package server.network;

import server.Server;
import shared.messages.response.Response;
import shared.messages.response.ResponseStatus;
import shared.util.ImageSender;

import java.util.Random;

public class Handler {

    private static Handler handler;

    private Handler(){

    }

    public static Handler getInstance(){
        if (handler==null)
            handler = new Handler();
        return handler;
    }
    public void sendCaptcha(String authToken){
        String captchaName = handler.getCaptchaName();
        String path = System.getProperty("user.dir") +
                "\\src\\main\\resources\\eData\\Captcha\\" + captchaName;
        String encodedImage = ImageSender.encode(path);
        Response response = new Response(ResponseStatus.Captcha);
        response.addData("image",encodedImage);
        response.addData("number",captchaName);
        Server.getServer().sendMessageToClient(authToken,Response.toJson(response));
    }

    private String getCaptchaName() {
        Random random = new Random();
        int captchaNumber = random.nextInt(5);
        if (captchaNumber == 0)
            return "5720.png";
        else if (captchaNumber == 1)
            return "6280.png";
        else if (captchaNumber == 2)
            return "8217.png";
        else if (captchaNumber == 3)
            return "8387.png";
        else
            return "8612.png";
    }
}
