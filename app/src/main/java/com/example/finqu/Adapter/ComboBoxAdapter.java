package com.example.finqu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finqu.R;

import java.util.ArrayList;

public class ComboBoxAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> items;

    public ComboBoxAdapter(Context contextParam, ArrayList<String> itemsParam) {
        this.context = contextParam;
        this.items = itemsParam;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return -1;
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.item_layout_combo_box, null, false);

        ((TextView)viewInflate.findViewById(R.id.comboBoxLbl)).setText(items.get(i));

        return viewInflate;
    }
}
