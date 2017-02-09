package com.notissu.UI.Splach.View;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.notissu.UI.Main.View.MainActivity;
import com.notissu.R;
import com.notissu.UI.Splach.Presenter.SplashContract;
import com.notissu.UI.Splach.Presenter.SplashPresenter;

import io.fabric.sdk.android.Fabric;

import static com.notissu.Util.LogUtils.makeLogTag;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{
    private static final String TAG = makeLogTag(SplashActivity.class);

    SplashContract.Presenter mPresenter;

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        mPresenter = new SplashPresenter(this);
        mPresenter.start();
    }

    @Override
    public void showMain() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
