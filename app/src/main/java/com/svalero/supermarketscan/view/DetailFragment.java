package com.svalero.supermarketscan.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.svalero.supermarketscan.R;

public class DetailFragment extends Fragment {


    public FloatingActionButton closeButton;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        // Inflate the layout for this fragment
        View detailView = inflater.inflate(R.layout.fragment_product_detail, container, false);


        closeButton = detailView.findViewById(R.id.close_detail_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDetails.hiddeDetails();
            }
        });

        return detailView;

         */
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.fragment_detail_imageview);
        TextView textView1 = view.findViewById(R.id.fragment_detail_textview1);
        TextView textView2 = view.findViewById(R.id.fragment_detail_textview2);
        TextView textView3 = view.findViewById(R.id.fragment_detail_textview3);
        TextView textView4 = view.findViewById(R.id.fragment_detail_textview4);


    }   // End switch
}