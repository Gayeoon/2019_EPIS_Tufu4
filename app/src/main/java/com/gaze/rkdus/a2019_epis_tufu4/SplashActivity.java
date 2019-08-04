package com.gaze.rkdus.a2019_epis_tufu4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class SplashActivity extends Activity {
    private Handler mHandler;
    private Runnable mRunnable;

    public String TAG = "LogGoGo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        StringBuffer sb = new StringBuffer();
        sb.append("density : " + dm.density);
        sb.append(" densityDPI : " + dm.densityDpi);
        sb.append(" widthPixels : " + dm.widthPixels);
        sb.append(" heightPixels : " + dm.heightPixels);
        Log.d(TAG, sb.toString());

        ImageView imageView = findViewById(R.id.splash);
//        Glide.with(this)
//                .load(R.raw.splash)
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .into(imageView);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView);
        Glide.with(getApplicationContext()).load(R.drawable.splash_min).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifImage);

        // MainActivity.class 자리에 다음에 넘어갈 액티비티를 넣어주기
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
//        // Handler를 통한 대기시간 설정
//        // (임의로 출력 확인을 위해 적은것 뿐, 원래는 앱 로딩 끝나면 바로 넘어가게 되어있음)
        mHandler = new Handler();
//        // mRunnable 내부 run() 실행하려면 기다려야 하는 delayMillis
        mHandler.postDelayed(mRunnable, 3000);
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}
