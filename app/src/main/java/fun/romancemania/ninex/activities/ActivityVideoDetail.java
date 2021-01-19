package fun.romancemania.ninex.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
//import androidx.core.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.cache.ImageLoader;
import fun.romancemania.ninex.models.ItemModelNewRelease;
import fun.romancemania.ninex.utilities.DatabaseHandler;
import fun.romancemania.ninex.videoplay.YoutubePlay;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityVideoDetail extends AppCompatActivity {

    int position;
    ImageView vp_imageview;
    ViewPager viewpager;
    int TOTAL_IMAGE;
    private Menu menu;
    TextView title, category, duration;
    WebView desc;
   // String vid, video_id;
   // String vtype, videoname, videourl, videoduration, videocatname, videocatid, videodesc, videoimageurl;
   // String VideoPlayId;
    ImageView img_video, img_fav;
    ImageLoader imageLoader;
    DatabaseHandler db;
    List<ItemModelNewRelease> itemNewsList;
    ItemModelNewRelease object;
    final Context context = this;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    private AdView mAdView;
    private InterstitialAd interstitial;
    String st_id,st_title,st_image,st_vid,st_desc,st_time,st_catname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        Intent i=getIntent();
       // position=i.getIntExtra("POSITION", 0);
        st_title=i.getStringExtra("TITLE");
        st_image=i.getStringExtra("IMAGE");
        st_vid=i.getStringExtra("LINK");
        st_desc=i.getStringExtra("DESC");

        Log.e("tiittt",""+st_title);

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

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        img_video = (ImageView) findViewById(R.id.picture);
        img_fav = (FloatingActionButton) findViewById(R.id.img_fav);

        title = (TextView) findViewById(R.id.text);
        category = (TextView) findViewById(R.id.text_category);
       // duration = (TextView) findViewById(R.id.second);

        desc = (WebView) findViewById(R.id.webView_text_desc);

       // db = new DatabaseHandler(ActivityVideoDetail.this);

     //   itemNewsList = new ArrayList<ItemModelNewRelease>();

//        if (JsonUtils.isNetworkAvailable(ActivityVideoDetail.this)) {
//            new MyTask().execute(Config.SERVER_URL + "/api.php?id=" + JsonConfig.VIDEO_ITEMIDD);
//        } else {
//            Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
//        }

        title.setText(st_title);
       // category.setText(s);
       // duration.setText(st_time);

        desc.setBackgroundColor(Color.parseColor("#ffffff"));
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
                + "<style type=\"text/css\">body{color: #525252;}"
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


                    Intent i = new Intent(ActivityVideoDetail.this, YoutubePlay.class);
                    i.putExtra("id", st_vid);
                    startActivity(i);

            }
        });


//        img_fav.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                List<Pojo> pojolist = db.getFavRow(vid);
//                if (pojolist.size() == 0) {
//
//                    db.AddtoFavorite(new Pojo(vid, video_id, videoname, videourl, videoduration, videocatname, videocatid, videodesc, videoimageurl));
//                    Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
//                    img_fav.setImageResource(R.drawable.ic_fav_toggle);
//
//                    interstitial = new InterstitialAd(getApplicationContext());
//                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//                    AdRequest adRequest = new AdRequest.Builder().build();
//                    interstitial.loadAd(adRequest);
//                    interstitial.setAdListener(new AdListener() {
//                        public void onAdLoaded() {
//                            if (interstitial.isLoaded()) {
//                                interstitial.show();
//                            }
//                        }
//                    });
//
//                } else {
//                    if (pojolist.get(0).getVId().equals(vid)) {
//
//                        db.RemoveFav(new Pojo(vid));
//                        Toast.makeText(getApplicationContext(), "Removed from Favorite", Toast.LENGTH_SHORT).show();
//                        img_fav.setImageResource(R.drawable.ic_fav_outline);
//                    }
//                }
//            }
//        });
    }

