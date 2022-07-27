package ltd.maimeng.windowedvoiceroom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ltd.maimeng.core.ui.list.RecyclerAdapter;
import ltd.maimeng.core.ui.list.VerticalItemDecoration;
import ltd.maimeng.windowedvoiceroom.im.RoomMessage;
import ltd.maimeng.windowedvoiceroom.im.RoomSystemMessage;
import ltd.maimeng.windowedvoiceroom.im.RoomSystemMsgHolder;
import ltd.maimeng.windowedvoiceroom.im.VoiceRoomMessageListCache;
import ltd.maimeng.windowedvoiceroom.service.VoiceRoomService;

public class VoiceRoomStandardActivity extends AppBaseActivity implements View.OnClickListener {

    private VoiceRoomInfo roomInfo;

    private RecyclerView rvVoiceRoomMessage;
    private RecyclerAdapter voiceRoomMessageAdapter;
    private VoiceRoomMessageListCache voiceRoomMessageListCache;

    @Override
    public View setContentView() {
        return View.inflate(VoiceRoomStandardActivity.this, R.layout.activity_voice_room, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roomInfo = (VoiceRoomInfo) getIntent().getSerializableExtra(VoiceRoomInfo.PARAM_ROOM_INFO);

        TextView tvVoiceRoomName = findViewById(R.id.tv_voice_room_name);
        ImageView ivVoiceRoomQuit = findViewById(R.id.iv_voice_room_quit);
        ImageView ivVoiceRoomWindowed = findViewById(R.id.iv_voice_room_windowed);

        tvVoiceRoomName.setText("StandardVoiceRoom");
        ivVoiceRoomQuit.setOnClickListener(this);
        ivVoiceRoomWindowed.setOnClickListener(this);

        rvVoiceRoomMessage = findViewById(R.id.rv_voice_room_message);
        LinearLayoutManager voiceRoomMessageLayoutManager = new LinearLayoutManager(VoiceRoomStandardActivity.this);
        voiceRoomMessageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVoiceRoomMessage.setLayoutManager(voiceRoomMessageLayoutManager);
        rvVoiceRoomMessage.addItemDecoration(new VerticalItemDecoration(DensityUtil.dp2px(VoiceRoomStandardActivity.this, 10), DensityUtil.dp2px(VoiceRoomStandardActivity.this, 10)));
        voiceRoomMessageListCache = new VoiceRoomMessageListCache();
        voiceRoomMessageAdapter = new RecyclerAdapter(VoiceRoomStandardActivity.this)
                .bindData(voiceRoomMessageListCache)
                .setViewHolder(RoomMessage.TYPE_SYSTEM, RoomSystemMsgHolder.class);
        rvVoiceRoomMessage.setAdapter(voiceRoomMessageAdapter);

        if (VoiceRoomService.inService) {
            voiceRoomMessageListCache.reset();
            voiceRoomMessageListCache.addData(VoiceRoomService.roomMessages);
            voiceRoomMessageAdapter.notifyDataSetChanged();
            voiceRoomMessageListScrollToBottom();

            addVoiceRoomRolesSystemMessage("这种方案下，通过悬浮窗回到房间，Activity重新创建，恢复聊天历史记录。");
        } else {
            Intent intent = new Intent(VoiceRoomStandardActivity.this, VoiceRoomService.class);
            intent.putExtra(VoiceRoomInfo.PARAM_ROOM_INFO, roomInfo);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }

            addVoiceRoomRolesSystemMessage("创建房间");
            addVoiceRoomRolesSystemMessage("点击右上角退出房间，房间关闭。");
            addVoiceRoomRolesSystemMessage("点击右上角收起房间，房间收起成悬浮窗，点击悬浮窗回到房间。");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_voice_room_quit) {
            if (VoiceRoomService.inService) {
                stopService(new Intent(VoiceRoomStandardActivity.this, VoiceRoomService.class));
            }
            VoiceRoomStandardActivity.this.finish();
        } else if (id == R.id.iv_voice_room_windowed) {
            VoiceRoomStandardActivity.this.finish();
            if (VoiceRoomService.inService) {
                VoiceRoomService.roomMessages = voiceRoomMessageListCache.getData();
                // 通知显示悬浮窗
                Intent intent = new Intent();
                intent.putExtra(BroadcastAction.ROOM_WINDOW_SHOW, roomInfo);
                intent.setAction(BroadcastAction.ROOM_WINDOW_SHOW);
                sendBroadcast(intent);
            } else {
                // TODO 启动悬浮窗管理服务
                // startService(new Intent(this, VoiceRoomService.class));
            }
        }
    }

    private void addVoiceRoomRolesSystemMessage(String content) {
        RoomSystemMessage roomSystemMessage = new RoomSystemMessage();
        roomSystemMessage.messageId = System.currentTimeMillis();
        roomSystemMessage.itemType = RoomMessage.TYPE_SYSTEM;
        roomSystemMessage.content = content;
        voiceRoomMessageListCache.addData(roomSystemMessage);
        voiceRoomMessageAdapter.notifyDataSetChanged();
        voiceRoomMessageListScrollToBottom();
    }

    private void voiceRoomMessageListScrollToBottom() {
        int messageNum = voiceRoomMessageListCache.getDataSize();
        rvVoiceRoomMessage.scrollToPosition(messageNum > 0 ? messageNum - 1 : 0);
    }
}