package com.notissu.UI.Main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.notissu.Model.Keyword;
import com.notissu.UI.Main.AddKeywordDialog.AddKeywordContract;
import com.notissu.UI.Main.AddKeywordDialog.AddKeywordDialog;
import com.notissu.UI.NoticeList.NoticeListFragment;
import com.notissu.UI.NoticeTab.NoticeTabFragment;
import com.notissu.UI.Setting.SettingFragment;
import com.notissu.Model.NavigationMenu;
import com.notissu.Notification.Alarm;
import com.notissu.R;
import com.notissu.Util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

//메인 화면이 나오는 Activity
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {
    private static final String TAG = LogUtils.makeLogTag(MainActivity.class);

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.main_nav_view)
    NavigationView navigationView;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_fab)
    FloatingActionButton fab;

    private ActionBarDrawerToggle mToggle;

    private AddKeywordDialog mAddKeywordDialog;

    //현재 어떤 Fragment가 띄워져 있는지.
    private int mPresenterFragment;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mPresenter = new MainPresenter(getIntent().getExtras(), this);
        mPresenter.start();

        Alarm.cancel(getApplicationContext());
        mToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mToggle.syncState();
        drawer.setDrawerListener(mToggle);

        navigationView.setNavigationItemSelectedListener(this);

        mAddKeywordDialog = AddKeywordDialog.newInstance();
        mAddKeywordDialog.setOnAddKeywordListner(new AddKeywordContract.OnAddKeywordListner() {
            @Override
            public void onAdd(Keyword keyword) {
                mPresenter.onAddNewItem(keyword);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onFabClick();
            }
        });

        LinearLayout easterEgg = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_easter_egg);
        easterEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "서라야 사랑해♥", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mPresenterFragment == MainContract.FLAG_MAIN_NOTICE) {
                super.onBackPressed();
            } else {
                int presentFegment = MainContract.FLAG_MAIN_NOTICE;
                String title = NavigationMenu.getInstance().getFristItemTitle();
                Fragment fragment = NoticeTabFragment.newInstance(presentFegment, title);
                showFragment(presentFegment, fragment);
            }
        }
    }

    /*
        Navigation의 메뉴가 클릭 됐을 때 생기는 Event 구현
        */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        fab.show();

        int id = item.getItemId();
        int groupid = item.getGroupId(); // keyword 그룹아이디 가져오기
        String itemTitle = item.getTitle().toString(); // keyword 구분을 위한 제목 가져오기

        if (id == R.id.nav_ssu_main) {
            //Main 공지사항
            int flag = MainContract.FLAG_MAIN_NOTICE;
            Fragment fragment = NoticeTabFragment.newInstance(flag, itemTitle);
            showFragment(flag, fragment);
        } else if (id == R.id.nav_ssu_library) {
            //도서관 공지사항
            int flag = MainContract.FLAG_LIBRARY_NOTICE;
            Fragment fragment = NoticeListFragment.newInstance(flag, itemTitle);
            showFragment(flag, fragment);
        } else if (id == R.id.nav_starred) {
            //즐겨찾기
            int flag = MainContract.FLAG_STARRED;
            Fragment fragment = NoticeListFragment.newInstance(flag, itemTitle);
            showFragment(flag, fragment);
        } else if (groupid == R.id.group_keyword) {
            int flag = MainContract.FLAG_KEYWORD;
            Fragment fragment = NoticeListFragment.newInstance(flag, itemTitle);
            showFragment(flag, fragment);
        } else if (id == R.id.nav_setting) {
            //설정 공지사항
            int flag = MainContract.FLAG_SETTING;
            Fragment fragment = SettingFragment.newInstance();
            showFragment(flag, fragment);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showFragment(int presentFegment, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        this.mPresenterFragment = presentFegment;
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void showAddKeyword() {
        mAddKeywordDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void showNavigation() {
        NavigationMenu navigationMenu = NavigationMenu.getInstance();
        navigationMenu.setMenu(navigationView);
    }

    @Override
    public void addMenuKeyword(Keyword keyword) {
        NavigationMenu navigationMenu = NavigationMenu.getInstance();
        navigationMenu.addKeyword(keyword);
    }
}
