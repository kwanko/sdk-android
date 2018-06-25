package fr.kwanko.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class ImageLoader {


    public void loadImage(ImageView imageView, String url){
        new AsyncLoad(imageView).execute(url);
    }

    private class AsyncLoad extends AsyncTask<String, Void, Bitmap>{

        private ImageView img;

        public AsyncLoad(ImageView img){
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                KwankoLog.e(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);
        }
    }
}
