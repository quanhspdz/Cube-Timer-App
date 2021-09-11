package com.example.progressbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Long> arrayTime;

    public TimeAdapter(Context context, int layout, ArrayList<Long> arrayTime) {
        this.context = context;
        this.layout = layout;
        this.arrayTime = arrayTime;
    }

    @Override
    public int getCount() {
        return arrayTime.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTime = convertView.findViewById(R.id.txtEachTime);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        String strTime = MainActivity.convertToHours(arrayTime.get(position));
        holder.txtTime.setText(strTime);

        return convertView;
    }
    private class ViewHolder {
        private TextView txtTime;
    }
}