//    private class MyTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            return JsonUtils.getJSONString(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            progressBar.setVisibility(View.GONE);
//            coordinatorLayout.setVisibility(View.VISIBLE);
//
//            if (null == result || result.length() == 0) {
//                Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
//                coordinatorLayout.setVisibility(View.GONE);
//            } else {
//
//                try {
//                    JSONObject mainJson = new JSONObject(result);
//                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ITEM_ARRAY_NAME);
//                    JSONObject objJson = null;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        objJson = jsonArray.getJSONObject(i);
//
//                        ItemModelNewRelease objItem = new ItemModelNewRelease();
//
//                        objItem.setId(objJson.getString(JsonConfig.LATEST_ID));
//                        objItem.setCategoryId(objJson.getString(JsonConfig.LATEST_CATID));
//                        objItem.setVideoName(objJson.getString(JsonConfig.LATEST_VIDEO_NAME));
//                        objItem.setVideoUrl(objJson.getString(JsonConfig.LATEST_VIDEO_URL));
//                        objItem.setVideoId(objJson.getString(JsonConfig.LATEST_VIDEO_ID));
//                        objItem.setDuration(objJson.getString(JsonConfig.LATEST_VIDEO_DURATION));
//                        objItem.setCategoryName(objJson.getString(JsonConfig.LATEST_CAT_NAME));
//                        objItem.setDescription(objJson.getString(JsonConfig.LATEST_VIDEO_DESCRIPTION));
//                        objItem.setImageUrl(objJson.getString(JsonConfig.LATEST_IMAGE_URL));
//
//                        itemNewsList.add(objItem);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                setAdapterToListview();
//            }
//
//        }
//    }
//
//    public void setAdapterToListview() {
//
//        object = itemNewsList.get(0);
//
//        vid = object.getId();
//        video_id = object.getVideoId();
//        videoname = object.getVideoName();
//        videourl = object.getVideoUrl();
//        videoduration = object.getDuration();
//        videocatname = object.getCategoryName();
//        videocatid = object.getCategoryId();
//        videodesc = object.getDescription();
//        videoimageurl = object.getImageUrl();
//
//        title.setText(videoname);
//        category.setText(videocatname);
//        duration.setText(videoduration);
//
//        desc.setBackgroundColor(Color.parseColor("#ffffff"));
//        desc.setFocusableInTouchMode(false);
//        desc.setFocusable(false);
//        desc.getSettings().setDefaultTextEncodingName("UTF-8");
//
//        WebSettings webSettings = desc.getSettings();
//        Resources res = getResources();
//        int fontSize = res.getInteger(R.integer.font_size);
//        webSettings.setDefaultFontSize(fontSize);
//
//        String mimeType = "text/html; charset=UTF-8";
//        String encoding = "utf-8";
//        String htmlText = videodesc;
//
//        String text = "<html><head>"
//                + "<style type=\"text/css\">body{color: #525252;}"
//                + "</style></head>"
//                + "<body>"
//                + htmlText
//                + "</body></html>";
//
//        desc.loadData(text, mimeType, encoding);
//
//        if (object.getVideoId().equals("000q1w2")) {
//
//            Picasso.with(this).load(Config.SERVER_URL + "/upload/" + object.getImageUrl()).placeholder(R.drawable.ic_thumbnail).into(img_video, new Callback() {
//                @Override
//                public void onSuccess() {
//                    Bitmap bitmap = ((BitmapDrawable) img_video.getDrawable()).getBitmap();
//                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                        @Override
//                        public void onGenerated(Palette palette) {
//                        }
//                    });
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
//
//        } else {
//            Picasso.with(this).load(JsonConfig.YOUTUBE_IMAGE_FRONT + object.getVideoId() + JsonConfig.YOUTUBE_IMAGE_BACK).placeholder(R.drawable.ic_thumbnail).into(img_video, new Callback() {
//                @Override
//                public void onSuccess() {
//                    Bitmap bitmap = ((BitmapDrawable) img_video.getDrawable()).getBitmap();
//                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                        @Override
//                        public void onGenerated(Palette palette) {
//                        }
//                    });
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
//        }
//
//        List<Pojo> pojolist = db.getFavRow(vid);
//        if (pojolist.size() == 0) {
//            img_fav.setImageResource(R.drawable.ic_fav_outline);
//        } else {
//            if (pojolist.get(0).getVId().equals(vid)) {
//                img_fav.setImageResource(R.drawable.ic_fav_toggle);
//            }
//        }
//
//        img_video.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                VideoPlayId = object.getVideoId();
//                String VideoUrl = object.getVideoUrl();
//
//                if (VideoPlayId.equals("000q1w2")) {
//                    Intent lVideoIntent = new Intent(null, Uri.parse("file://" + VideoUrl), ActivityVideoDetail.this, VideoPlayerActivity.class);
//                    startActivity(lVideoIntent);
//                } else {
//                    Intent i = new Intent(ActivityVideoDetail.this, YoutubePlay.class);
//                    i.putExtra("id", VideoPlayId);
//                    startActivity(i);
//                }
//            }
//        });
//
//        img_fav.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                List<Pojo> pojolist = db.getFavRow(vid);
//                if (pojolist.size() == 0) {
//
//                    db.AddtoFavorite(new Pojo(vid, video_id, videoname, videourl, videoduration, videocatname, videocatid, videodesc, videoimageurl));
//                    Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
//                    img_fav.setImageResource(R.drawable.ic_fav_toggle);
//
//                    interstitial = new InterstitialAd(getApplicationContext());
//                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//                    AdRequest adRequest = new AdRequest.Builder().build();
//                    interstitial.loadAd(adRequest);
//                    interstitial.setAdListener(new AdListener() {
//                        public void onAdLoaded() {
//                            if (interstitial.isLoaded()) {
//                                interstitial.show();
//                            }
//                        }
//                    });
//
//                } else {
//                    if (pojolist.get(0).getVId().equals(vid)) {
//
//                        db.RemoveFav(new Pojo(vid));
//                        Toast.makeText(getApplicationContext(), "Removed from Favorite", Toast.LENGTH_SHORT).show();
//                        img_fav.setImageResource(R.drawable.ic_fav_outline);
//                    }
//                }
//            }
//        });
//
//    }

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

}
