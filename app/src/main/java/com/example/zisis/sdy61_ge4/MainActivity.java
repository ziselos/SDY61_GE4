package com.example.zisis.sdy61_ge4;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity   {






    //boolean haveConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnEntry = (Button)findViewById(R.id.btnEntry);
        final TextView txtView = (TextView)findViewById((R.id.textView));
        final Button btnCheckIC = (Button)findViewById(R.id.CheckIC);
        final Animation btnVanish = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clock);


        //setting button sound using as sound the sample1.mp3 on raw folder
        final MediaPlayer mpBtnClick = MediaPlayer.create(this, R.raw.sample1);

        btnCheckIC.setVisibility(View.INVISIBLE);

        btnCheckIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking if an Internet connection exists (WiFi or Mobile). In case of none existance, a message shows up in user.
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                boolean haveConnected = netInfo != null ?true:false;


                if (haveConnected){

                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);



                }
                else {
                    Toast.makeText(getApplicationContext(),"ΠΑΡΑΚΑΛΩ ΣΥΝΔΕΘΕΙΤΕ ΣΤΟ ΙΝΤΕΡΝΕΤ ΠΡΙΝ ΣΥΝΕΧΙΣΕΤΕ",Toast.LENGTH_SHORT).show();

                }



            }
        });


        btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEntry.startAnimation(btnVanish);
                mpBtnClick.start();
                btnEntry.setVisibility(View.INVISIBLE);
                btnCheckIC.setVisibility(View.VISIBLE);


            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
