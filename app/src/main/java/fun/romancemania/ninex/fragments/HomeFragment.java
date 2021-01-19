package fun.romancemania.ninex.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentTransaction;
//import androidx.core.view.PagerAdapter;
//import androidx.core.view.ViewPager;
//import androidx.core.widget.DrawerLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import fun.romancemania.ninex.Config;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.adapters.HomeCategoryAdapter;
import fun.romancemania.ninex.adapters.HomeCategoryOtherAdapter;
import fun.romancemania.ninex.adapters.HomeVideoAdapter;
import fun.romancemania.ninex.json.ItemOffsetDecoration;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.json.JsonUtils;
import fun.romancemania.ninex.models.ItemModelCategory;
import fun.romancemania.ninex.models.ItemSlider;
import fun.romancemania.ninex.videoplay.YoutubePlay;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment {

    ViewPager viewPager;
    CircleIndicator circleIndicator;
    int currentCount = 0;
     RecyclerView recyclerView_Category, recyclerView_Video,recyclerView_CatOther;
    RelativeLayout relativeLayout_Main;
    ProgressBar progressBar;
    ArrayList<ItemSlider> mSliderList;
    ArrayList<ItemModelCategory> mListCategory;
    ArrayList<ItemModelCategory> mListCategoryOther;
    ArrayList<ItemModelCategory> mListVideo;
    ImagePagerAdapter adapter;
    ItemSlider itemSlider;
    HomeCategoryAdapter homeCategoryAdapter;
    HomeVideoAdapter homeVideoAdapter;
    HomeCategoryOtherAdapter homeCategoryOtherAdapter;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private DrawerLayout snckbar_RelativeLAyout;
    private Context context;
    private EditText editSerach;
    private Button btnSearch;
    private static final String TAG = "HTAG";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.app_name));

        mSliderList = new ArrayList<>();
        mListCategory = new ArrayList<>();
        mListVideo = new ArrayList<>();
        mListCategoryOther=new ArrayList<>();

        // search Code

