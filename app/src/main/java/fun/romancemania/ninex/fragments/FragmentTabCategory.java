package fun.romancemania.ninex.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import androidx.core.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.core.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import fun.romancemania.ninex.Config;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.activities.ActivityVideoCatList;
import fun.romancemania.ninex.adapters.AdapterCategory;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.json.JsonUtils;
import fun.romancemania.ninex.models.ItemModelCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentTabCategory extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    GridView listview;
    List<ItemModelCategory> listItem;
    AdapterCategory adapter;
    private ItemModelCategory object;
    ArrayList<String> ArrayCatid, ArrayCatName, ArrayCatImageUrl;
    String[] StringCatid, StringCatname, StringCatImageurl;
    int textlength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    ProgressBar progressBar;
    private InterstitialAd mInterstitial;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        listview = (GridView) rootView.findViewById(R.id.lsv_allphotos);
        listItem = new ArrayList<ItemModelCategory>();
        setHasOptionsMenu(true);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        ArrayCatid = new ArrayList<String>();
        ArrayCatImageUrl = new ArrayList<String>();
        ArrayCatName = new ArrayList<String>();

        StringCatid = new String[ArrayCatid.size()];
        StringCatname = new String[ArrayCatName.size()];
        StringCatImageurl = new String[ArrayCatImageUrl.size()];

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTask().execute(Config.SERVER_URL + "/api.php");
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
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
                        new RefreshTask().execute(Config.SERVER_URL + "/api.php");
                    }
                }, 3000);
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listview != null && listview.getChildCount() > 0) {
                    boolean firstItemVisible = listview.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listview.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                object = listItem.get(position);
                int Catid = object.getCategoryId();
                JsonConfig.CATEGORY_ID = object.getCategoryId();
                Log.e("cat_id", "" + Catid);
                JsonConfig.CATEGORY_TITLE = object.getCategoryName();

                if(object.getCategoryType().equals("keyword"))
                {
                    LoadingDialog();
                    mInterstitial = new InterstitialAd(getActivity());
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
                            Intent intcat = new Intent(getActivity(), ActivityVideoCatList.class);
                            startActivity(intcat);



                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            pDialog.dismiss();
                            Intent intcat = new Intent(getActivity(), ActivityVideoCatList.class);
                            startActivity(intcat);
                        }

                    });


                }else if(object.getCategoryType().equals("url"))
                {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(object.getCategoryLink())));
                }


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
                Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelCategory objItem = new ItemModelCategory();
                        objItem.setCategoryName(objJson.getString(JsonConfig.HOME_CATEGORY_NAME));
                        objItem.setCategoryId(objJson.getInt(JsonConfig.HOME_CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(JsonConfig.HOME_CATEGORY_IMAGE));
                        objItem.setCategoryType(objJson.getString(JsonConfig.HOME_CATEGORY_TYPE));
                        objItem.setCategoryLink(objJson.getString(JsonConfig.HOME_CATEGORY_URL));
                        listItem.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int j = 0; j < listItem.size(); j++) {
                    object = listItem.get(j);

                    ArrayCatid.add(String.valueOf(object.getCategoryId()));
                    StringCatid = ArrayCatid.toArray(StringCatid);

                    ArrayCatName.add(object.getCategoryName());
                    StringCatname = ArrayCatName.toArray(StringCatname);

                    ArrayCatImageUrl.add(object.getCategoryImageurl());
                    StringCatImageurl = ArrayCatImageUrl.toArray(StringCatImageurl);

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
                Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelCategory objItem = new ItemModelCategory();
                        objItem.setCategoryName(objJson.getString(JsonConfig.HOME_CATEGORY_NAME));
                        objItem.setCategoryId(objJson.getInt(JsonConfig.HOME_CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(JsonConfig.HOME_CATEGORY_IMAGE));
                        objItem.setCategoryType(objJson.getString(JsonConfig.HOME_CATEGORY_TYPE));
                        objItem.setCategoryLink(objJson.getString(JsonConfig.HOME_CATEGORY_URL));
                        listItem.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int j = 0; j < listItem.size(); j++) {
                    object = listItem.get(j);

                    ArrayCatid.add(String.valueOf(object.getCategoryId()));
                    StringCatid = ArrayCatid.toArray(StringCatid);

                    ArrayCatName.add(object.getCategoryName());
                    StringCatname = ArrayCatName.toArray(StringCatname);

                    ArrayCatImageUrl.add(object.getCategoryImageurl());
                    StringCatImageurl = ArrayCatImageUrl.toArray(StringCatImageurl);

                }
                setAdapterToListview();
            }
        }
    }

    public void setAdapterToListview() {
        adapter = new AdapterCategory(getActivity(), R.layout.lsv_item_model_category,
                listItem);
        listview.setAdapter(adapter);
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
                // TODO Auto-generated method stub
                textlength = newText.length();
                listItem.clear();

                for (int i = 0; i < StringCatname.length; i++) {
                    if (textlength <= StringCatname[i].length()) {
                    	if (StringCatname[i].toLowerCase().contains(newText.toLowerCase())) {
                            ItemModelCategory objItem = new ItemModelCategory();
                            objItem.setCategoryId(Integer.parseInt(StringCatid[i]));
                            objItem.setCategoryName(StringCatname[i]);
                            objItem.setCategoryImageurl(StringCatImageurl[i]);

                            listItem.add(objItem);
                        }
                    }
                }

                setAdapterToListview();

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }
        });


    }

    public void LoadingDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();

    }

}
