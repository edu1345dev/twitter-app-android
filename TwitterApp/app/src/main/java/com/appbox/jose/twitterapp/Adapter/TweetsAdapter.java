package com.appbox.jose.twitterapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbox.jose.twitterapp.Model.Tweeto;
import com.appbox.jose.twitterapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by edu_1 on 29/09/2016.
 */
public class TweetsAdapter extends ArrayAdapter<Tweeto> {

    private Context context;

    public TweetsAdapter(Context context, ArrayList<Tweeto> tweets) {
        super(context, 0, tweets);

        this.context = context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Tweeto tweeto = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_view, parent, false);
        }

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.user_photo);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_item_adapter_name);
        TextView tvText = (TextView) convertView.findViewById(R.id.tv_item_adapter_text);

        tvName.setText(tweeto.getName());
        tvText.setText(tweeto.getText());

        Picasso.with(context)
                .load(tweeto.getPhotoUrl())
                .fit()
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(ivPhoto);

        return convertView;
    }
}
