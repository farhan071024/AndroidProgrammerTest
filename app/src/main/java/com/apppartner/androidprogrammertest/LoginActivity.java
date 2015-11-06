package com.apppartner.androidprogrammertest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity
{
    EditText userName,userPassword;
    private Toolbar toolBar;
    String nameAndPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName= (EditText) findViewById(R.id.editText);
        userPassword= (EditText) findViewById(R.id.editText2);
        //setting up the toolbar
        toolBar= (Toolbar) findViewById(R.id.include);
        toolBar.setTitle("Login");
        toolBar.setBackgroundColor(Color.BLACK);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // applying the suggested font
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Jelloween - Machinato.ttf");
//        Typeface type2 = Typeface.createFromAsset(getAssets(), "fonts/Jelloween - Machinato ExtraLight.ttf");
        userName.setTypeface(type);
        userPassword.setTypeface(type);


        userName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userName.setText("");
                return false;
            }
        });
        userPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userPassword.setText("");
                return false;
            }
        });
    }
    public void login(View v){
      String  name=userName.getText().toString();
      String  password=userPassword.getText().toString();
      nameAndPassword= name+","+password;
      BackgroundTask bt=new BackgroundTask();
      bt.execute(nameAndPassword);
    }
    public class BackgroundTask extends AsyncTask<String,Void,String>{
        String Info[]={};
        String text;
        Long startTime,finishTime;

        @Override
        protected void onPreExecute() {
            startTime=System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(String... params) {
            Info=params[0].split(",");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://dev.apppartner.com/AppPartnerProgrammerTest/scripts/login.php");

            try {
                // Add your data
                List nameValuePairs = new ArrayList(2);
                nameValuePairs.add(new BasicNameValuePair("username",Info[0]));
                nameValuePairs.add(new BasicNameValuePair("password",Info[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                InputStream is = response.getEntity().getContent();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(20);

                int current = 0;

                while((current = bis.read()) != -1){
                    baf.append((byte)current);
                }

            /* Convert the Bytes read to a String. */
                text = new String(baf.toByteArray());
                //txtvw.setText(text);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            finishTime=System.currentTimeMillis();
            try {
                final JSONObject object= new JSONObject(s);
                // creates alert dialog
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(object.getString("code"))
                        .setMessage(object.getString("message")+"\nThe api call took " + (finishTime - startTime) + " miliseconds")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (object.getString("code").matches("Success")) {
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
