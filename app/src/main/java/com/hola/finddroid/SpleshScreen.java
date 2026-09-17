package com.hola.finddroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SpleshScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splesh_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainLayout=new Intent(SpleshScreen.this,MainActivity.class);
                startActivity(mainLayout);
                finish();
            }
        },2000);
    }
    public void Animate(){
        Animation anime= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splesh_screen_anime);
        ConstraintLayout fullSpleshScreen=findViewById(R.id.main);
        fullSpleshScreen.setAnimation(anime);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainLayout=new Intent(SpleshScreen.this,MainActivity.class);
                startActivity(mainLayout);
                finish();
            }
        },2000);
    }
}