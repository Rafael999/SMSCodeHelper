package me.gitai.smscodehelper.utils;

import com.mokee.mms.utils.CaptchasUtils;

import me.gitai.smscodehelper.bean.SMSInfo;

/**
 * Created by gitai on 15-12-12.
 */
public class SMSCode {
    public final static int PARSE_TYPE_DIY_REGEX = 0x0;
    public final static int PARSE_TYPE_MOKEE = 0x1;
    public final static int PARSE_TYPE_V1 = 0x2;

    public static SMSInfo parse(String messageBody, String originatingAddress, int type){
        SMSInfo smsinfo = null;
        switch (type){
            case PARSE_TYPE_DIY_REGEX:
                smsinfo = new Regex(messageBody).getSMSInfo();
                break;
            case PARSE_TYPE_V1:
                smsinfo = V1.parse(messageBody);
                break;
            case PARSE_TYPE_MOKEE:
                smsinfo = findByMokee(messageBody, originatingAddress);
                break;
        }
        return smsinfo;
    }

    private static SMSInfo findByMokee(String messageBody, String originatingAddress){
        String provider = CaptchasUtils.getCaptchaProvider(messageBody, originatingAddress);
        if (!CaptchasUtils.isChineseContains(messageBody) && CaptchasUtils.isCaptchasEnMessage(messageBody)){
            return new SMSInfo(provider, CaptchasUtils.tryToGetEnCaptchas(messageBody));
        }else if(CaptchasUtils.isCaptchasMessage(messageBody)){
            return new SMSInfo(provider, CaptchasUtils.tryToGetCnCaptchas(messageBody));
        }
        return null;
    }
}
