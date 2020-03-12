package com.example.foodtracker.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodtracker.R;
import com.example.foodtracker.ScannedBarcodeActivity;
import com.example.foodtracker.fetchData;


public class ToolsFragment extends Fragment implements View.OnClickListener {

    Button click;
    // Scan Button
    Button btnScanBarcode;

    public static TextView data;

    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        click = (Button) root.findViewById(R.id.fetchJSON);
        data = (TextView) root.findViewById(R.id.fetcheddata);
        btnScanBarcode = (Button) root.findViewById(R.id.btnScanBarcode);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData process = new fetchData();
                process.execute();
            }
        });

        initViews();
        return root;
    }

    private void initViews() {
        btnScanBarcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        startActivity(new Intent(ToolsFragment.this, ScannedBarcodeActivity.class));
        Intent myIntent = new Intent(ToolsFragment.this.getActivity(),ScannedBarcodeActivity.class);
        startActivity(myIntent);
    }
}