package me.gitai.smscodehelper.utils;

import android.content.Context;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.gitai.library.utils.L;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.bean.MSG;

/**
 * Created by gitai on 16-1-3.
 */
public class Captchas {
    public static final int PARSE_TYPE_DIY = 0x0;
    public static final int PARSE_TYPE_AUTO = 0x1;

    private static String keywords = "";
    private static String ambiguities = "";
    private Context mContext;

    private MSG msg = null;

    public Captchas(MSG m, int t){
        msg = m;
    }

    public Captchas(Context ctx, MSG m, int t){
        mContext = ctx;
        msg = m;
        init();
    }

    private void init(){
        L.d("Init with prefs");
        keywords = SharedPreferencesUtil.getInstence(null)
                .getString(Constant.KEY_PARSE_KEYWORDS, mContext.getString(R.string.prefs_parse_keywords_text));
        L.d("Keywords: " + keywords);
        ambiguities = SharedPreferencesUtil.getInstence(null)
                .getString(Constant.KEY_PARSE_AMBIGUITIES, mContext.getString(R.string.prefs_parse_ambiguities_text));
        L.d("Ambiguities: " + ambiguities);
    }

    public MSG parseAuto(){
        L.d("Auto parse");
        String body = msg.getBody();
        L.d("Body: " + body);
        Stack<String> senders = new Stack();
        Stack<String> codes = new Stack();
        if (StringUtils.isChinese(body) // 关键字匹配是否为验证码
                && StringUtils.test(keywords,
                    body)){
            L.d("isChinese");

            body = body
                    .replaceAll("((http|ftp|https):?\\/\\/)?(\\w+@)?[\\w\\-_]+((\\.[\\w\\-_]+)+)([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?", "")  // 扔掉网址/邮箱
                    .replaceAll("【|\\[|\\{|<", " &") // 替换符号标记 左
                    .replaceAll("】|\\]|\\}|>", "& ") // 替换符号标记 右
                    .replaceAll("码(是|为)?(：|:)?", "码是")
                    .replaceAll("是|为", " 是 ");

            Matcher m = Pattern.compile("&?([\\u4e00-\\u9fa5]+|[A-Za-z0-9]{4,10})&?").matcher(body);
            body = "";
            while(m.find()){
                body = body + " " + m.group();
            }

            String[] parts =  body.split(",|\\.|!|！|，|。|”|“|\"|（|\\(|）|\\)|\\s"); //分割句子及辅助动词

            L.d("Parts: " + StringUtils.toString(parts));

            int pos = 1000;
            for (int i = parts.length - 1; i >= 0; i--) {
                String d = parts[i];
                if (!StringUtils.isEmpty(d)){
                    String code = StringUtils.match("^(G\\-)?[A-Za-z0-9]{4,10}$", d);
                    L.d(String.format("%s of %s", code, d));
                    if ((!StringUtils.isEmpty(code)
                            && !StringUtils.test(ambiguities, code, Pattern.CASE_INSENSITIVE))){
                        if (code.startsWith("G-")){
                            L.d("from Google and remove prefix \"G-\"");
                            code = code.replaceAll("G-","");
                        }
                        int abs = Math.abs(body.indexOf(code) - body.indexOf("是"));
                        if(abs < pos){
                            pos = abs;
                            L.d("Pos: " + pos);
                            codes.push(code);
                        }
                    }else if(StringUtils.test("^&(.+)&$", d)){
                        senders.push(d);
                    }
                }
            }
        }else if(StringUtils.test("code", body)){
            L.d("isEnglish");
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
        }else{
            L.d("Unknown");
        }
        if (!codes.isEmpty()){
            msg.setCode(codes.pop());
            L.d("Code: " + msg.getCode());
        }
        if (!senders.isEmpty()){
            msg.setSender(senders.pop().replaceAll("&",""));
            L.d("Sender: " + msg.getSender());
        }
        return msg;
    }

    public MSG parseDIY(){
        String body = msg.getBody();return msg;
    }

}
