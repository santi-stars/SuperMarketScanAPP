package com.svalero.supermarketscan.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.domain.FavListaTotal;
import com.svalero.supermarketscan.view.FavListView;

import java.text.DecimalFormat;
import java.util.List;

public class FavListAdapter extends BaseAdapter {

    private Context context;
    public List<FavListaTotal> favList;
    private LayoutInflater inflater;
    private DecimalFormat df;

    public FavListAdapter(Activity context, List<FavListaTotal> favList) {
        this.context = context;
        this.favList = favList;
        inflater = LayoutInflater.from(context);
        df = new DecimalFormat("#.##");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FavListaTotal favListaTotal = (FavListaTotal) getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listas_adapter, parent, false);
        }

        TextView nameListTv = convertView.findViewById(R.id.list_name_adapter);
        TextView totalListTv = convertView.findViewById(R.id.list_total_adapter);
        ImageView editListButton = convertView.findViewById(R.id.edit_list_imageView);
        ImageView deleteListButton = convertView.findViewById(R.id.delete_list_imageView);

        nameListTv.setText(favListaTotal.getNombreLista());
        totalListTv.setText(df.format(favListaTotal.getTotal()) + "€");

        editListButton.setTag(position);
        deleteListButton.setTag(position);

        editListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                ((FavListView) context).editFavList(v, pos);
            }
        });

        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                ((FavListView) context).deleteFavList(v, pos);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return favList.size();
    }

    @Override
    public Object getItem(int position) {
        return favList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
