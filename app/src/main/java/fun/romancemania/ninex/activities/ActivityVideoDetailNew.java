package fun.romancemania.ninex.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.videoplay.YoutubePlay;
import com.squareup.picasso.Picasso;

public class ActivityVideoDetailNew extends AppCompatActivity {


    String st_id,st_title,st_image,st_vid,st_desc,st_time,st_catname;
    TextView title;
    private AdView mAdView;
    ImageView img_video, img_fav;
    WebView desc;
    private InterstitialAd mInterstitial;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_detailnew);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int error) {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });
        img_video = (ImageView) findViewById(R.id.picture);
        img_fav = (ImageView) findViewById(R.id.img_fav);
         title = (TextView) findViewById(R.id.text);
        desc = (WebView) findViewById(R.id.webView_text_desc);

        Intent i = getIntent();
        // position=i.getIntExtra("POSITION", 0);
        st_title = i.getStringExtra("TITLE");
        st_image = i.getStringExtra("IMAGE");
        st_vid = i.getStringExtra("LINK");
        st_desc = i.getStringExtra("DESC");


        title.setText(st_title);
        // category.setText(s);
        // duration.setText(st_time);

        desc.setBackgroundColor(getResources().getColor(R.color.card_bg));
        desc.setFocusableInTouchMode(false);
        desc.setFocusable(false);
        desc.getSettings().setDefaultTextEncodingName("UTF-8");

        WebSettings webSettings = desc.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size);
        webSettings.setDefaultFontSize(fontSize);

        String mimeType = "text/html; charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = st_desc;

        String text = "<html><head>"
                + "<style type=\"text/css\">body{color: #ffffff;}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        desc.loadData(text, mimeType, encoding);


        Picasso.with(this).load(st_image ).placeholder(R.drawable.ic_thumbnail).into(img_video);

//        List<Pojo> pojolist = db.getFavRow(vid);
//        if (pojolist.size() == 0) {
//            img_fav.setImageResource(R.drawable.ic_fav_outline);
//        } else {
//            if (pojolist.get(0).getVId().equals(vid)) {
//                img_fav.setImageResource(R.drawable.ic_fav_toggle);
//            }
//        }

        img_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(st_vid.isEmpty())
                {
                    Toast.makeText(ActivityVideoDetailNew.this,"No video",Toast.LENGTH_SHORT).show();
                }
                else {

//                    Intent i = new Intent(ActivityVideoDetailNew.this, YoutubePlay.class);
//                    i.putExtra("id", st_vid);
//                    startActivity(i);

                    LoadingDialog();
                    mInterstitial = new InterstitialAd(ActivityVideoDetailNew.this);
                    mInterstitial.setAdUnitId(getResources().getString(R.string.admob_interstitial_id));
                    mInterstitial.loadAd(new AdRequest.Builder().build());

                    mInterstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // TODO Auto-generated method stub
                            super.onAdLoaded();
                            pDialog.dismiss();
                            if (mInterstitial.isLoaded()) {
                                mInterstitial.show();
                            }
                        }

                        public void onAdClosed() {
                            Intent i = new Intent(ActivityVideoDetailNew.this, YoutubePlay.class);
                            i.putExtra("id", st_vid);
                            startActivity(i);

                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            pDialog.dismiss();
                            Intent i = new Intent(ActivityVideoDetailNew.this, YoutubePlay.class);
                            i.putExtra("id", st_vid);
                            startActivity(i);
                        }

                    });

                }



            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_share:

                String formattedString = android.text.Html.fromHtml(st_desc).toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, st_title + "\n");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    public void LoadingDialog() {
        pDialog = new ProgressDialog(ActivityVideoDetailNew.this);
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();

    }
}
