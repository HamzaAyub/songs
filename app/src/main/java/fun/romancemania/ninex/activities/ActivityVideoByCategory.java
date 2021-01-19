package fun.romancemania.ninex.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.core.widget.SwipeRefreshLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import fun.romancemania.ninex.Config;

import fun.romancemania.ninex.R;
import fun.romancemania.ninex.adapters.AdapterVideoByCategory;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.json.JsonUtils;
import fun.romancemania.ninex.models.ItemModelVideoByCategory;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityVideoByCategory extends AppCompatActivity {

	ListView lsv_cat;
	List<ItemModelVideoByCategory> arrayOfLatestVideo;
	AdapterVideoByCategory objAdapter;
	ArrayList<String> allListVideo, allListVideoCatName;
	ArrayList<String> allListVideoId, allListVideoCatId, allListVideoUrl, allListVideoName, allListVideoDuration,
			allListVideoDesc, allListImageUrl;
	String[] allArrayVideo, allArrayVideoCatName;
	String[] allArrayVideoId, allArrayVideoCatId, allArrayVideourl, allArrayVideoName, allArrayVideoDuration,
			allArrayVideoDesc, allArrayImageUrl;
	private ItemModelVideoByCategory objAllBean;
	private int columnWidth;
	JsonUtils util;
	int textlength = 0;
	SwipeRefreshLayout swipeRefreshLayout = null;
	ProgressBar progressBar;
	private AdView mAdView;
	private InterstitialAd mInterstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_by_category);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

		//show admob banner ad
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

		setTitle(JsonConfig.CATEGORY_TITLE);
		lsv_cat = (ListView) findViewById(R.id.lsv_cat_item);
		arrayOfLatestVideo = new ArrayList<ItemModelVideoByCategory>();
		allListVideo = new ArrayList<String>();
		allListVideoCatName = new ArrayList<String>();
		allListVideoCatId = new ArrayList<String>();
		allListVideoId = new ArrayList<String>();
		allListVideoName = new ArrayList<String>();
		allListVideoUrl = new ArrayList<String>();
		allListVideoDuration = new ArrayList<String>();
		allListVideoDesc = new ArrayList<String>();
		allListImageUrl = new ArrayList<String>();

		allArrayVideo = new String[allListVideo.size()];
		allArrayVideoCatName = new String[allListVideoCatName.size()];
		allArrayVideoId = new String[allListVideoId.size()];
		allArrayVideoCatId = new String[allListVideoCatId.size()];
		allArrayVideourl = new String[allListVideoUrl.size()];
		allArrayVideoName = new String[allListVideoName.size()];
		allArrayVideoDuration = new String[allListVideoDuration.size()];
		allArrayVideoDesc = new String[allListVideoDesc.size()];
		allArrayImageUrl = new String[allListImageUrl.size()];

		util = new JsonUtils(getApplicationContext());

		if (JsonUtils.isNetworkAvailable(ActivityVideoByCategory.this)) {
			new MyTask().execute(Config.SERVER_URL + "/api.php?cat_id=" + JsonConfig.CATEGORY_ID);
		} else {
			Toast.makeText(getApplicationContext(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
		}

		// Using to refresh webpage when user swipes the screen
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						swipeRefreshLayout.setRefreshing(false);
						objAdapter.clear();
						new RefreshTask().execute(Config.SERVER_URL + "/api.php?cat_id=" + JsonConfig.CATEGORY_ID);
					}
				}, 3000);
			}
		});

		lsv_cat.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (lsv_cat != null && lsv_cat.getChildCount() > 0) {
					boolean firstItemVisible = lsv_cat.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = lsv_cat.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeRefreshLayout.setEnabled(enable);
			}
		});

		lsv_cat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				objAllBean = arrayOfLatestVideo.get(position);
				int pos = Integer.parseInt(objAllBean.getId());

				mInterstitial = new InterstitialAd(ActivityVideoByCategory.this);
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
				});

				Intent intplay = new Intent(getApplicationContext(), ActivityVideoDetail.class);
				intplay.putExtra("POSITION", pos);
				JsonConfig.VIDEO_ITEMIDD = objAllBean.getId();

				startActivity(intplay);

			}
		});

	}

	private class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ActivityVideoByCategory.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				Toast.makeText(getApplicationContext(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ITEM_ARRAY_NAME);
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);

						ItemModelVideoByCategory objItem = new ItemModelVideoByCategory();

						objItem.setId(objJson.getString(JsonConfig.CATEGORY_ITEM_ID));
						objItem.setCategoryId(objJson.getString(JsonConfig.CATEGORY_ITEM_CATID));
						objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_ITEM_CAT_NAME));
						objItem.setVideoUrl(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_URL));
						objItem.setVideoId(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_ID));
						objItem.setVideoName(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_NAME));
						objItem.setDuration(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_DURATION));
						objItem.setDescription(objJson.getString(JsonConfig.LATEST_VIDEO_DESCRIPTION));
						objItem.setImageUrl(objJson.getString(JsonConfig.LATEST_IMAGE_URL));

						arrayOfLatestVideo.add(objItem);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				for (int j = 0; j < arrayOfLatestVideo.size(); j++) {

					objAllBean = arrayOfLatestVideo.get(j);

					allListVideo.add(objAllBean.getVideoId());
					allArrayVideo = allListVideo.toArray(allArrayVideo);

					allListVideoCatName.add(objAllBean.getCategoryName());
					allArrayVideoCatName = allListVideoCatName.toArray(allArrayVideoCatName);

					allListVideoId.add(String.valueOf(objAllBean.getId()));
					allArrayVideoId = allListVideoId.toArray(allArrayVideoId);

					allListVideoCatId.add(String.valueOf(objAllBean.getCategoryId()));
					allArrayVideoCatId = allListVideoCatId.toArray(allArrayVideoCatId);

					allListVideoUrl.add(String.valueOf(objAllBean.getVideoUrl()));
					allArrayVideourl = allListVideoUrl.toArray(allArrayVideourl);

					allListVideoName.add(String.valueOf(objAllBean.getVideoName()));
					allArrayVideoName = allListVideoName.toArray(allArrayVideoName);

					allListVideoDuration.add(String.valueOf(objAllBean.getDuration()));
					allArrayVideoDuration = allListVideoDuration.toArray(allArrayVideoDuration);

					allListVideoDesc.add(objAllBean.getDescription());
					allArrayVideoDesc = allListVideoDesc.toArray(allArrayVideoDesc);

					allListImageUrl.add(objAllBean.getImageUrl());
					allArrayImageUrl = allListImageUrl.toArray(allArrayImageUrl);
				}
				setAdapterToListview();
			}
		}
	}

	private class RefreshTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			progressBar.setVisibility(View.GONE);

			if (null == result || result.length() == 0) {
				Toast.makeText(getApplicationContext(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ITEM_ARRAY_NAME);
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);

						ItemModelVideoByCategory objItem = new ItemModelVideoByCategory();

						objItem.setId(objJson.getString(JsonConfig.CATEGORY_ITEM_ID));
						objItem.setCategoryId(objJson.getString(JsonConfig.CATEGORY_ITEM_CATID));
						objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_ITEM_CAT_NAME));
						objItem.setVideoUrl(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_URL));
						objItem.setVideoId(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_ID));
						objItem.setVideoName(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_NAME));
						objItem.setDuration(objJson.getString(JsonConfig.CATEGORY_ITEM_VIDEO_DURATION));
						objItem.setDescription(objJson.getString(JsonConfig.LATEST_VIDEO_DESCRIPTION));
						objItem.setImageUrl(objJson.getString(JsonConfig.LATEST_IMAGE_URL));

						arrayOfLatestVideo.add(objItem);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				for (int j = 0; j < arrayOfLatestVideo.size(); j++) {

					objAllBean = arrayOfLatestVideo.get(j);

					allListVideo.add(objAllBean.getVideoId());
					allArrayVideo = allListVideo.toArray(allArrayVideo);

					allListVideoCatName.add(objAllBean.getCategoryName());
					allArrayVideoCatName = allListVideoCatName.toArray(allArrayVideoCatName);

					allListVideoId.add(String.valueOf(objAllBean.getId()));
					allArrayVideoId = allListVideoId.toArray(allArrayVideoId);

					allListVideoCatId.add(String.valueOf(objAllBean.getCategoryId()));
					allArrayVideoCatId = allListVideoCatId.toArray(allArrayVideoCatId);

					allListVideoUrl.add(String.valueOf(objAllBean.getVideoUrl()));
					allArrayVideourl = allListVideoUrl.toArray(allArrayVideourl);

					allListVideoName.add(String.valueOf(objAllBean.getVideoName()));
					allArrayVideoName = allListVideoName.toArray(allArrayVideoName);

					allListVideoDuration.add(String.valueOf(objAllBean.getDuration()));
					allArrayVideoDuration = allListVideoDuration.toArray(allArrayVideoDuration);

					allListVideoDesc.add(objAllBean.getDescription());
					allArrayVideoDesc = allListVideoDesc.toArray(allArrayVideoDesc);

					allListImageUrl.add(objAllBean.getImageUrl());
					allArrayImageUrl = allListImageUrl.toArray(allArrayImageUrl);
				}
				setAdapterToListview();
			}
		}
	}

	public void setAdapterToListview() {
		objAdapter = new AdapterVideoByCategory(ActivityVideoByCategory.this, R.layout.lsv_item_model_list_video,
				arrayOfLatestVideo, columnWidth);
		lsv_cat.setAdapter(objAdapter);
	}

	public void showToast(String msg) {
		Toast.makeText(ActivityVideoByCategory.this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);

		final SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(menu.findItem(R.id.search));

		final MenuItem searchMenuItem = menu.findItem(R.id.search);
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					searchMenuItem.collapseActionView();
					searchView.setQuery("", false);
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {

				textlength = newText.length();
				arrayOfLatestVideo.clear();

				for (int i = 0; i < allArrayVideoName.length; i++) {
					if (textlength <= allArrayVideoName[i].length()) {
						if (allArrayVideoName[i].toLowerCase().contains(newText.toLowerCase())) {

							ItemModelVideoByCategory objItem = new ItemModelVideoByCategory();
							objItem.setId(allArrayVideoId[i]);
							objItem.setCategoryId(allArrayVideoCatId[i]);
							objItem.setCategoryName(allArrayVideoCatName[i]);
							objItem.setDescription(allArrayVideoDesc[i]);
							objItem.setDuration(allArrayVideoDuration[i]);
							objItem.setVideoId(allArrayVideo[i]);
							objItem.setVideoName(allArrayVideoName[i]);
							objItem.setVideoUrl(allArrayVideourl[i]);
							objItem.setImageUrl(allArrayImageUrl[i]);

							arrayOfLatestVideo.add(objItem);

						}
					}
				}

				setAdapterToListview();
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// Do something
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
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

	@Override
	protected void onPause() {
		mAdView.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.resume();
	}

	@Override
	protected void onDestroy() {
		mAdView.destroy();
		super.onDestroy();
	}

}
