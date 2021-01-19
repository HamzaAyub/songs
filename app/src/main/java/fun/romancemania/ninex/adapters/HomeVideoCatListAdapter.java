package fun.romancemania.ninex.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.activities.ActivityVideoDetailNew;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.models.ItemModelVideoByCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeVideoCatListAdapter extends RecyclerView.Adapter<HomeVideoCatListAdapter.ItemRowHolder> {

    private ArrayList<ItemModelVideoByCategory> dataList;
    private Context mContext;
    private InterstitialAd mInterstitial;
    ProgressDialog pDialog;

    public HomeVideoCatListAdapter(Context context, ArrayList<ItemModelVideoByCategory> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_home_cat_list, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemModelVideoByCategory singleItem = dataList.get(position);

        Picasso.with(mContext).load(JsonConfig.YOUTUBE_IMAGE_FRONT + singleItem.getVideoId() + JsonConfig.YOUTUBE_SMALL_IMAGE_BACK).placeholder(R.drawable.ic_thumbnail).into(holder.image);
        holder.text.setText(singleItem.getVideoName());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(mContext, ActivityVideoDetailNew.class);
//                intent.putExtra("TITLE", singleItem.getVideoName());
//                intent.putExtra("LINK", singleItem.getVideoId());
//                intent.putExtra("IMAGE", singleItem.getImageUrl());
//                intent.putExtra("DESC", singleItem.getDescription());
//                mContext.startActivity(intent);


                Intent intent = new Intent(mContext, ActivityVideoDetailNew.class);
                intent.putExtra("TITLE", singleItem.getVideoName());
                intent.putExtra("LINK", singleItem.getVideoId());
                intent.putExtra("IMAGE", singleItem.getImageUrl());
                intent.putExtra("DESC", singleItem.getDescription());
                mContext.startActivity(intent);

//                LoadingDialog();
//                mInterstitial = new InterstitialAd(mContext);
//                mInterstitial.setAdUnitId(mContext.getResources().getString(R.string.admob_interstitial_id));
//                mInterstitial.loadAd(new AdRequest.Builder().build());
//
//                mInterstitial.setAdListener(new AdListener() {
//                    @Override
//                    public void onAdLoaded() {
//                        // TODO Auto-generated method stub
//                        super.onAdLoaded();
//                        pDialog.dismiss();
//                        if (mInterstitial.isLoaded()) {
//                            mInterstitial.show();
//                        }
//                    }
//
//                    public void onAdClosed() {
//                        Intent intent = new Intent(mContext, ActivityVideoDetailNew.class);
//                        intent.putExtra("TITLE", singleItem.getVideoName());
//                        intent.putExtra("LINK", singleItem.getVideoId());
//                        intent.putExtra("IMAGE", singleItem.getImageUrl());
//                        intent.putExtra("DESC", singleItem.getDescription());
//                        mContext.startActivity(intent);
//
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        pDialog.dismiss();
//                        Intent intent = new Intent(mContext, ActivityVideoDetailNew.class);
//                        intent.putExtra("TITLE", singleItem.getVideoName());
//                        intent.putExtra("LINK", singleItem.getVideoId());
//                        intent.putExtra("IMAGE", singleItem.getImageUrl());
//                        intent.putExtra("DESC", singleItem.getDescription());
//                        mContext.startActivity(intent);
//                    }
//
//                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
    public void LoadingDialog() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();

    }
}
