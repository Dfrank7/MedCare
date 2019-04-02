package com.example.oladipo.fyp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.activemedication.ActiveMedicationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedActivity extends AppCompatActivity {

//    @BindView(R.id.content_frame)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med);
        ButterKnife.bind(this);
        navigateToFragment(new ActiveMedicationFragment());
    }

    void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }



}
