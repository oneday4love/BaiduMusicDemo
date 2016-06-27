package com.lanou.baidumusicdemo.util;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dllo on 16/6/25.
 */
public class GetDrawable {

    private static Drawable mDrawable;

    public static Drawable getDrawable(final String urlPath){
        new DownImage().execute(urlPath);

        return mDrawable;
    }


    private static Drawable getDrawableFromUrl(String urlPath) {

        try {
            mDrawable = Drawable.createFromStream(new URL(urlPath).openStream(), null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDrawable;
    }



    private static class DownImage extends AsyncTask<String, Void, Drawable>{

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                mDrawable = Drawable.createFromStream(new URL(params[0]).openStream(), null);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mDrawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            mDrawable = drawable;
        }
    }


}
