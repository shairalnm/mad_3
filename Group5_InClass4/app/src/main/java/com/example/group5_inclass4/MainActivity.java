package com.example.group5_inclass4;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    ImageView imageView;
    Handler handler;
    ProgressBar progressBar;
    Button buttonAT,buttonThread;
    final String downloadUrl = "https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";
    final String downloadUrl2 ="https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Display Image");

        buttonAT = findViewById(R.id.buttonAsyncTask);
        buttonThread = findViewById(R.id.buttonThread);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);



        buttonAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new DoWorkAsync().execute(downloadUrl);
            }
        });
        buttonThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Log.d("demo","image recieved");
                        Bitmap bitmap = (Bitmap) msg.obj;
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });

            }

        });

    }
    class DoWorkAsync extends AsyncTask< String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... param) {
            // TODO Auto-generated method stub
            return getImageBitmap(param[0]);
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
            //progressBar = ProgressDialog.show(MainActivity.this,"Wait", "Downloading Image");
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
            imageView.setImageBitmap(result);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }

        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }




    }
    class DoWork extends Thread implements Runnable{

        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void run() {
            Log.d("demo","Inside Run");

            Bitmap myBitmap = getImageBitmap(downloadUrl2);
            Message message =new Message();
            message.obj= myBitmap;
            handler.sendMessage(message);
            Log.d("demo","Image Displayed!!!");
        }
    }
}
