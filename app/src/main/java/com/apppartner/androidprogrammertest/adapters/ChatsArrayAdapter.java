package com.apppartner.androidprogrammertest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apppartner.androidprogrammertest.R;
import com.apppartner.androidprogrammertest.models.ChatData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created on 12/23/14.
 *
 * @author Thomas Colligan
 */
public class ChatsArrayAdapter extends ArrayAdapter<ChatData>
{
    ChatCell chatCell=null;
    ChatData chatData=null;
    public ChatsArrayAdapter(Context context, List<ChatData> objects)
    {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
         chatCell = new ChatCell();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.cell_chat, parent, false);

        chatCell.usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextView);
        chatCell.messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        chatCell.imageView = (ImageView) convertView.findViewById(R.id.imageView);


        chatData = getItem(position);


        DownloadImage image= new DownloadImage();
        image.execute(chatData.avatarURL);

        return convertView;
    }

    private static class ChatCell
    {
        TextView usernameTextView;
        TextView messageTextView;
        ImageView imageView;
    }
    public class DownloadImage extends AsyncTask<String,Void,Bitmap>{
        URL url = null;
        Bitmap bmp=null;
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                url = new URL(params[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
            chatCell.imageView.setImageBitmap(circleBitmap);
            chatCell.usernameTextView.setText(chatData.username);
            chatCell.messageTextView.setText(chatData.message);

        }
    }
}
