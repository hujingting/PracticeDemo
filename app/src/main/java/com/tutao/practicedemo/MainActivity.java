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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int type = 0;
    private EditText edNum;
    private ThumbUpView mThumbUpView;
    private Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fun();

        mThumbUpView = findViewById(R.id.thumbUpView);
        edNum = findViewById(R.id.ed_num);
        btnSetting = findViewById(R.id.btn_setting);

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

        btnSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            int num = Integer.valueOf(edNum.getText().toString().trim());
            mThumbUpView.setCount(num).setThumbUp(false);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "只能输入整数", Toast.LENGTH_LONG).show();
        }
    }

    public void fun() {
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
}
