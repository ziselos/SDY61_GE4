package com.example.zisis.sdy61_ge4;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.os.Handler;


/**
 * Created by zisis on 73//17.
 */
public class splash extends AppCompatActivity {

    private final static int SPLASH_DURATION = 2000; // μετά από 2 δευτερόλεπτα η splash οθόνη φεύγει
    private boolean bckBtnPress;
    private Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
           @Override
           public void run() {
               finish();

                if(!bckBtnPress)
                {
                    Intent homeActivity = new Intent(splash.this,MainActivity.class);
                    splash.this.startActivity(homeActivity);

                }

           }
       },SPLASH_DURATION);


    }

    @Override
    public void onBackPressed(){

        bckBtnPress = true;
        super.onBackPressed();
    }
}
