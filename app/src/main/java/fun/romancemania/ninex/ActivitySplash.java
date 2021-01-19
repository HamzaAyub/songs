package fun.romancemania.ninex;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import fun.romancemania.ninex.R;
import com.google.android.gms.ads.InterstitialAd;

public class ActivitySplash extends AppCompatActivity {

    private InterstitialAd mInterstitial;
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }

        }).start();

       /* mInterstitial = new InterstitialAd(ActivitySplash.this);
        mInterstitial.setAdUnitId(getResources().getString(R.string.admob_interstitial_id));
        mInterstitial.loadAd(new AdRequest.Builder().build());

        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                super.onAdLoaded();
                if (mInterstitial.isLoaded()) {
                    mInterstitial.show();
                }
            }

            public void onAdClosed() {
                Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (JsonUtils.isNetworkAvailable(ActivitySplash.this)) {

                } else {

                    Intent i = new Intent(ActivitySplash.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        }, SPLASH_DURATION);*/
    }
    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }
}