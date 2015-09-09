/* Copyright (C) 2013 The Android Open Source Project Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package com.ntilde.listaexpandible;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ntilde.donantes.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a custom array adapter used to populate the listview whose items will expand to display extra content in addition to the default display.
 */
public class CustomArrayAdapter extends ArrayAdapter<ExpandableListItem> {
    private List<ExpandableListItem> mData;
    private int mLayoutViewResourceId;
    private Context mContext;

    public CustomArrayAdapter(Context context, int layoutViewResourceId, List<ExpandableListItem> data) {
        super(context, layoutViewResourceId, data);
        mData = data;
        mLayoutViewResourceId = layoutViewResourceId;
        mContext = context;
    }

    /**
     * Populates the item in the listview cell with the appropriate data. This method
     * sets the thumbnail image, the title and the extra text. This method also updates
     * the layout parameters of the item's view so that the image and title are centered
     * in the bounds of the collapsed view, and such that the extra text is not displayed
     * in the collapsed state of the cell.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ExpandableListItem object = mData.get(position);

        Holder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
            holder = new Holder(convertView);

            convertView.setTag(holder);

        }else{
            holder = (Holder) convertView.getTag();
        }

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        holder.linearLayout.setLayoutParams(linearLayoutParams);

        holder.titleView.setText(object.getTitle());
            //holder.imgView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), object.getImgResource(), null));
        Picasso.with(mContext).load(object.getImgResource()).into(holder.imgView);

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

        holder.expandingLayout = (LinearLayout) convertView.findViewById(R.id.expanding_layout);

        if (!object.isExpanded()) {
            holder.expandingLayout.setVisibility(View.GONE);
        } else {
            holder.expandingLayout.setVisibility(View.VISIBLE);

        }

        return convertView;
    }

    static class Holder{
        @InjectView(R.id.image_view)
        ImageView imgView;
        @InjectView(R.id.title_view)
        TextView titleView;
        @InjectView(R.id.item_linear_layout)
        LinearLayout linearLayout;
        @InjectView(R.id.expanding_layout)
        LinearLayout expandingLayout;

        public Holder(View v){
            ButterKnife.inject(this,v);
        }
    }


}