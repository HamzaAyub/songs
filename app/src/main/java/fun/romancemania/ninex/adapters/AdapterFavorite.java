package fun.romancemania.ninex.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fun.romancemania.ninex.Config;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.cache.ImageLoader;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.models.Pojo;

import java.util.List;

public class AdapterFavorite extends BaseAdapter {

    LayoutInflater inflate;
    Activity activity;
    private List<Pojo> data;
    public ImageLoader imageLoader;

    public AdapterFavorite(List<Pojo> contactList, Activity activity, int columnWidth) {
        this.activity = activity;
        this.data = contactList;
        inflate = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class GroupItem {
        public ImageView image;
        public TextView name, time, category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rootView = null;
        final GroupItem holder = new GroupItem();
        rootView = inflate.inflate(R.layout.lsv_item_model_list_video, null);
        holder.image = (ImageView) rootView.findViewById(R.id.picture);
        holder.name = (TextView) rootView.findViewById(R.id.text);
     //   holder.time = (TextView) rootView.findViewById(R.id.second);
     //   holder.category = (TextView) rootView.findViewById(R.id.text_category);

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/NotoSans-Regular.ttf");
        holder.name.setTypeface(font);
      //  holder.time.setTypeface(font);
     //   holder.category.setTypeface(font);

        if (data.get(position).getVideoId().equals("000q1w2")) {
            imageLoader.DisplayImage(Config.SERVER_URL + "/upload/thumbs/" + data.get(position).getImageUrl(), holder.image);
            //Picasso.with(activity).load(Config.SERVER_URL + "/upload/thumbs/" + data.get(position).getImageUrl()).placeholder(R.drawable.ic_thumbnail).into(holder.image);

        } else {
            imageLoader.DisplayImage(JsonConfig.YOUTUBE_IMAGE_FRONT + data.get(position).getVideoId() + JsonConfig.YOUTUBE_SMALL_IMAGE_BACK, holder.image);
            //Picasso.with(activity).load(JsonConfig.YOUTUBE_IMAGE_FRONT + data.get(position).getVideoId() + JsonConfig.YOUTUBE_SMALL_IMAGE_BACK).placeholder(R.drawable.ic_thumbnail).into(holder.image);

        }

        holder.name.setText(data.get(position).getVideoName());
      //  holder.time.setText(data.get(position).getDuration());
      //  holder.category.setText(data.get(position).getCategoryName());

        return rootView;
    }

}

