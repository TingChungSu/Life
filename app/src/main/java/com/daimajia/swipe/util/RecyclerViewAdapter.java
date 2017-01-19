package com.daimajia.swipe.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.tingchung.life.R;
import com.tingchung.life.electric.ElectricDbCtrl;
import com.tingchung.life.electric.ElectricObject;

import java.util.ArrayList;

import util.ToastUtil;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewPos;
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewData;
        Button buttonDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewPos = (TextView) itemView.findViewById(R.id.position);
            textViewData = (TextView) itemView.findViewById(R.id.text_data);
            textViewDate = (TextView) itemView.findViewById(R.id.text_date);
            textViewTime = (TextView) itemView.findViewById(R.id.text_time);
            buttonDelete = (Button) itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
//                    ToastUtil.showToast(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private Context mContext;
    private ArrayList<ElectricObject> mDataset;

    public void insertToDataset(ElectricObject object){
        mDataset.add(object);
        notifyDataSetChanged();
    }

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public RecyclerViewAdapter(Context context, ArrayList<ElectricObject> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.electric_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final ElectricObject item = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewData.getText().toString());
//                ToastUtil.showToast(view.getContext(), "onItemSelected: " + viewHolder.textViewData.getText().toString(), Toast.LENGTH_SHORT);
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Log.d(getClass().getSimpleName(), "onDoubleClick: " + viewHolder.textViewData.getText().toString());
//                ToastUtil.showToast(mContext, "DoubleClick", Toast.LENGTH_SHORT);
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                mDataset.remove(position);
                ElectricDbCtrl.getInstance().delete(item.getId());

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
                ToastUtil.showToast(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT);
            }
        });
        viewHolder.textViewPos.setText((position + 1) + ".");
        viewHolder.textViewDate.setText(item.getDate());
        viewHolder.textViewTime.setText(item.getTime());
        viewHolder.textViewData.setText(item.getNumber()+"");
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
