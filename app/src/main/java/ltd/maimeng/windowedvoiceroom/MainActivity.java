package ltd.maimeng.windowedvoiceroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ltd.maimeng.windowedvoiceroom.service.VoiceRoomService;

public class MainActivity extends AppBaseActivity {

    @Override
    public View setContentView() {
        return View.inflate(MainActivity.this, R.layout.activity_main, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.btn_voice_room_single_instance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VoiceRoomService.inService) {
                    VoiceRoomInfo roomInfo = VoiceRoomService.roomInfo;
                    if (roomInfo != null && roomInfo.getType() != VoiceRoomInfo.TYPE_SINGLE_INSTANCE) {
                        stopService(new Intent(MainActivity.this, VoiceRoomService.class));
                    }
                }

                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent(BroadcastAction.ROOM_WINDOW_HIDE));

                Intent intent = new Intent(MainActivity.this, VoiceRoomSingleInstanceActivity.class);
                intent.putExtra(VoiceRoomInfo.PARAM_ROOM_INFO, new VoiceRoomInfo(VoiceRoomInfo.TYPE_SINGLE_INSTANCE));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_voice_room_standard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VoiceRoomService.inService) {
                    VoiceRoomInfo roomInfo = VoiceRoomService.roomInfo;
                    if (roomInfo != null && roomInfo.getType() != VoiceRoomInfo.TYPE_STANDARD) {
                        stopService(new Intent(MainActivity.this, VoiceRoomService.class));
                    }
                }

                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent(BroadcastAction.ROOM_WINDOW_HIDE));

                Intent intent = new Intent(MainActivity.this, VoiceRoomStandardActivity.class);
                intent.putExtra(VoiceRoomInfo.PARAM_ROOM_INFO, new VoiceRoomInfo(VoiceRoomInfo.TYPE_STANDARD));
                startActivity(intent);
            }
        });
    }
}