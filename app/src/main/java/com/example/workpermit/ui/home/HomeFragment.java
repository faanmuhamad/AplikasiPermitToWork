package com.example.workpermit.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.workpermit.HistoryPermitActivity;
import com.example.workpermit.HomeActivity;
import com.example.workpermit.PermitActivity;
import com.example.workpermit.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LinearLayoutCompat home_btn_permit, home_btn_history;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        home_btn_permit = root.findViewById(R.id.home_btn_permit);
        home_btn_history = root.findViewById(R.id.home_btn_history);

        home_btn_permit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToPermit();
            }
        });

        home_btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHistory();
            }
        });
        return root;
    }

    private void GoToHistory() {
        Intent i = new Intent(getActivity(), HistoryPermitActivity.class);
        startActivity(i);
    }

    private void GoToPermit() {
        Intent i = new Intent(getActivity(), PermitActivity.class);
        startActivity(i);
    }
}