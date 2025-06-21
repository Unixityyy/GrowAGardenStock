package com.unixity.gagstock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class BackgroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, buildNotification());

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    Log.d("Network", "✅ Network is available. Connecting WebSocket...");
                    connectToEndpointInService();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    Log.w("Network", "⚠️ Network connection lost. You may want to disconnect the WebSocket here.");
                }
            });
        }

        return START_STICKY;
    }

    private void connectToEndpointInService() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("notif_prefs", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("wss://websocket.joshlei.com/growagarden/")
                .build();

        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JSONObject json = new JSONObject(text);
                    if (!json.has("seed_stock")) return;

                    JSONArray seedStock = json.getJSONArray("seed_stock");
                    for (int i = 0; i < seedStock.length(); i++) {
                        JSONObject seed = seedStock.getJSONObject(i);
                        String rawName = seed.getString("display_name");
                        String key = rawName.toLowerCase().replaceAll("\\s+", "");

                        if (prefs.getBoolean(key, false)) {
                            int quantity = seed.optInt("quantity", 0);
                            sendSeedNotification(getApplicationContext(), rawName, quantity);
                        }
                    }
                } catch (Exception e) {
                    Log.e("WebSocket", "Parsing error", e);
                }
            }
        });
    }
    private Notification buildNotification() {
        NotificationChannel channel = new NotificationChannel(
                "channel_id", "Background Channel", NotificationManager.IMPORTANCE_MIN);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) manager.createNotificationChannel(channel);
//        i dont know how to make it non swipeable :sob:
        return new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Service Running")
                .setContentText("Running in background")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .build();
    }

    private void sendSeedNotification(Context context, String seedName, int quantity) {
        String channelId = "seed_channel";

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                channelId, "Seed Stock Alerts", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Seed Restock Alert 🌱")
                .setContentText(seedName + " is back in stock! (Stock: x" + quantity + ")")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        manager.notify(seedName.hashCode(), builder.build());
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not using binding
    }
}
