package com.lanou.baidumusicdemo.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by dllo on 16/6/21.
 */
public class RequestQueueSingleton {

    private static RequestQueueSingleton requestQueueSingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private RequestQueueSingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new MemoryCache());
    }

    public static RequestQueueSingleton getInstance(Context context){
        if (requestQueueSingleton == null){
            synchronized (RequestQueueSingleton.class){
                if (requestQueueSingleton == null){
                    requestQueueSingleton = new RequestQueueSingleton(context);
                }
            }
        }
        return requestQueueSingleton;
    }

}
