package com.daimajia.swipe.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.tingchung.life.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ListViewAdapter extends BaseSwipeAdapter {

    private ArrayList<String> mDataset;
    private Context mContext;

    public ListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public ListViewAdapter(Context mContext,String[] adapterData) {
        mDataset = new ArrayList<String>(Arrays.asList(adapterData));
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        return v;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        if(position > getCount()-1)
            return;

        TextView t = (TextView)convertView.findViewById(R.id.position);
        t.setText((position + 1) + ".");

        TextView textView = (TextView)convertView.findViewById(R.id.text_data);
        textView.setText(mDataset.get(position));

        final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                    @Override
                    public void onDoubleClick(SwipeLayout layout, boolean surface) {
                        Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
                    }
                });
                convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                        mItemManger.closeItem(position);
                        mDataset.remove(position);
                        mItemManger.removeShownLayouts(swipeLayout);
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getCount() {
        if(mDataset!=null)
            return mDataset.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
