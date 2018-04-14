package com.tutao.practicedemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int type = 0;
    private EditText edNum;
    private ThumbUpView mThumbUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fun();

        mThumbUpView = findViewById(R.id.thumbUpView);
        edNum = findViewById(R.id.ed_num);

        mThumbUpView.setThumbUpClickListener(new ThumbView.ThumbUpClickListener() {
            @Override
            public void thumbUpFinish() {
                Log.d("MainActivity", "Old点赞成功");
            }

            @Override
            public void thumbDownFinish() {
                Log.d("MainActivity", "Old点赞取消");
            }
        });
    }

    public void setNum(View view) {
        try {
            int num = Integer.valueOf(edNum.getText().toString().trim());
            mThumbUpView.setCount(num).setThumbUp(false);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "只能输入整数", Toast.LENGTH_LONG).show();
        }
    }

    public void fun() {
        final UpDownTextView textView = (UpDownTextView) findViewById(R.id.up_down_text_view);
        final ArrayList<String> titleList = new ArrayList<String>();
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        for (int i = 0; i < 10; i++) {
            titleList.add(i + "");
        }
        textView.setTextList(titleList);
        final ImageView ivShining = (ImageView) findViewById(R.id.iv_shining);
        final ImageView ivLike = (ImageView) findViewById(R.id.iv_like);
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                textView.startAutoScroll();
                if (type == 1) {
                    ivShining.setVisibility(View.GONE);
                    ivLike.setImageResource(R.mipmap.ic_comment_like);
                } else {
                    ivLike.setImageResource(R.mipmap.ic_messages_like_selected);
                }


                ivLike.animate()
                        .scaleX(0.5f)
                        .scaleY(0.5f)
                        .setDuration(300)
                        .setInterpolator(new CycleInterpolator(0.5f))
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                ivLike.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivLike.setScaleX(1);
                                        ivLike.setScaleY(1);
                                        if (type == 0) {
                                            type = 1;
//                                            ivLike.setImageResource(R.mipmap.ic_messages_like_selected);
                                            ivShining.setVisibility(View.VISIBLE);
                                        } else {
                                            type = 0;
                                        }
                                    }
                                }, 150);
                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
