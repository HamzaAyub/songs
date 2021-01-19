package fun.romancemania.ninex.fragments;

import java.util.ArrayList;
import java.util.List;

import fun.romancemania.ninex.R;
import fun.romancemania.ninex.activities.ActivityVideoDetail;
import fun.romancemania.ninex.adapters.AdapterFavorite;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.json.JsonUtils;
import fun.romancemania.ninex.models.Pojo;
import fun.romancemania.ninex.utilities.DatabaseHandler;
import fun.romancemania.ninex.utilities.DatabaseHandler.DatabaseManager;

import android.content.Intent;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentFavorite extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListView listview;
    DatabaseHandler db;
    private DatabaseManager dbManager;
    AdapterFavorite adapter;
    TextView txt_no;
    private int columnWidth;
    JsonUtils util;
    List<Pojo> allData;
    ArrayList<String> ArrayVideo, ArrayVideoCatName;
    ArrayList<String> ArrayVideoId, ArrayVideoCatId, ArrayVideoUrl, ArrayVideoName, ArrayVideoDuration, ArrayVideoDesc, ArrayImageUrl;
    String[] StringVideo, StringVideoCatName;
    String[] StringVideoId, StringVideoCatId, StringVideourl, StringVideoName, StringVideoDuration, StringVideoDesc, StringImageUrl;
    int textlength = 0;
    Pojo pojo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        setHasOptionsMenu(true);

        listview = (ListView) rootView.findViewById(R.id.lsv_favorite);
        txt_no = (TextView) rootView.findViewById(R.id.textView1);
        db = new DatabaseHandler(getActivity());
        dbManager = DatabaseManager.INSTANCE;
        dbManager.init(getActivity());
        util = new JsonUtils(getActivity());

        allData = db.getAllData();
        adapter = new AdapterFavorite(allData, getActivity(), columnWidth);
        listview.setAdapter(adapter);
        if (allData.size() == 0) {
            txt_no.setVisibility(View.VISIBLE);
        } else {
            txt_no.setVisibility(View.INVISIBLE);
        }

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                pojo = allData.get(position);
                int pos = Integer.parseInt(pojo.getVId());

                Intent intplay = new Intent(getActivity(), ActivityVideoDetail.class);
                intplay.putExtra("POSITION", pos);
                JsonConfig.VIDEO_ITEMIDD = pojo.getVId();

                startActivity(intplay);

            }
        });

        return rootView;
    }


    public void onDestroyView() {
        super.onDestroyView();
        //Log.e("OnDestroy", "called");
        if (!dbManager.isDatabaseClosed())
            dbManager.closeDatabase();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.e("OnPaused", "called");
        if (!dbManager.isDatabaseClosed())
            dbManager.closeDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e("OnResume", "called");
        //when back key pressed or go one tab to another we update the favorite item so put in resume
        allData = db.getAllData();
        adapter = new AdapterFavorite(allData, getActivity(), columnWidth);
        listview.setAdapter(adapter);
        if (allData.size() == 0) {
            txt_no.setVisibility(View.VISIBLE);
        } else {
            txt_no.setVisibility(View.INVISIBLE);
        }

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

        for (int j = 0; j < allData.size(); j++) {
            Pojo objAllBean = allData.get(j);

            ArrayVideo.add(objAllBean.getVideoId());
            StringVideo = ArrayVideo.toArray(StringVideo);

            ArrayVideoCatName.add(objAllBean.getCategoryName());
            StringVideoCatName = ArrayVideoCatName.toArray(StringVideoCatName);

            ArrayVideoId.add(String.valueOf(objAllBean.getVId()));
            StringVideoId = ArrayVideoId.toArray(StringVideoId);

            ArrayVideoCatId.add(String.valueOf(objAllBean.getCategoryId()));
            StringVideoCatId = ArrayVideoCatId.toArray(StringVideoCatId);

            ArrayVideoUrl.add(String.valueOf(objAllBean.getVideoUrl()));
            StringVideourl = ArrayVideoUrl.toArray(StringVideourl);

            ArrayVideoName.add(String.valueOf(objAllBean.getVideoName()));
            StringVideoName = ArrayVideoName.toArray(StringVideoName);

            ArrayVideoDuration.add(String.valueOf(objAllBean.getDuration()));
            StringVideoDuration = ArrayVideoDuration.toArray(StringVideoDuration);

            ArrayVideoDesc.add(objAllBean.getDescription());
            StringVideoDesc = ArrayVideoDesc.toArray(StringVideoDesc);

            ArrayImageUrl.add(objAllBean.getImageUrl());
            StringImageUrl = ArrayImageUrl.toArray(StringImageUrl);
        }
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
                allData.clear();

                for (int i = 0; i < StringVideoName.length; i++) {
                    if (textlength <= StringVideoName[i].length()) {
                    	if (StringVideoName[i].toLowerCase().contains(newText.toLowerCase())) {

                            Pojo objItem = new Pojo();

                            objItem.setVId(StringVideoId[i]);
                            objItem.setCategoryId(StringVideoCatId[i]);
                            objItem.setCategoryName(StringVideoCatName[i]);
                            objItem.setDescription(StringVideoDesc[i]);
                            objItem.setDuration(StringVideoDuration[i]);
                            objItem.setVideoId(StringVideo[i]);
                            objItem.setVideoName(StringVideoName[i]);
                            objItem.setVideoUrl(StringVideourl[i]);
                            objItem.setImageUrl(StringImageUrl[i]);

                            allData.add(objItem);

                        }
                    }
                }

                adapter = new AdapterFavorite(allData, getActivity(), columnWidth);
                listview.setAdapter(adapter);

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
