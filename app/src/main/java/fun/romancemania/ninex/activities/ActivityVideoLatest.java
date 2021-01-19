package fun.romancemania.ninex.activities;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import fun.romancemania.ninex.Config;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.adapters.HomeVideoCatListAdapter;
import fun.romancemania.ninex.json.ItemOffsetDecoration;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.json.JsonUtils;
import fun.romancemania.ninex.models.ItemModelVideoByCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityVideoLatest extends AppCompatActivity {

	RecyclerView recyclerView_Video;
	ProgressBar progressBar;
	ArrayList<ItemModelVideoByCategory> mListVideo;
	HomeVideoCatListAdapter homeVideoAdapter;
	private AdView mAdView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_latest);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mListVideo=new ArrayList<>();

 		recyclerView_Video = (RecyclerView) findViewById(R.id.vertical_courses_list2);
 		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityVideoLatest.this, R.dimen.item_offset);

		recyclerView_Video.setHasFixedSize(false);
		recyclerView_Video.setNestedScrollingEnabled(false);
		recyclerView_Video.setLayoutManager(new GridLayoutManager(ActivityVideoLatest.this, 2));
		recyclerView_Video.addItemDecoration(itemDecoration);

		if (JsonUtils.isNetworkAvailable(ActivityVideoLatest.this)) {
			new MyTaskHomeLAtest().execute(Config.SERVER_URL + "/api.php?latest_youtube");
			Log.e("latset",""+Config.SERVER_URL + "/api.php?latest_youtube");
		} else {
			Toast.makeText(ActivityVideoLatest.this, "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
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
	}

	public class MyTaskHomeLAtest extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);


			if (null == result || result.length() == 0) {
				showToast(getString(R.string.failed_connect_network));

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONObject mainJsonob = mainJson.getJSONObject(JsonConfig.SLIDER_ARRAY);
					JSONArray jsonArray = mainJsonob.getJSONArray("play_list");
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);

						ItemModelVideoByCategory objItem = new ItemModelVideoByCategory();

						objItem.setVideoId(objJson.getString(JsonConfig.HOME_LATEST_PLAYID));
						objItem.setVideoName(objJson.getString(JsonConfig.HOME_LATEST_TITLE));
						objItem.setDuration(objJson.getString(JsonConfig.HOME_LATEST_TIME));
						objItem.setDescription(objJson.getString(JsonConfig.HOME_LATEST_DESC));
						objItem.setImageUrl(objJson.getString(JsonConfig.HOME_LATEST_IMAGE));
						mListVideo.add(objItem);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setAdapterHomeLatest();
			}

		}
	}

	public void setAdapterHomeLatest() {

		homeVideoAdapter = new HomeVideoCatListAdapter(ActivityVideoLatest.this, mListVideo);
		recyclerView_Video.setAdapter(homeVideoAdapter);
	}
	public void showToast(String msg) {
		Toast.makeText(ActivityVideoLatest.this, msg, Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
