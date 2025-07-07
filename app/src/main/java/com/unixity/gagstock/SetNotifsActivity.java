package com.unixity.gagstock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SetNotifsActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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

        if (ActivityCompat.checkSelfPermission(SetNotifsActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
        }

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        CheckBox corn = findViewById(R.id.corn);
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
        CheckBox burningbud = findViewById(R.id.burningbud);
//        CheckBox cauliflower = findViewById(R.id.cauliflower);
//        CheckBox watermelon = findViewById(R.id.watermelon);
//        CheckBox greenapple = findViewById(R.id.greenapple);
//        CheckBox avocado = findViewById(R.id.avocado);
//        CheckBox banana = findViewById(R.id.banana);
//        CheckBox pineapple = findViewById(R.id.pineapple);
//        CheckBox kiwi = findViewById(R.id.kiwi);
//        CheckBox bellpepper = findViewById(R.id.bellpepper);
//        CheckBox pricklypear = findViewById(R.id.pricklypear);
//        CheckBox loquat = findViewById(R.id.loquat);
//        CheckBox feijoa = findViewById(R.id.feijoa);
//        CheckBox sugarapple = findViewById(R.id.sugarapple);

        Button save = findViewById(R.id.save);
//        cauliflower.setChecked(prefs.getBoolean("cauliflower", false));
//        watermelon.setChecked(prefs.getBoolean("watermelon", false));
//        greenapple.setChecked(prefs.getBoolean("greenapple", false));
//        avocado.setChecked(prefs.getBoolean("avocado", false));
//        banana.setChecked(prefs.getBoolean("banana", false));
//        pineapple.setChecked(prefs.getBoolean("pineapple", false));
//        kiwi.setChecked(prefs.getBoolean("kiwi", false));
//        bellpepper.setChecked(prefs.getBoolean("bellpepper", false));
//        pricklypear.setChecked(prefs.getBoolean("pricklypear", false));
//        loquat.setChecked(prefs.getBoolean("loquat", false));
//        feijoa.setChecked(prefs.getBoolean("feijoa", false));
//        sugarapple.setChecked(prefs.getBoolean("sugarapple", false));

//        corn.setChecked(prefs.getBoolean("corn", false));
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
        burningbud.setChecked(prefs.getBoolean("burningbud", false));

        save.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();

//            editor.putBoolean("corn", corn.isChecked());
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
            editor.putBoolean("burningbud", burningbud.isChecked());

//            editor.putBoolean("cauliflower", cauliflower.isChecked());
//            editor.putBoolean("watermelon", watermelon.isChecked());
//            editor.putBoolean("greenapple", greenapple.isChecked());
//            editor.putBoolean("avocado", avocado.isChecked());
//            editor.putBoolean("banana", banana.isChecked());
//            editor.putBoolean("pineapple", pineapple.isChecked());
//            editor.putBoolean("kiwi", kiwi.isChecked());
//            editor.putBoolean("bellpepper", bellpepper.isChecked());
//            editor.putBoolean("pricklypear", pricklypear.isChecked());
//            editor.putBoolean("loquat", loquat.isChecked());
//            editor.putBoolean("feijoa", feijoa.isChecked());
//            editor.putBoolean("sugarapple", sugarapple.isChecked());

            if (editor.commit()) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
