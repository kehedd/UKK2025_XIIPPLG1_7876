package com.example.ukk_hed;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class kategoriAdpt extends ArrayAdapter<ktgr> {
    public kategoriAdpt(Context context, List<ktgr> ktgrlist) {super(context,0,ktgrlist);}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ktgr_item, parent, false);
        }

        TextView kategori = convertView.findViewById(R.id.nama);

        ktgr book = getItem(position);
        if (book != null) {
            kategori.setText("Kategori: " + book.getNama_kategori());

        }

        return convertView;
    }
}
