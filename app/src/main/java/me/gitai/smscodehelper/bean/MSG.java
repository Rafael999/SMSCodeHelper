package me.gitai.smscodehelper.bean;

import android.telephony.SmsMessage;

/**
 * Created by gitai on 15-01-05.
 */
public class MSG {
    private String _id;
    private String address;
    private String body;
    private String action;
    private String sender;
    private String code;

    public static MSG createFromPdu(Object p){
        if (p!=null){
            return createFromSmsMessage(SmsMessage.createFromPdu((byte[]) p));
        }
        return null;
    }

    public static MSG createFromSmsMessage(SmsMessage sms){
        if (sms.getMessageBody() == null){
            return null;
        }
        return new MSG(sms.getOriginatingAddress(), sms.getMessageBody());
    }

    public MSG() {
    }

    public MSG(String address, String body){
        setAddress(address);
        setBody(body);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MSG smsInfo = (MSG) o;

        if (!address.equals(smsInfo.address)) return false;
        return body.equals(smsInfo.body);

    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "_id='" + _id + '\'' +
                ", address='" + address + '\'' +
                ", body='" + body + '\'' +
                ", action='" + action + '\'' +
                ", sender='" + sender + '\'' +
                ", code='" + code + '\'';
    }
}
