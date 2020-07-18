package android.example.blogtalk.Models;

public class Chat {

    private String message,uimg,uid;

    public Chat(){

    }

    public Chat(String uid, String message, String uimg) {
        this.uid = uid;
        this.message = message;
        this.uimg = uimg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }
}
