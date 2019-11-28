package edu.asu.mcgroup27.emotrack.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.ui.home.HomeFragment;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class CustomAdapter  extends BaseAdapter {
    private final String TAG = "CustomAdapter";

    private Activity activity;
    private ArrayList<DisplayContent> data;
    private static LayoutInflater inflater;
    private Context context;
    int width, height;

    public CustomAdapter(HomeFragment homeFragment,
                         ArrayList<DisplayContent> friendlist, Context context) {
        super();

        this.activity = homeFragment.getActivity();
        this.data = friendlist;
        this.context = context;

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        Log.v(TAG,"<Suprateem>getView");

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        if (width < 800)
            width = (int) (width * 0.25);
        else
            width = (int) (width * 0.2);
        height = (int) (width * 0.8);

        DisplayContent item = new DisplayContent();
        item = data.get(position);

        Context adapterContext = CustomAdapter.inflater.getContext();

        LinearLayout row = new LinearLayout(adapterContext);
        row.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(adapterContext);
        image.setMaxWidth(R.dimen.list_image_size);
        image.setMaxHeight(R.dimen.list_image_size);
        LinearLayout mediaData = new LinearLayout(adapterContext);
        TextView name = new TextView(adapterContext);
        TextView length = new TextView(adapterContext);

        /*if (SymphonyMainActivity.selectedTab == 10) {
            CheckBox cb = new CheckBox(adapterContext);
            cb.setClickable(true);
            row.addView(cb);
        }*/

        /*if (item.getThmb() != null) {
            Log.v(TAG,"<Suprateem>thumb is not null");
            image.setImageBitmap(item.getThmb());
            image.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            row.addView(image);
        } else {
            image.setImageResource(R.drawable.ic_menu_share);
            image.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            row.addView(image);
        }*/
        Glide.with(context)
                .load(item.getThmb())
                .centerCrop()
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        row.addView(image);
        mediaData.setOrientation(LinearLayout.VERTICAL);

        name.setSingleLine(true);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        name.setText(item.getName());
        mediaData.addView(name);

        mediaData.addView(length);

        row.addView(mediaData);

        /*
         * ImageView playlistIcon = new ImageView(adapterContext);
         * playlistIcon.setImageResource(R.drawable.playlist_icon); if (width <
         * 800) width = (int) (width * 0.25); else width = (int) (width * 0.2);
         * height = (int) (width * 0.8); playlistIcon.setLayoutParams(new
         * LayoutParams(width, height));
         * playlistIcon.setScaleType(ScaleType.FIT_XY);
         * row.addView(playlistIcon);
         *
         * LinearLayout playlistData = new LinearLayout(adapterContext);
         * playlistData.setOrientation(LinearLayout.VERTICAL);
         *
         * TextView playlistName = new TextView(adapterContext);
         * playlistName.setSingleLine(true);
         * playlistName.setEllipsize(TruncateAt.MARQUEE);
         * playlistName.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
         * playlistName.setText(item.getName());
         * playlistData.addView(playlistName);
         *
         * TextView songs_count = new TextView(adapterContext);
         * songs_count.setTypeface(Typeface.MONOSPACE, Typeface.BOLD); if
         * (item.getDuration() != null) { songs_count.setText(item.getDuration()
         * + " Songs"); playlistData.addView(songs_count); }
         * row.addView(playlistData);
         */
        return row;

    }
}