//        editSerach = rootView.findViewById(R.id.edit_search_songs_layout);
//        btnSearch = rootView.findViewById(R.id.btn_search_songs_layout);
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchText = editSerach.getText().toString();
//                if (searchText.equals("")){
//                    Toast.makeText(getActivity(), "Type A Song Name", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onClick: Type A Song Name");
//                }
//                else {
//                    Log.d(TAG, "onClick: my text : " + searchText);
//                    Intent intent = new Intent(getActivity(), SearchListActivity.class);
//                    intent.putExtra("songName", searchText);
//                    startActivity(intent);
//                }
//
//            }
//        });



        // super class layout
        snckbar_RelativeLAyout = getActivity().findViewById(R.id.drawer_layout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        circleIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator_unselected_background);

        recyclerView_Category = (RecyclerView) rootView.findViewById(R.id.vertical_courses_list);
        recyclerView_Video = (RecyclerView) rootView.findViewById(R.id.vertical_courses_list2);
        recyclerView_CatOther = (RecyclerView) rootView.findViewById(R.id.vertical_courses_list3);
        relativeLayout_Main = (RelativeLayout) rootView.findViewById(R.id.main);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView_Category.setHasFixedSize(false);
        recyclerView_Category.setNestedScrollingEnabled(false);
        recyclerView_Category.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView_Category.addItemDecoration(itemDecoration);

        recyclerView_Video.setHasFixedSize(false);
        recyclerView_Video.setNestedScrollingEnabled(false);
        recyclerView_Video.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_Video.addItemDecoration(itemDecoration);

        recyclerView_CatOther.setHasFixedSize(false);
        recyclerView_CatOther.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView_CatOther.setLayoutManager(layoutManager);
        recyclerView_CatOther.addItemDecoration(itemDecoration);
        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTaskSlider().execute(Config.SERVER_URL+"/api.php"+ JsonConfig.SLIDER_URL);
            Log.e("HTAG",Config.SERVER_URL+JsonConfig.SLIDER_URL);
        } else {
            showToast(getString(R.string.failed_connect_network));
        }


        return rootView;
    }
    public class MyTaskSlider extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            relativeLayout_Main.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.INVISIBLE);
            relativeLayout_Main.setVisibility(View.VISIBLE);

            if (null == result || result.length() == 0) {
                showToast(getString(R.string.failed_connect_network));

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.SLIDER_ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemSlider objItem = new ItemSlider();
                        objItem.setSliderId(objJson.getString(JsonConfig.SLIDER_ID));
                        objItem.setSliderImageUrl(objJson.getString(JsonConfig.SLIDER_IMAGE));
                        objItem.setSliderLink(objJson.getString(JsonConfig.SLIDER_VIDEO_LINK));
                        mSliderList.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterToFeatured();
            }

        }
    }

    public void setAdapterToFeatured() {

        adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        autoPlay(viewPager);

        // last method to show only last videos

        setAdapterHomeLatest();


      /*  if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTaskHomeCategory().execute(Config.SERVER_URL+JsonConfig.FEATURE_CAT_URL);
        } else {
            showToast(getString(R.string.failed_connect_network));
        }*/

    }


    private void autoPlay(final ViewPager viewPager) {

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (adapter != null && viewPager.getAdapter().getCount() > 0) {
                        int position = currentCount % adapter.getCount();
                        currentCount++;
                        viewPager.setCurrentItem(position);
                        autoPlay(viewPager);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "auto scroll pager error.", e);
                }
            }
        }, 2500);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mSliderList.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.viewpager_slider_item, container, false);
            assert imageLayout != null;
            itemSlider = mSliderList.get(position);
            ImageView imageview = (ImageView) imageLayout.findViewById(R.id.imageView_viewitem);

            Picasso.with(getActivity()).load(Config.SERVER_URL +"/upload/slider/"+itemSlider.getSliderImageUrl()).into(imageview);

            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemSlider = mSliderList.get(position);
                    String id = itemSlider.getSliderLink();
                    if(id.isEmpty())
                    {
                        Toast.makeText(getActivity(),"No video",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i = new Intent(getActivity(), YoutubePlay.class);
                        i.putExtra("id", JsonUtils.getVideoId(id));
                        startActivity(i);
                    }


                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        Snackbar.make(snckbar_RelativeLAyout,msg,Snackbar.LENGTH_LONG).show();
    }

  /*  public class MyTaskHomeCategory extends AsyncTask<String, Void, String> {

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
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.SLIDER_ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelCategory objItem = new ItemModelCategory();

                        objItem.setCategoryId(objJson.getInt(JsonConfig.HOME_FEA_CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(JsonConfig.HOME_FEA_CATEGORY_IMAGE));
                        objItem.setCategoryName(objJson.getString(JsonConfig.HOME_FEA_CATEGORY_NAME));
                        objItem.setCategoryType(objJson.getString(JsonConfig.HOME_FEA_CATEGORY_TYPE));
                        objItem.setCategoryLink(objJson.getString(JsonConfig.HOME_FEA_CATEGORY_URL));
                        mListCategory.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterHomeCategory();
            }

        }
    }

    public void setAdapterHomeCategory() {

        homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), mListCategory);
        recyclerView_Category.setAdapter(homeCategoryAdapter);


        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTaskHomeLAtest().execute(Config.SERVER_URL + JsonConfig.SLIDER_CAT_URL);
        } else {
            Toast.makeText(getActivity(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
        }

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
                    //JSONObject mainJsonob = mainJson.getJSONObject(JsonConfig.SLIDER_ARRAY);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.SLIDER_ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelCategory objItem = new ItemModelCategory();

                        objItem.setCategoryId(objJson.getInt(JsonConfig.SLIDER_V_CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(JsonConfig.SLIDER_V_CATEGORY_IMAGE));
                        objItem.setCategoryName(objJson.getString(JsonConfig.SLIDER_V_CATEGORY_NAME));
                        objItem.setCategoryType(objJson.getString(JsonConfig.SLIDER_V_CATEGORY_TYPE));
                        objItem.setCategoryLink(objJson.getString(JsonConfig.SLIDER_V_CATEGORY_URL));
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

        homeVideoAdapter = new HomeVideoAdapter(getActivity(), mListVideo);
        recyclerView_Video.setAdapter(homeVideoAdapter);

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTaskHomeCategoryOther().execute(Config.SERVER_URL + "/api.php");
        } else {
            Toast.makeText(getActivity(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
        }
    }
    */
  public void setAdapterHomeLatest() {

      if (JsonUtils.isNetworkAvailable(getActivity())) {
          new MyTaskHomeCategoryOther().execute(Config.SERVER_URL + "/api.php");
      } else {
          Toast.makeText(getActivity(), "Failed Connect to Network!!", Toast.LENGTH_SHORT).show();
      }
  }


    public class MyTaskHomeCategoryOther extends AsyncTask<String, Void, String> {

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
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.SLIDER_ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemModelCategory objItem = new ItemModelCategory();

                        objItem.setCategoryId(objJson.getInt(JsonConfig.HOME_CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(JsonConfig.HOME_CATEGORY_IMAGE));
                        objItem.setCategoryName(objJson.getString(JsonConfig.HOME_CATEGORY_NAME));
                        objItem.setCategoryType(objJson.getString(JsonConfig.HOME_CATEGORY_TYPE));
                        objItem.setCategoryLink(objJson.getString(JsonConfig.HOME_CATEGORY_URL));
                        mListCategoryOther.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterHomeCategoryOther();
            }

        }
    }

    public void setAdapterHomeCategoryOther() {

        homeCategoryOtherAdapter = new HomeCategoryOtherAdapter(getActivity(), mListCategoryOther);
        recyclerView_CatOther.setAdapter(homeCategoryOtherAdapter);

    }

}
