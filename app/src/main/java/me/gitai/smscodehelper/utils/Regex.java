package me.gitai.smscodehelper.utils;

import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.smscodehelper.bean.SMSInfo;

/**
 * Created by gitai on 15-12-25.
 */
public class Regex {
    private static String def_keywords = "验证码 校验码 动态码 确认码 随机码 验证密码 动态密码 校验密码 随机密码 确认密码 激活码 兑换码 认证码 认证号码 认证密码 交易码 交易密码 授权码 操作码";
    private static String def_provider_regexs = "(?<=(\\[|【))(.*)(?=(\\]|】))";
    private static String def_captchas_regexs = "\\d{6}";

    private String[] keywords,provider_regexs,captchas_regexs;

    private SMSInfo smsinfo = null;

    private SharedPreferences shared = SharedPreferencesUtil.getInstence(null);

    public Regex(String messageBody) {
        if (!isCaptchasMessage(messageBody)){
            smsinfo = null;
            return;
        }
        smsinfo = new SMSInfo(getCaptchaProvider(messageBody),tryToGetCnCaptchas(messageBody));
    }

    public SMSInfo getSMSInfo(){
        return smsinfo;
    }

    private boolean isCaptchasMessage(String messageBody){
        keywords = shared.getString("parse_keywords", def_keywords).split(" ");
        for (String keyeord:keywords) {
            if(messageBody.indexOf(keyeord) != -1){
                return true;
            }
        }
        return false;
    }

    private String getCaptchaProvider(String messageBody){
        provider_regexs = shared.getString("parse_provider_regexs", def_provider_regexs).split("\n");
        for (String regexs:provider_regexs) {
            Matcher matcher = Pattern.compile(regexs).matcher(messageBody);
            if(matcher.find()){
                return matcher.group(0);
            }
        }
        return null;
    }

    private String tryToGetCnCaptchas(String messageBody){
        captchas_regexs = shared.getString("parse_captchas_regexs", def_captchas_regexs).split("\n");
        for (String regexs:captchas_regexs) {
            Matcher matcher = Pattern.compile(regexs).matcher(messageBody);
            if(matcher.find()){
                return matcher.group(0);
            }
        }
        return null;
    }
}
