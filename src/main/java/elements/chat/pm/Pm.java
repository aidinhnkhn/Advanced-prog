package elements.chat.pm;

import shared.util.Config;
import shared.util.ImageSender;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Pm implements Serializable {

    public static String path =
            System.getProperty("user.dir")+Config.getConfig().getProperty(String.class,"cachePath");
    private String content;

    private String username;
    private LocalDateTime date;
    private String id;
    private PmType type;

    public Pm(PmType type,String username){
        this.type = type;
        this.username = username;
        this.id = createId();
        this.date=LocalDateTime.now();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        StringBuilder pmContent = new StringBuilder();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        pmContent.append(date.format(dateTimeFormatter)+ username + ": ");
        if (type == PmType.Text) pmContent.append(content);
        else pmContent.append(type+ ": "+id);
        return pmContent.toString();
    }

    public String getContent(){
        if (type == PmType.Audio) return "audio";
        if (type == PmType.Image) return "image";
        if (type == PmType.Pdf) return "pdf";
        int min = Math.min(content.length(),10);
        return this.content.substring(0,min)+(min == content.length()?"":"...");
    }
    public PmType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getDate() {
        return date;
    }

    private String createId(){
        StringBuilder idBuilder=new StringBuilder();
        LocalDateTime localDate = LocalDateTime.now();
        idBuilder.append("P");
        idBuilder.append(localDate.getYear()%100);
        idBuilder.append(localDate.getMonthValue());
        idBuilder.append(localDate.getDayOfMonth());
        idBuilder.append(localDate.getHour());
        idBuilder.append(localDate.getMinute());
        idBuilder.append(localDate.getSecond());
        return idBuilder.toString();
    }


}
