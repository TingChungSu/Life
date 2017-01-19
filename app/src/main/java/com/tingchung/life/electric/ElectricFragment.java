package com.tingchung.life.electric;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.daimajia.swipe.util.DividerItemDecoration;
import com.daimajia.swipe.util.RecyclerViewAdapter;
import com.tingchung.life.ContentFragment;
import com.tingchung.life.MainActivity;
import com.tingchung.life.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import util.FileIO;

/**
 * Created by Administrator on 2017/1/9.
 */

public class ElectricFragment extends ContentFragment {

    private ElectricDbCtrl dbCtrl;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;

    private ArrayList<ElectricObject> mDataSet;


    private String TAG = "ELECTRIC";
    private final static String strElectricFile = "/sdcard/Download/Electric.txt";
    private TextView textMain;

    public static ElectricFragment newInstance() {
        ElectricFragment contentFragment = new ElectricFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_electric, container, false);

        Button button;
//        = (Button) rootView.findViewById(R.id.btnDelete);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onDeleteClick();
//            }
//        });
        dbConnect();
        final TableRow tableRow = (TableRow) rootView.findViewById(R.id.btnAdd);
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
            }
        });

        button = (Button) rootView.findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClick();
            }
        });

        return rootView;
    }

    private void dbConnect() {
        // 建立資料庫物件
        dbCtrl = ElectricDbCtrl.getInstance(getActivity().getApplicationContext());

        if (dbCtrl.getCount() == 0) {
            dbCtrl.sample();
        }

        List<ElectricObject> items = dbCtrl.getAll();
        for (ElectricObject item : items) {
            if (MainActivity.isDebug) {
                Log.d(TAG, "DB test ID : " + item.getId());
                Log.d(TAG, "DB test Date : " + item.getDate());
                Log.d(TAG, "DB test Time : " + item.getTime());
                Log.d(TAG, "DB test Number : " + item.getNumber());
            }
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) containerView.findViewById(R.id.recycler_view);
        reLoadEletricData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        // Adapter:
        mDataSet = new ArrayList<ElectricObject>(dbCtrl.getAll());
        mAdapter = new RecyclerViewAdapter(getActivity(), mDataSet);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Multiple);
        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
        * Substitute for our onScrollListener for RecyclerView
        */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };


    private void reLoadEletricData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                textMain.setText(FileIO.readFile(strElectricFile));
//                textMain.clearFocus();
            }
        });
    }

    public void onAddClick() {
        showDialog();
    }

    public void onSaveClick() {
//        FileIO.writeFile(strElectricFile, textMain.getText().toString().trim(), false);
//        reLoadEletricData();
    }

    public void onDeleteClick() {
        FileIO.deleteAllFile(strElectricFile);
        reLoadEletricData();
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View v = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(getActivity());
        MyAlertDialog.setTitle("新增");
        MyAlertDialog.setView(v);
        final EditText etNumber = (EditText) (v.findViewById(R.id.editText));
        TextView textView = (TextView) (v.findViewById(R.id.textView));
        textView.setText(whatTimeIsIt());
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!"".equals(etNumber.getText().toString())) {
                    addElectric(Float.parseFloat(etNumber.getText().toString()));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    Date current = new Date();
                    ElectricObject item = new ElectricObject(0, dateFormat.format(current), timeFormat.format(current), Float.parseFloat(etNumber.getText().toString()));
                    if (dbCtrl != null) {
                        dbCtrl.insert(item);
                        mAdapter.insertToDataset(item);
                    }
                }
            }
        };
        MyAlertDialog.setNeutralButton("確定", OkClick);

        MyAlertDialog.show();
        etNumber.requestFocus();
    }

    private void addElectric(float number) {
        FileIO.writeFile(strElectricFile, whatTimeIsIt() + ",\t\t\t\t" + number, true);
        reLoadEletricData();
    }

    private String whatTimeIsIt() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd\t\tHH:mm:ss");
        Date current = new Date();
        return sdFormat.format(current);
    }
}
