package com.appbox.jose.twitterapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbox.jose.twitterapp.Interface.SearchTermClickInterface;
import com.appbox.jose.twitterapp.Model.Tweeto;
import com.appbox.jose.twitterapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by edu_1 on 29/09/2016.
 */
public class SearchTermAdapter extends ArrayAdapter<String> {

    private Context context;
    private SearchTermClickInterface searchTermClickInterface;

    public SearchTermAdapter(Context context, ArrayList<String> search, SearchTermClickInterface searchTermClickInterface) {
        super(context, 0, search);
        this.context = context;
        this.searchTermClickInterface = searchTermClickInterface;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final String search = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item_view, parent, false);
        }
        // Lookup view for data population
        TextView tvSearch = (TextView) convertView.findViewById(R.id.tv_item_search);
        // Populate the data into the template view using the data object
        tvSearch.setText(search);


        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTermClickInterface.onSearchItemClick(search);
            }
        });

        return convertView;
    }
}
