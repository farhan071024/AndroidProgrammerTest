package com.apppartner.androidprogrammertest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.apppartner.androidprogrammertest.adapters.ChatsArrayAdapter;
import com.apppartner.androidprogrammertest.models.ChatData;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ChatActivity extends ActionBarActivity
{
    private static final String LOG_TAG = "ActionBarActivity";
    private ArrayList<ChatData> chatDataArrayList;
    private ChatsArrayAdapter chatsArrayAdapter;
    private ListView listView;
    private Toolbar toolBar;
    public static Typeface type,type2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listView = (ListView) findViewById(R.id.listView);

        //Setting the suggested fonts
        type = Typeface.createFromAsset(getAssets(), "fonts/Jelloween - Machinato.ttf");
        type2 = Typeface.createFromAsset(getAssets(), "fonts/Jelloween - Machinato Light.ttf");


        //setting up the toolbar
        toolBar= (Toolbar) findViewById(R.id.include);
        toolBar.setTitle("Chat");
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

        chatDataArrayList = new ArrayList<ChatData>();
        try
        {
            String chatFileData = loadChatFile();
            JSONObject jsonData = new JSONObject(chatFileData);
            JSONArray jsonArray = jsonData.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ChatData chatData = new ChatData(jsonObject);
                chatDataArrayList.add(chatData);
            }
        }
        catch (Exception e)
        {
            Log.w(LOG_TAG, e);
        }
        chatsArrayAdapter = new ChatsArrayAdapter(this, chatDataArrayList);
        listView.setAdapter(chatsArrayAdapter);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String loadChatFile() throws IOException
    {
        InputStream inputStream = getResources().openRawResource(R.raw.chat_data);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null )
        {
            stringBuilder.append(receiveString);
            stringBuilder.append("\n");
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();

        return stringBuilder.toString();
    }

}
