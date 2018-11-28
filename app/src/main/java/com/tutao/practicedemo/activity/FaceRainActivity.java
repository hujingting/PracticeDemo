package com.tutao.practicedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.tutao.practicedemo.R;
import com.tutao.practicedemo.view.FaceRainView;

import java.util.ArrayList;
import java.util.List;

public class FaceRainActivity extends BaseActivity {

    FaceRainView rainView;

    Button btnStart;

    public static void  start(Context context) {
        Intent intent = new Intent(context, FaceRainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_rain);

        final int dp50Pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        rainView = (FaceRainView) findViewById(R.id.face_rain_view);
        rainView.setAutoRecycleBitmap(true);
        btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FaceRainView.Conf conf = new FaceRainView.Conf.Builder().bitmaps(getBitmaps())
//                        .emoticonHeightPixel(dp50Pixel).emoticonWidthPixel(dp50Pixel).build();
                FaceRainView.Conf conf = new FaceRainView.Conf().bitmaps(getBitmaps()).emoticonHeightPixel(dp50Pixel).emoticonWidthPixel(dp50Pixel).build();

                rainView.start(conf);
            }
        });

    }


    public List<Bitmap> getBitmaps(){
        List<Bitmap> bitmaps = new ArrayList<>();
        //TODO 此处应自行对图片宽高进行压缩，避免OOM
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_face_smile));
//        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_dog));
//        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.baby));
        return bitmaps;
    }
}
