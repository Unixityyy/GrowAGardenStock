package com.unixity.gagstock;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private LinearLayout scrollContent;
    private ScrollView scrollContainer;
    private TextView loading;
    private Button btn1, btn2, btn3, btn4;

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
        scrollContent = findViewById(R.id.scrollContent);
        scrollContainer = findViewById(R.id.scrollContainer);
        loading = findViewById(R.id.loading);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        btn1.setOnClickListener(v -> {
            scrollContent.removeAllViews();
            scrollContainer.setVisibility(View.VISIBLE);
            loading.setVisibility(ViewPager2.VISIBLE);
            loading.setText("Loading...");
            fetchAndDisplaySeeds();
        });

        btn2.setOnClickListener(v -> {
            scrollContainer.setVisibility(View.GONE);
            loading.setText("Gears Coming soon");
            loading.setVisibility(View.VISIBLE);
        });

        btn3.setOnClickListener(v -> {
            scrollContainer.setVisibility(View.GONE);
            loading.setText("Eggs Coming soon");
            loading.setVisibility(View.VISIBLE);
        });

        btn4.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Unixityyy/GrowAGardenStock"));
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        });

        fetchAndDisplaySeeds();
    }

    private void fetchAndDisplaySeeds() {
        String infoUrl = "https://growagardenapi.vercel.app/api/item-info";
        String stockUrl = "https://growagardenapi.vercel.app/api/stock/GetStock";
        TextView loading = findViewById(R.id.loading);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest infoRequest = new JsonArrayRequest(Request.Method.GET, infoUrl, null,
                infoResponse -> {
                    try {
                        Map<String, String> imageMap = new HashMap<>();
                        List<String> seedOrder = new ArrayList<>();
                        for (int i = 0; i < infoResponse.length(); i++) {
                            JSONObject item = infoResponse.getJSONObject(i);
                            if ("Fruits".equals(item.optString("category"))) {
                                String name = item.getString("name");
                                String image = item.optString("image", null);
                                seedOrder.add(name);
                                if (image != null) imageMap.put(name, image);
                            }
                        }

                        JsonObjectRequest stockRequest = new JsonObjectRequest(Request.Method.GET, stockUrl, null,
                                stockResponse -> {
                                    try {
                                        JSONObject data = stockResponse.getJSONObject("Data");
                                        JSONArray seeds = data.getJSONArray("seeds");

                                        Map<String, JSONObject> seedMap = new HashMap<>();
                                        for (int i = 0; i < seeds.length(); i++) {
                                            JSONObject seed = seeds.getJSONObject(i);
                                            seedMap.put(seed.getString("name"), seed);
                                        }

                                        loading.setText("");
                                        for (String seedName : seedOrder) {
                                            if (seedMap.containsKey(seedName)) {
                                                String stock = seedMap.get(seedName).getString("stock");
                                                String imageUrl = imageMap.getOrDefault(seedName, "");
                                                addSeedCard(seedName, stock, imageUrl);
                                            }
                                        }

                                    } catch (Exception e) {
                                        Log.e("SEED_PARSE", "Error", e);
                                    }
                                },
                                error -> Log.e("SEED_API", "Failed", error)
                        );

                        queue.add(stockRequest);
                    } catch (Exception e) {
                        Log.e("INFO_PARSE", "Error", e);
                    }
                },
                error -> Log.e("INFO_API", "Failed", error)
        );

        queue.add(infoRequest);
    }

    private void addSeedCard(String name, String stock, String imageUrl) {
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
        Glide.with(this).load(imageUrl).into(image);

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

        scrollContent.addView(card);
    }
}