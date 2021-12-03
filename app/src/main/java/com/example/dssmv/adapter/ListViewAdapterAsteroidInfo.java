package com.example.dssmv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dssmv.R;
import com.example.dssmv.model.AsteroidInfo;

import java.util.List;

public class ListViewAdapterAsteroidInfo extends BaseAdapter {
    Context context;
    int layout_id;
    List<AsteroidInfo> items;

    public ListViewAdapterAsteroidInfo(final Context context, final int layout_id,final List<AsteroidInfo> items) {
        this.context = context;
        this.items = items;
        this.layout_id = layout_id;
    }

    public void setItems(final List<AsteroidInfo> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;
        final AsteroidInfo asteroid = this.items.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(layout_id, null);
        } else {
            itemView = convertView;
        }

        TextView asteroid_name = (TextView) itemView.findViewById(R.id.weather_date);
        asteroid_name.setText(asteroid.getName());

        TextView diameter = (TextView) itemView.findViewById(R.id.weather_min);
        diameter.setText(String.valueOf(asteroid.getDiameter()));

        TextView hazardous = (TextView) itemView.findViewById(R.id.weather_max);
        hazardous.setText(String.valueOf(asteroid.getHazardous()));

        /*ImageView imageView = (ImageView) itemView.findViewById(R.id.thumbImage);
        imageView.setImageBitmap(asteroid.getIcon());*/

        return itemView;

    }
}
