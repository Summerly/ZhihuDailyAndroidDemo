package com.example.pein.demo.cache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.pein.demo.DemoApplication;

/**
 * Created by Pein on 15/12/2.
 */
public class RequestQueueManager {
    public static RequestQueue sRequestQueue = Volley.newRequestQueue(DemoApplication.getInstance());

    public static void addRequest(Request<?> request, Object object) {
        if (object != null) {
            request.setTag(object);
        }

        sRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        sRequestQueue.cancelAll(tag);
    }
}

