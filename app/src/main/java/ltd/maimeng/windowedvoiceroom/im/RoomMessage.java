package ltd.maimeng.windowedvoiceroom.im;

import ltd.maimeng.core.ui.list.ListBean;

/**
 * <pre>
 *     author : chenzimeng
 *     e-mail : 1989583040@qq.com
 *     time   : 2022/07/07
 *     desc   :
 * </pre>
 */
public class RoomMessage extends ListBean {

    /**
     * 系统消息（如：派对守则）
     */
    public static final int TYPE_SYSTEM = 0;
    /**
     * 文本消息
     */
    public static final int TYPE_TXT = 1;
    /**
     * 用户行为-进入房间
     */
    public static final int TYPE_USER_JOIN = 2;
    /**
     * 房主收到的观众申请上麦消息
     */
    public static final int TYPE_AUDIENCE_APPLY_TO_ANCHOR = 3;
    /**
     * 房主收到的观众主动申请上麦消息
     */
    public static final int TYPE_AUDIENCE_AUTO_APPLY_TO_ANCHOR = 4;

    public long messageId;

    public String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomMessage that = (RoomMessage) o;
        return messageId == that.messageId;
    }
}
