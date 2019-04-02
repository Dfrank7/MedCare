package com.example.oladipo.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.oladipo.fyp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {
    @BindView(R.id.det_name)
    TextView name;
    @BindView(R.id.det_email)
    TextView email;
    @BindView(R.id.det_age) TextView age;
    @BindView(R.id.det_gender)
    TextView gender;
    private String mName,mGender,mEmail,mAge;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ButterKnife.bind(this);
        intent = getIntent();
        setTitle("Profile");
        bindData();
    }

    private void bindData(){
        mName = intent.getExtras().getString("name");
        mAge = intent.getExtras().getString("age");
        mGender = intent.getExtras().getString("gender");
        mEmail = intent.getExtras().getString("email");
        name.setText(mName);
        age.setText(mAge);
        gender.setText(mGender);
        email.setText(mEmail);
    }
}
