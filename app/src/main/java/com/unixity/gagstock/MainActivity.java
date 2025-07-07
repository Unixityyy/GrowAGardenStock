package com.unixity.gagstock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.bumptech.glide.Glide;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {
    private LinearLayout scrollContent;
    private ScrollView scrollContainer;
    private TextView loading;
    private SharedPreferences prefs;
    private LinearLayout gearContent;
    private ScrollView gearContainer;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
        }

        if (!isMyServiceRunning()) {
            Intent serviceIntent = new Intent(this, BackgroundService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs.getInt("dcid", 0) == 0)
        {
            Intent discordIntent = new Intent(MainActivity.this, IdActivity.class);
            startActivity(discordIntent);
        }
        scrollContent = findViewById(R.id.scrollContent);
        scrollContainer = findViewById(R.id.scrollContainer);
        gearContent = findViewById(R.id.gearContent);
        gearContainer = findViewById(R.id.gearContainer);
        loading = findViewById(R.id.loading);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        loading.setText(R.string.load);

        btn1.setOnClickListener(v -> {
            gearContainer.setVisibility(View.GONE);
            scrollContainer.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        });

        btn2.setOnClickListener(v -> {
            scrollContainer.setVisibility(View.GONE);
            gearContainer.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        });

        btn3.setOnClickListener(v -> {
            scrollContainer.setVisibility(View.GONE);
            gearContainer.setVisibility(View.GONE);
            loading.setText("Eggs Coming soon");
            loading.setVisibility(View.VISIBLE);
        });

        btn4.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/Unixityyy/GrowAGardenStock"));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        btn5.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetNotifsActivity.class);
            startActivity(intent);
        });

        fetchAndDisplaySeeds();
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void fetchAndDisplaySeeds() {
//        String infoUrl = "https://growagardenapi.vercel.app/api/item-info";
//        loading.setText(R.string.load);
//        RequestQueue queue = Volley.newRequestQueue(this);
        List<String> seedOrder = getStrings("seed");
        List<String> gearOrder = getStrings("gear");
//        JsonArrayRequest infoRequest = new JsonArrayRequest(Request.Method.GET, infoUrl, null,
//                infoResponse -> {
//                    try {
//
//                        for (int i = 0; i < infoResponse.length(); i++) {
//                            JSONObject item = infoResponse.getJSONObject(i);
//                            if ("Fruits".equals(item.optString("category"))) {
//                                seedOrder.add(item.getString("name"));
//                            }
//                        }
//
//                        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
//                            @Override
//                            public void onAvailable(@NonNull Network network) {
//                                Log.d("Network", "Connected: " + network);
//                                connectToEndpoint(seedOrder);
//                            }
//
//                            @Override
//                            public void onLost(@NonNull Network network) {
//                                Log.d("Network", "Disconnected");
//                            }
//                        });
//
//                    } catch (Exception e) {
//                        Log.e("INFO_PARSE", "Error parsing item info", e);
//                    }
//                },
//                error -> Log.e("INFO_API", "Failed to load item info", error)
//        );
        connectToEndpoint(seedOrder, gearOrder);
//        queue.add(infoRequest);
    }

    @NonNull
    private static List<String> getStrings(String type) {
        List<String> order = new ArrayList<>();
        if (Objects.equals(type, "seed")) {
            order.add("Carrot");
            order.add("Strawberry");
            order.add("Blueberry");
            order.add("Orange Tulip");
            order.add("Tomato");
            order.add("Daffodil");
            order.add("Watermelon");
            order.add("Pumpkin");
            order.add("Apple");
            order.add("Bamboo");
            order.add("Coconut");
            order.add("Cactus");
            order.add("Dragon Fruit");
            order.add("Mango");
            order.add("Grape");
            order.add("Mushroom");
            order.add("Pepper");
            order.add("Cacao");
            order.add("Beanstalk");
            order.add("Ember Lily");
            order.add("Sugar Apple");
            order.add("Burning Bud");
            return order;
        } else if (Objects.equals(type, "gear")) {
            order.add("Watering Can");
            order.add("Trowel");
            order.add("Recall Wrench");
            order.add("Basic Sprinkler");
            order.add("Advanced Sprinkler");
            order.add("Godly Sprinkler");
            order.add("Magnifying Glass");
            order.add("Tanning Mirror");
            order.add("Master Sprinkler");
            order.add("Cleaning Spray");
            order.add("Favorite Tool");
            order.add("Harvest Tool");
            order.add("Friendship Pot");
            return order;
        }
        return order;
    }


    private void connectToEndpoint(List<String> seedOrder, List<String> gearOrder) {
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("wss://websocket.joshlei.com/growagarden?user_id=" + prefs.getInt("dcid", 0))
                .build();
        try {
            client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                    Log.d("WebSocket", "Connected to endpoint");
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    try {
                        JSONObject json = new JSONObject(text);
                        if (!json.has("seed_stock") || !json.has("gear_stock")) {
                            Log.d("WebSocket", "Skipping irrelevant packet");
                            return;
                        }

                        if (json.has("seed_stock")) {
                            JSONArray seedStock = json.getJSONArray("seed_stock");
                            Map<String, JSONObject> stockMap = new HashMap<>();

                            for (int i = 0; i < seedStock.length(); i++) {
                                JSONObject seed = seedStock.getJSONObject(i);
                                stockMap.put(seed.getString("display_name"), seed);
                            }

                            runOnUiThread(() -> {
                                scrollContent.removeAllViews();
                                loading.setText("");
                                for (String name : seedOrder) {
                                    if (stockMap.containsKey(name)) {
                                        JSONObject seed = stockMap.get(name);
                                        String quantity = String.valueOf(seed.optInt("quantity", 0));
                                        String icon = seed.optString("icon", "");
                                        addCard(name, quantity, icon, "seed");
                                    }
                                }
                            });
                        }
                        if (json.has("gear_stock")) {
                            JSONArray gearStock = json.getJSONArray("gear_stock");
                            Map<String, JSONObject> stockMap = new HashMap<>();

                            for (int i = 0; i < gearStock.length(); i++) {
                                JSONObject gear = gearStock.getJSONObject(i);
                                stockMap.put(gear.getString("display_name"), gear);
                            }

                            runOnUiThread(() -> {
                                gearContent.removeAllViews();
                                loading.setText("");
                                for (String name : gearOrder) {
                                    if (stockMap.containsKey(name)) {
                                        JSONObject seed = stockMap.get(name);
                                        String quantity = String.valueOf(seed.optInt("quantity", 0));
                                        String icon = seed.optString("icon", "");
                                        addCard(name, quantity, icon, "gear");
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.e("WebSocket", "Error parsing message", e);
                    }
                }


                @Override
                public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                    Log.e("WebSocket", "Connection failed", t);
                    scrollContent.removeAllViews();
                    gearContent.removeAllViews();
                    loading.setText(R.string.reconnecting);
                    loading.setVisibility(View.VISIBLE);
                    fetchAndDisplaySeeds();
                }
            });
        } catch (Exception e) {
            Log.e("WebSocket", "java angy: ", e);
            fetchAndDisplaySeeds();
        }

        client.dispatcher().executorService().shutdown();
    }

    @SuppressLint("SetTextI18n")
    private void addCard(String name, String stock, String imageUrl, String type) {
        Typeface gagFont = ResourcesCompat.getFont(this, R.font.gagfont);
        CardView card = new CardView(this);
        card.setRadius(12);
        card.setCardElevation(4);
        card.setUseCompatPadding(true);
        card.setCardBackgroundColor(Color.parseColor("#AA666666"));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setPadding(16, 16, 16, 16);

        ImageView image = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(300, 300);
        imageParams.setMargins(0, 0, 16, 0);
        image.setLayoutParams(imageParams);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (!isFinishing() && !isDestroyed()) {
            Glide.with(this).load(imageUrl).into(image);
        }

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView nameView = new TextView(this);
        nameView.setText(name);
        nameView.setTextSize(36);
        nameView.setTypeface(gagFont);


        TextView stockView = new TextView(this);
        stockView.setText("Stock: x" + stock);
        stockView.setTextSize(18);
        stockView.setTypeface(gagFont);

        textLayout.addView(nameView);
        textLayout.addView(stockView);

        layout.addView(image);
        layout.addView(textLayout);
        card.addView(layout);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(16, 16, 16, 0);
        card.setLayoutParams(cardParams);
        if (Objects.equals(type, "seed")) {
            scrollContent.addView(card);
        } else if (Objects.equals(type, "gear")) {
            gearContent.addView(card);
        }
    }
}