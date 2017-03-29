package com.example.zisis.sdy61_ge4;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText usr,pass;
    TextView message;
    private SignInButton btnLogin;
    private final static int RC_SIGN_IN = 1;
    Button btnNext;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usr = (EditText)findViewById(R.id.usr);
        pass = (EditText)findViewById(R.id.pass);
        btnLogin = (SignInButton)findViewById(R.id.btnLogIn);
        message = (TextView)findViewById(R.id.Login);
        btnNext = (Button)findViewById(R.id.next);
        btnNext.setVisibility(View.GONE);

        final MediaPlayer mpBtnLogin = MediaPlayer.create(this,R.raw.sample2);
        final MediaPlayer mpBtnNext = MediaPlayer.create(this,R.raw.sample1);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mpBtnLogin.start();
                signIn();

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mpBtnNext.start();
                //Toast.makeText(getApplicationContext(),"Go to next image view",Toast.LENGTH_SHORT).show();

                message.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                //Now i will try to show the Image View Fragment that i have create (fragment_web_image_viewer)
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                WebImageViewerFragment webImageViewerFragment = new WebImageViewerFragment();
                fragmentTransaction.add(R.id.Fragment_Place, webImageViewerFragment);
                fragmentTransaction.commit();


            }
        });


    }

    private void signIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);

    }


    private void handleResult(GoogleSignInResult googleSignInResult){

        boolean var = googleSignInResult.isSuccess();
        updateUI((var == true) ? true : false);

    }


    private void updateUI(boolean isLogin){

        if (isLogin){
            btnLogin.setVisibility(View.GONE);
            usr.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
            message.setText("Αυθεντικοποίηση Επιτυχής");
            btnNext.setVisibility(View.VISIBLE);

        }
        else {
            Toast.makeText(getApplicationContext(),"Λανθασμένα στοιχεία.Ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
            startActivity(intent);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);

        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
