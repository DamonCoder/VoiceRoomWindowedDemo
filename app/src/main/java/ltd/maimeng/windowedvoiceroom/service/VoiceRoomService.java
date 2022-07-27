package ltd.maimeng.windowedvoiceroom.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ltd.maimeng.core.utils.AppUtil;
import ltd.maimeng.windowedvoiceroom.BroadcastAction;
import ltd.maimeng.windowedvoiceroom.R;
import ltd.maimeng.windowedvoiceroom.VoiceRoomInfo;
import ltd.maimeng.windowedvoiceroom.VoiceRoomSingleInstanceActivity;
import ltd.maimeng.windowedvoiceroom.VoiceRoomStandardActivity;
import ltd.maimeng.windowedvoiceroom.im.RoomMessage;
import ltd.maimeng.windowedvoiceroom.widget.FloatingMagnetView;
import ltd.maimeng.windowedvoiceroom.widget.FloatingView;
import ltd.maimeng.windowedvoiceroom.widget.GlobalFloatingWindow;
import ltd.maimeng.windowedvoiceroom.widget.MagnetViewListener;

public class VoiceRoomService extends Service {

    private String CHANNEL_ID = AppUtil.getPackageName();
    private String CHANNEL_NAME = AppUtil.getAppName();

    public static boolean inService = false;
    public static List<RoomMessage> roomMessages = new ArrayList<>();

    private GlobalFloatingWindow globalFloatingWindow;

    public static final String PARAM_ROOM_INFO = "param_room_info";
    public static VoiceRoomInfo roomInfo;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        inService = true;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ROOM_WINDOW_SHOW);
        intentFilter.addAction(BroadcastAction.ROOM_WINDOW_HIDE);
        // intentFilter.addAction(BroadcastAction.RONG_VOICE_ROOM_MESSAGE);
        // intentFilter.addAction(BroadcastAction.RONG_VOICE_ROOM_MESSAGE_RECALL);
        LocalBroadcastManager.getInstance(VoiceRoomService.this).registerReceiver(RoomBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // globalFloatingWindow.show();

        roomInfo = (VoiceRoomInfo) intent.getSerializableExtra(VoiceRoomInfo.PARAM_ROOM_INFO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(channel);
        }
        startForeground(AppUtil.getVersionCode(), getNotification());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        inService = false;
        LocalBroadcastManager.getInstance(VoiceRoomService.this).sendBroadcast(new Intent(BroadcastAction.ROOM_WINDOW_HIDE));
    }

    /**
     * 获取通知(Android8.0后需要)
     *
     * @return
     */
    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("语音房")
                // .setContentIntent(getIntent())
                .setContentText("后台运行中");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        return builder.build();
    }

    private BroadcastReceiver RoomBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.ROOM_WINDOW_SHOW.equals(action)) {
                VoiceRoomInfo roomInfo = (VoiceRoomInfo) intent.getSerializableExtra(BroadcastAction.ROOM_WINDOW_SHOW);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    // 判断是否拥有悬浮窗权限，无则跳转悬浮窗权限授权页面
//                    if (Settings.canDrawOverlays(VoiceRoomService.this)) {
//                         showGlobalFloatingWindow(roomInfo);
//                    } else {
                showAppFloatingWindow(roomInfo);
//                    }
//                } else {
//                     showGlobalFloatingWindow(roomInfo);
//                }
            } else if (BroadcastAction.ROOM_WINDOW_HIDE.equals(action)) {
                hideAppFloatingWindow();
            }
        }
    };

    private void showGlobalFloatingWindow(final VoiceRoomInfo roomInfo) {
        if (globalFloatingWindow == null) {
            globalFloatingWindow = new GlobalFloatingWindow(VoiceRoomService.this);
        }
        globalFloatingWindow.show();
        globalFloatingWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToVoiceRoom(roomInfo);
            }
        });
    }

    private void showAppFloatingWindow(VoiceRoomInfo roomInfo) {
        FloatingView.get().add();
        FloatingView.get().listener(new MagnetViewListener() {
            @Override
            public void onRemove(FloatingMagnetView magnetView) {
            }

            @Override
            public void onClick(FloatingMagnetView magnetView) {
                jumpToVoiceRoom(roomInfo);
            }
        });
    }

    private void hideAppFloatingWindow() {
        FloatingView.get().remove();
    }

    private void jumpToVoiceRoom(VoiceRoomInfo roomInfo) {
        // 隐藏悬浮窗
        LocalBroadcastManager.getInstance(VoiceRoomService.this).sendBroadcast(new Intent(BroadcastAction.ROOM_WINDOW_HIDE));
        // 跳转到语音房
        if (VoiceRoomInfo.TYPE_SINGLE_INSTANCE == roomInfo.getType()) {
            Intent intent = new Intent(VoiceRoomService.this, VoiceRoomSingleInstanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(VoiceRoomInfo.PARAM_ROOM_INFO, roomInfo);
            startActivity(intent);
        } else if (VoiceRoomInfo.TYPE_STANDARD == roomInfo.getType()) {
            Intent intent = new Intent(VoiceRoomService.this, VoiceRoomStandardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(VoiceRoomInfo.PARAM_ROOM_INFO, roomInfo);
            startActivity(intent);
        }
    }
}
