package ltd.maimeng.windowedvoiceroom.im;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import ltd.maimeng.core.ui.list.RecyclerViewHolder;
import ltd.maimeng.windowedvoiceroom.R;

/**
 * 语音房-系统消息
 */
public class RoomSystemMsgHolder extends RecyclerViewHolder {

    public static int LAYOUT_ID = R.layout.holder_room_message_system;

    private Activity activity;
    private TextView tvContent;
    private RoomSystemMessage roomSystemMessage;

    public RoomSystemMsgHolder(Activity activity, @NonNull View itemView) {
        super(itemView);
        this.activity = activity;
    }

    @Override
    public void findViews() {
        tvContent = itemView.findViewById(R.id.tv_holder_room_message_system);
    }

    @Override
    public void setData(Object object) {
        roomSystemMessage = (RoomSystemMessage) object;
        tvContent.setText(roomSystemMessage.content);
    }
}
