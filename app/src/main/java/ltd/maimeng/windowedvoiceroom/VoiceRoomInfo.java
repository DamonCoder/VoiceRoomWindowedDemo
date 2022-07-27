package ltd.maimeng.windowedvoiceroom;

import java.io.Serializable;

import ltd.maimeng.core.ui.list.ListBean;

public class VoiceRoomInfo extends ListBean implements Serializable {

    public static final int TYPE_SINGLE_INSTANCE = 1;
    public static final int TYPE_STANDARD = 2;
    private int type;

    public static final String PARAM_ROOM_INFO = "param_room_info";

    public VoiceRoomInfo(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
