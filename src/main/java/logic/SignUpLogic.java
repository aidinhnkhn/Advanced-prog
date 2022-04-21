package logic;

import javafx.scene.image.Image;

public class SignUpLogic {
    private static SignUpLogic signUpLogic;

    private SignUpLogic(){

    }

    public static SignUpLogic getInstance(){
        if (signUpLogic==null){
            signUpLogic=new SignUpLogic();
        }
        return signUpLogic;
    }
    public boolean SignUp(String username, String password, String confirmPass,
                          String melicode, String phoneNumber, String email, String profession,
                          String department, String degree, Image image){
        if (!allChecked(username,password,confirmPass,melicode,phoneNumber,email)) return false;
        return true;
    }
    public boolean allChecked(String username, String password, String confirmPass,
                              String melicode, String phoneNumber, String email){
        if (username.equals("")) return false;
        if (password.equals("")) return false;
        if (!password.equals(confirmPass)) return false;
        if (melicode.equals("")) return false;
        if (phoneNumber.equals("")) return false;
        if (email.equals("")) return false;
        return true;
    }
}
