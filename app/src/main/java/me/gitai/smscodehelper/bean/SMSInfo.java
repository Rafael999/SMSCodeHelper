package me.gitai.smscodehelper.bean;

/**
 * Created by gitai on 15-12-25.
 */
public class SMSInfo {
    public String _id="";
    public String thread_id="";
    public String smsAddress="";
    public String smsBody="";
    public String read="";
    public String action="";
    public String sender="";
    public String code="";

    public SMSInfo(){

    }

    public SMSInfo(String sender, String code){
        this.sender = sender;
        this.code = code;
    }
}
