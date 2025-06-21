package com.unixity.gagstock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SetNotifsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notifs), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("notif_prefs", MODE_PRIVATE);
        CheckBox corn = findViewById(R.id.corn);
        CheckBox daffodil = findViewById(R.id.daffodil);
        CheckBox watermelon = findViewById(R.id.watermelon);
        CheckBox pumpkin = findViewById(R.id.pumpkin);
        CheckBox apple = findViewById(R.id.apple);
        CheckBox bamboo = findViewById(R.id.bamboo);
        CheckBox coconut = findViewById(R.id.coconut);
        CheckBox cactus = findViewById(R.id.cactus);
        CheckBox dragonfruit = findViewById(R.id.dragonfruit);
        CheckBox mango = findViewById(R.id.mango);
        CheckBox grape = findViewById(R.id.grape);
        CheckBox mushroom = findViewById(R.id.mushroom);
        CheckBox pepper = findViewById(R.id.pepper);
        CheckBox cacao = findViewById(R.id.cacao);
        CheckBox beanstalk = findViewById(R.id.beanstalk);
        CheckBox sugarapple = findViewById(R.id.sugarapple);
        CheckBox emberlily = findViewById(R.id.emberlily);
        Button save = findViewById(R.id.save);

        corn.setChecked(prefs.getBoolean("corn", false));
        daffodil.setChecked(prefs.getBoolean("daffodil", false));
        watermelon.setChecked(prefs.getBoolean("watermelon", false));
        pumpkin.setChecked(prefs.getBoolean("pumpkin", false));
        apple.setChecked(prefs.getBoolean("apple", false));
        bamboo.setChecked(prefs.getBoolean("bamboo", false));
        coconut.setChecked(prefs.getBoolean("coconut", false));
        cactus.setChecked(prefs.getBoolean("cactus", false));
        dragonfruit.setChecked(prefs.getBoolean("dragonfruit", false));
        mango.setChecked(prefs.getBoolean("mango", false));
        grape.setChecked(prefs.getBoolean("grape", false));
        mushroom.setChecked(prefs.getBoolean("mushroom", false));
        pepper.setChecked(prefs.getBoolean("pepper", false));
        cacao.setChecked(prefs.getBoolean("cacao", false));
        beanstalk.setChecked(prefs.getBoolean("beanstalk", false));
        sugarapple.setChecked(prefs.getBoolean("sugarapple", false));
        emberlily.setChecked(prefs.getBoolean("emberlily", false));

        save.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean("corn", corn.isChecked());
            editor.putBoolean("daffodil", daffodil.isChecked());
            editor.putBoolean("watermelon", watermelon.isChecked());
            editor.putBoolean("pumpkin", pumpkin.isChecked());
            editor.putBoolean("apple", apple.isChecked());
            editor.putBoolean("bamboo", bamboo.isChecked());
            editor.putBoolean("coconut", coconut.isChecked());
            editor.putBoolean("cactus", cactus.isChecked());
            editor.putBoolean("dragonfruit", dragonfruit.isChecked());
            editor.putBoolean("mango", mango.isChecked());
            editor.putBoolean("grape", grape.isChecked());
            editor.putBoolean("mushroom", mushroom.isChecked());
            editor.putBoolean("pepper", pepper.isChecked());
            editor.putBoolean("cacao", cacao.isChecked());
            editor.putBoolean("beanstalk", beanstalk.isChecked());
            editor.putBoolean("sugarapple", sugarapple.isChecked());
            editor.putBoolean("emberlily", emberlily.isChecked());

            if (editor.commit()) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
