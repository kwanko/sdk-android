package fr.kwanko.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * SourceCode
 * Created by erusu on 5/26/2017.
 */

public class KwankoBitmapUtils {

    private KwankoBitmapUtils(){
        throw new AssertionError("instance is not allowed");
    }

    private static Bitmap getCustomCloseForAdd(String urlString) {
        try {
            java.net.URL url = new java.net.URL(urlString);
            InputStream input ;
            if(URLUtil.isHttpsUrl(urlString)) {
                HttpsURLConnection connection = (HttpsURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();

            }else{
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            KwankoLog.e(e);
            return null;
        }
    }

    public static File saveBitmapToFile(String url,File bitmapFile){
        Bitmap bmp = getCustomCloseForAdd( url);
        if(bmp == null){
            return null;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(bitmapFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmapFile;
        } catch (Exception e) {
            KwankoLog.e(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                KwankoLog.e(e);
            }
        }
        return null;
    }

    public static Bitmap loadBitmapFromFile(File file){
        if(file == null){
            return null;
        }
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            KwankoLog.e(e);
        }
        return null;
    }

    public static boolean setBackgroundForClosableRegion(View view, File file){
        if(file == null || view == null){
            return false;
        }
        if(!(view instanceof ImageView) && view instanceof ViewGroup){
            return setBackgroundForClosableRegion(((ViewGroup) view).getChildAt(0),file);
        }
        Bitmap b = loadBitmapFromFile(file);
        if(b == null){
            return false;
        }
        setImageBitmapForClosableRegion(view,b);
        return true;
    }

    public static void setImageBitmapForClosableRegion(View view, Bitmap b){
        if(!(view instanceof ImageView)){
            return;
        }
        ((ImageView) view).setImageBitmap(b);
    }

    public static void setImageResourceForClosableRegion(View view, int resource){
        if(!(view instanceof ImageView) && view instanceof ViewGroup){
            setImageResourceForClosableRegion(((ViewGroup) view).getChildAt(0),resource);
            return;
        }
        if(view instanceof ImageView) {
            ((ImageView) view).setImageResource(resource);
        }
    }

    public static File getCloseBtnFile(Context context){
        return new File(context.getCacheDir(),"closeBtn.png");
    }
}
