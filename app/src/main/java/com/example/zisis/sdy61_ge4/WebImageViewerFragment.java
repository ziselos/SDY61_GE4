package com.example.zisis.sdy61_ge4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


public class WebImageViewerFragment extends Fragment {

    final static String ImageURL = "https://unsplash.it/200/300/?random";


    public WebImageViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_web_image_viewer, container, false);
        new PreviewImageTask((ImageView)view.findViewById(R.id.imageView)).execute(ImageURL);

        final TextView txtView = (TextView)view.findViewById(R.id.textView);
        final ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        Button btnNext = (Button)view.findViewById(R.id.nextImage);
        Button btnToGeo = (Button)view.findViewById(R.id.nextToGeo);
        final MediaPlayer mpBtnNext = MediaPlayer.create(getContext(), R.raw.sample3);
        final MediaPlayer mpBtnToGeo = MediaPlayer.create(getContext(),R.raw.sample1);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mpBtnNext.start();
                new PreviewImageTask((ImageView)view.findViewById(R.id.imageView)).execute(ImageURL);

            }
        });


        btnToGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpBtnToGeo.start();
                //go to Geofencing activity
                Intent intent = new Intent(getActivity(),Geofencing.class);
                startActivity(intent);



            }
        });

        return view;
    }


    /*Υλοποίηση κλάσης για την εμφάνιση τυχαίας εικόνας από url
    /* με χρήση AsyncTask για να εξαλείψω το φαινόμενο παγώματος της
    /* οθόνης όταν φορτώνει η σελίδα με τις εικόνες.
     */


    private class PreviewImageTask extends AsyncTask<String,Void,Bitmap> {

        ImageView bmImage;

        public PreviewImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            String urldisplay = params[0];
            Bitmap icon = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                icon = BitmapFactory.decodeStream(in);
            }  catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return icon;
        }


        protected void onPostExecute(Bitmap result){

            bmImage.setImageBitmap(result);
        }



    }//end of PreviewImageTask





}
