package com.notissu;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by forhack on 2017-02-06.
 */

public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private static RequestQueue mQueue;
    public static final String BASE_URL = "http://1004xlsrn.gonetis.com:8500/";
    public static final String BASE_SEARCH_URL = "http://m.ssu.ac.kr/html/themes/m/html/notice_univ_list.jsp?curPage=%d&sCategory=%s&sKeyType=%s&sKeyword=%s";

    @Override
    public void onCreate() {
        super.onCreate();
        networkSettings();

        pushSettings();

        databaseSettings();
    }

    private void pushSettings() {

    }

    private void databaseSettings() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    private void networkSettings() {
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue() {
        return mQueue;
    }
}
