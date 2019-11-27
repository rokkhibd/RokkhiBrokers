package com.rokkhi.brokers.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rokkhi.brokers.R;



public class CustomListAdapter extends BaseAdapter {

    Context context;
    String roadList[];


    public CustomListAdapter(Context context, String roadList[]) {

        this.context=context;
        this.roadList=roadList;

    }

    @Override
    public int getCount() {
        return roadList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.custom_lisview,parent,false);
        TextView txt=view.findViewById(R.id.custom_list);

        txt.setText(roadList[position]);

        return view;
    }
}
