package ltd.maimeng.windowedvoiceroom.im;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * <pre>
 *     author : chenzimeng
 *     e-mail : 1989583040@qq.com
 *     time   : 2022/07/07
 *     desc   :
 * </pre>
 */
public class RoomSystemMessage extends RoomMessage {

    public String eventType;
    public String fromUserId;
    public String extra;

    public static SpannableString generateCommonContent(String fromUserNickname, String content) {
        String strContent = fromUserNickname + " " + content;
        int startIndex = strContent.indexOf(fromUserNickname);
        SpannableString spContent = new SpannableString(strContent);
        spContent.setSpan(new ForegroundColorSpan(Color.parseColor("#EDD250")), startIndex, startIndex + fromUserNickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spContent;
    }

    public static SpannableString generateCharacterSelectContent(String fromUserNickname, String content, String characterName) {
        String strContent = fromUserNickname + " " + content + " " + characterName;
        int startIndexContent = strContent.indexOf(fromUserNickname);
        int startIndexCharacterName = strContent.lastIndexOf(characterName);
        SpannableString spContent = new SpannableString(strContent);
        spContent.setSpan(new ForegroundColorSpan(Color.parseColor("#EDD250")), startIndexContent, startIndexContent + fromUserNickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spContent.setSpan(new ForegroundColorSpan(Color.parseColor("#EDD250")), startIndexCharacterName, startIndexCharacterName + characterName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spContent;
    }
}
