package fun.romancemania.ninex.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fun.romancemania.ninex.Config;
import fun.romancemania.ninex.R;
import fun.romancemania.ninex.cache.ImageLoader;
import fun.romancemania.ninex.json.JsonConfig;
import fun.romancemania.ninex.models.ItemModelVideoByCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterVideoLatest extends ArrayAdapter<ItemModelVideoByCategory> {

    private Activity activity;
    private List<ItemModelVideoByCategory> item;
    ItemModelVideoByCategory object;
    private int row;
    public ImageLoader imageLoader;

    public AdapterVideoLatest(Activity act, int resource, List<ItemModelVideoByCategory> arrayList, int columnWidth) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.item = arrayList;
        imageLoader = new ImageLoader(activity);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((item == null) || ((position + 1) > item.size()))
            return view;

        object = item.get(position);


        holder.image = (ImageView) view.findViewById(R.id.picture);
        holder.name = (TextView) view.findViewById(R.id.text);
       /// holder.time = (TextView) view.findViewById(R.id.second);
       // holder.category = (TextView) view.findViewById(R.id.text_category);

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/NotoSans-Regular.ttf");
        holder.name.setTypeface(font);
      //  holder.time.setTypeface(font);
      //  holder.category.setTypeface(font);

        if (object.getVideoId().equals("000q1w2")) {
            //imageLoader.DisplayImage(Config.SERVER_URL + "/upload/thumbs/" + object.getImageUrl(), holder.image);
            Picasso.with(getContext()).load(Config.SERVER_URL + "/upload/" +
                    object.getImageUrl()).placeholder(R.drawable.ic_thumbnail).into(holder.image);

        } else {
            //imageLoader.DisplayImage(JsonConfig.YOUTUBE_IMAGE_FRONT + object.getVideoId() + JsonConfig.YOUTUBE_SMALL_IMAGE_BACK, holder.image);
            Picasso.with(getContext()).load(JsonConfig.YOUTUBE_IMAGE_FRONT + object.getVideoId() + JsonConfig.YOUTUBE_IMAGE_BACK).placeholder(R.drawable.ic_thumbnail).into(holder.image);

        }

        holder.name.setText(object.getVideoName());
      //  holder.time.setText(object.getDuration());
       // holder.category.setText(object.getCategoryName());

        return view;

    }

    public class ViewHolder {

        public ImageView image;
        public TextView name, time, category;

    }
}
