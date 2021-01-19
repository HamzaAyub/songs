package fun.romancemania.ninex.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import androidx.core.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.core.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import fun.romancemania.ninex.Config;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.activities.ActivityVideoDetail;
import fun.romancemania.ninex.adapters.AdapterNewRelease;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.json.JsonUtils;
import fun.romancemania.ninex.models.ItemModelNewRelease;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentTabNewRelease extends Fragment {

    ListView listView;
    List<ItemModelNewRelease> listItem;
    AdapterNewRelease adapter;
    ArrayList<String> ArrayVideo, ArrayVideoCatName;
    ArrayList<String> ArrayVideoId, ArrayVideoCatId, ArrayVideoUrl, ArrayVideoName, ArrayVideoDuration, ArrayVideoDesc, ArrayImageUrl;
    String[] StringVideo, StringVideoCatName;
    String[] StringVideoId, StringVideoCatId, StringVideourl, StringVideoName, StringVideoDuration, StringVideoDesc, StringImageUrl;
    private ItemModelNewRelease object;
    private int columnWidth;
    JsonUtils util;
    int textlength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    ProgressBar progressBar;
    private InterstitialAd interstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_release, container, false);

        setHasOptionsMenu(true);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        listView = (ListView) rootView.findViewById(R.id.lsv_latest);
        listItem = new ArrayList<ItemModelNewRelease>();
        ArrayVideo = new ArrayList<String>();
        ArrayVideoCatName = new ArrayList<String>();
        ArrayVideoCatId = new ArrayList<String>();
        ArrayVideoId = new ArrayList<String>();
        ArrayVideoName = new ArrayList<String>();
        ArrayVideoUrl = new ArrayList<String>();
        ArrayVideoDuration = new ArrayList<String>();
        ArrayVideoDesc = new ArrayList<String>();
        ArrayImageUrl = new ArrayList<String>();

        StringVideo = new String[ArrayVideo.size()];
        StringVideoCatName = new String[ArrayVideoCatName.size()];
        StringVideoId = new String[ArrayVideoId.size()];
        StringVideoCatId = new String[ArrayVideoCatId.size()];
        StringVideourl = new String[ArrayVideoUrl.size()];
        StringVideoName = new String[ArrayVideoName.size()];
        StringVideoDuration = new String[ArrayVideoDuration.size()];
        StringVideoDesc = new String[ArrayVideoDesc.size()];
        StringImageUrl = new String[ArrayImageUrl.size()];

        util = new JsonUtils(getActivity());

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTask().execute(Config.SERVER_URL + "/api.php?latest=50");
        } else {
            Toast.makeText(getActivity(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
        }

        // Using to refresh webpage when user swipes the screen
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.clear();
                        new RefreshTask().execute(Config.SERVER_URL + "/api.php?latest=50");
                    }
                }, 3000);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                object = listItem.get(position);
                int pos = Integer.parseInt(object.getId());

                Intent intplay = new Intent(getActivity(), ActivityVideoDetail.class);
                intplay.putExtra("POSITION", pos);
                JsonConfig.VIDEO_ITEMIDD = object.getId();
                startActivity(intplay);

                interstitialAd = new InterstitialAd(getActivity());
                interstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
                AdRequest adRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(adRequest);
                interstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        if (interstitialAd.isLoaded()) {
                            interstitialAd.show();
                        }
                    }
                });
            }
        });

        return rootView;
    }


    private class MyTask extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
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
                Toast.makeText(getActivity(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.LATEST_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelNewRelease objItem = new ItemModelNewRelease();

                        objItem.setId(objJson.getString(JsonConfig.LATEST_ID));
                        objItem.setCategoryId(objJson.getString(JsonConfig.LATEST_CATID));
                        objItem.setCategoryName(objJson.getString(JsonConfig.LATEST_CAT_NAME));
                        objItem.setVideoUrl(objJson.getString(JsonConfig.LATEST_VIDEO_URL));
                        objItem.setVideoId(objJson.getString(JsonConfig.LATEST_VIDEO_ID));
                        objItem.setVideoName(objJson.getString(JsonConfig.LATEST_VIDEO_NAME));
                        objItem.setDuration(objJson.getString(JsonConfig.LATEST_VIDEO_DURATION));
                        objItem.setDescription(objJson.getString(JsonConfig.LATEST_VIDEO_DESCRIPTION));
                        objItem.setImageUrl(objJson.getString(JsonConfig.LATEST_IMAGE_URL));

                        listItem.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < listItem.size(); j++) {

                    object = listItem.get(j);

                    ArrayVideo.add(object.getVideoId());
                    StringVideo = ArrayVideo.toArray(StringVideo);

                    ArrayVideoCatName.add(object.getCategoryName());
                    StringVideoCatName = ArrayVideoCatName.toArray(StringVideoCatName);

                    ArrayVideoId.add(String.valueOf(object.getId()));
                    StringVideoId = ArrayVideoId.toArray(StringVideoId);

                    ArrayVideoCatId.add(String.valueOf(object.getCategoryId()));
                    StringVideoCatId = ArrayVideoCatId.toArray(StringVideoCatId);

                    ArrayVideoUrl.add(String.valueOf(object.getVideoUrl()));
                    StringVideourl = ArrayVideoUrl.toArray(StringVideourl);

                    ArrayVideoName.add(String.valueOf(object.getVideoName()));
                    StringVideoName = ArrayVideoName.toArray(StringVideoName);

                    ArrayVideoDuration.add(String.valueOf(object.getDuration()));
                    StringVideoDuration = ArrayVideoDuration.toArray(StringVideoDuration);

                    ArrayVideoDesc.add(object.getDescription());
                    StringVideoDesc = ArrayVideoDesc.toArray(StringVideoDesc);


                    ArrayImageUrl.add(object.getImageUrl());
                    StringImageUrl = ArrayImageUrl.toArray(StringImageUrl);

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
                Toast.makeText(getActivity(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.LATEST_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelNewRelease objItem = new ItemModelNewRelease();

                        objItem.setId(objJson.getString(JsonConfig.LATEST_ID));
                        objItem.setCategoryId(objJson.getString(JsonConfig.LATEST_CATID));
                        objItem.setCategoryName(objJson.getString(JsonConfig.LATEST_CAT_NAME));
                        objItem.setVideoUrl(objJson.getString(JsonConfig.LATEST_VIDEO_URL));
                        objItem.setVideoId(objJson.getString(JsonConfig.LATEST_VIDEO_ID));
                        objItem.setVideoName(objJson.getString(JsonConfig.LATEST_VIDEO_NAME));
                        objItem.setDuration(objJson.getString(JsonConfig.LATEST_VIDEO_DURATION));
                        objItem.setDescription(objJson.getString(JsonConfig.LATEST_VIDEO_DESCRIPTION));
                        objItem.setImageUrl(objJson.getString(JsonConfig.LATEST_IMAGE_URL));

                        listItem.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < listItem.size(); j++) {

                    object = listItem.get(j);

                    ArrayVideo.add(object.getVideoId());
                    StringVideo = ArrayVideo.toArray(StringVideo);

                    ArrayVideoCatName.add(object.getCategoryName());
                    StringVideoCatName = ArrayVideoCatName.toArray(StringVideoCatName);

                    ArrayVideoId.add(String.valueOf(object.getId()));
                    StringVideoId = ArrayVideoId.toArray(StringVideoId);

                    ArrayVideoCatId.add(String.valueOf(object.getCategoryId()));
                    StringVideoCatId = ArrayVideoCatId.toArray(StringVideoCatId);

                    ArrayVideoUrl.add(String.valueOf(object.getVideoUrl()));
                    StringVideourl = ArrayVideoUrl.toArray(StringVideourl);

                    ArrayVideoName.add(String.valueOf(object.getVideoName()));
                    StringVideoName = ArrayVideoName.toArray(StringVideoName);

                    ArrayVideoDuration.add(String.valueOf(object.getDuration()));
                    StringVideoDuration = ArrayVideoDuration.toArray(StringVideoDuration);

                    ArrayVideoDesc.add(object.getDescription());
                    StringVideoDesc = ArrayVideoDesc.toArray(StringVideoDesc);


                    ArrayImageUrl.add(object.getImageUrl());
                    StringImageUrl = ArrayImageUrl.toArray(StringImageUrl);

                }
                setAdapterToListview();
            }
        }
    }

    public void setAdapterToListview() {
        adapter = new AdapterNewRelease(getActivity(), R.layout.lsv_item_model_list_video,
                listItem, columnWidth);
        listView.setAdapter(adapter);
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        final SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.search));

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
                listItem.clear();

                for (int i = 0; i < StringVideoName.length; i++) {
                    if (textlength <= StringVideoName[i].length()) {
                    	if (StringVideoName[i].toLowerCase().contains(newText.toLowerCase())) {

                            ItemModelNewRelease objItem = new ItemModelNewRelease();

                            objItem.setId(StringVideoId[i]);
                            objItem.setCategoryId(StringVideoCatId[i]);
                            objItem.setCategoryName(StringVideoCatName[i]);
                            objItem.setDescription(StringVideoDesc[i]);
                            objItem.setDuration(StringVideoDuration[i]);
                            objItem.setVideoId(StringVideo[i]);
                            objItem.setVideoName(StringVideoName[i]);
                            objItem.setVideoUrl(StringVideourl[i]);
                            objItem.setImageUrl(StringImageUrl[i]);
                            listItem.add(objItem);

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
    }

}
