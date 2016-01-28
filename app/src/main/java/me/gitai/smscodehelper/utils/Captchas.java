package me.gitai.smscodehelper.utils;

import java.util.Stack;
import java.util.regex.Pattern;

import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.bean.MSG;

/**
 * Created by gitai on 16-1-3.
 */
public class Captchas {
    public static final int PARSE_TYPE_DIY = 0x0;
    public static final int PARSE_TYPE_AUTO = 0x1;

    private static String keywords = "验证码|校验码|动态码|确认码|随机码|验证|校验|验证密码|动态密码|校验密码|随机密码|确认密码|激活码|兑换码|认证码|认证号码|认证密码|交易码|交易密码|授权码|操作码|密码|提取码";
    private static String ambiguities = "google|facebook";

    private MSG msg = null;

    public Captchas(MSG m, int t){
        msg = m;
        init();
    }

    private void init(){
        keywords = SharedPreferencesUtil.getInstence(null)
                .getString(Constant.KEY_PARSE_KEYWORDS, keywords);
        ambiguities = SharedPreferencesUtil.getInstence(null)
                .getString(Constant.KEY_PARSE_AMBIGUITIES, ambiguities);
    }

    public MSG getMsg() {
        return msg;
    }

    public MSG parseAuto(){
        String body = msg.getBody();
        Stack<String> senders = new Stack();
        Stack<String> codes = new Stack();
        if (StringUtils.isChinese(body) // 关键字匹配是否为验证码
                && StringUtils.test(keywords,
                    body)){

            body = body
                    .replaceAll("((http|ftp|https):?\\/\\/)?(\\w+@)?[\\w\\-_]+((\\.[\\w\\-_]+)+)([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?", "")  // 扔掉网址/邮箱
                    .replaceAll("【|\\[|\\{|<", " &") // 替换符号标记 左
                    .replaceAll("】|\\]|\\}|>", "& ") // 替换符号标记 右
                    .replaceAll("码(是|为)?(：|:)?", "码是")
                    .replaceAll("是|为", " 是 ");

            String[] parts =  body.split(",|\\.|!|！|，|。|”|“|\"|（|\\(|）|\\)|\\s"); //分割句子及辅助动词

            int pos = 1000;
            for (int i = parts.length - 1; i >= 0; i--) {
                String d = parts[i];
                if (!StringUtils.isEmpty(d)){
                    String code = StringUtils.match("^(G\\-)?[A-Za-z0-9]{4,10}$", d);
                    if ((!StringUtils.isEmpty(code)
                            && !StringUtils.test(ambiguities, code, Pattern.CASE_INSENSITIVE))){
                        if (code.startsWith("G-")){
                            code = code.replaceAll("G-","");
                        }
                        int abs = Math.abs(body.indexOf(code) - body.indexOf("是"));
                        if(abs < pos){
                            pos = abs;
                            codes.push(code);
                        }
                    }else if(StringUtils.test("^&(.+)&$", d)){
                        senders.push(d);
                    }
                }
            }
        }else if(StringUtils.test("code", body)){
            String[] parts = body
                    .replaceAll("\\[|\\{|\\(|<", " &") // 替换符号标记 左
                    .replaceAll("\\]|\\}|\\)|>", "& ") // 替换符号标记 右
                    .split("\\s");

            for (int i = parts.length - 1; i > 0; i--) {
                if (parts[i - 1].equals("is")){
                    codes.push(StringUtils.match("^[A-Za-z0-9]{4,10}$", parts[i]));
                }else if(StringUtils.test("^&(.+)&$", parts[i])){
                    senders.push(parts[i]);
                }
            }
        }
        if (!senders.isEmpty()){
            msg.setSender(senders.pop().replaceAll("&",""));
        }
        if (!codes.isEmpty()){
            msg.setCode(codes.pop());
        }else{
            msg.setCode(msg.getSender());
            msg.setSender(null);
        }
        return msg;
    }

    public MSG parseDIY(){
        String body = msg.getBody();return msg;
    }

}
