package com.example.oladipo.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.adapter.FragmentAdapter;
import com.example.oladipo.fyp.fragments.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    private FirebaseAuth mAuth;

    List<Fragment> fragments =new ArrayList<>();
    List<String> titles = new ArrayList<>();
    private void adddata(Fragment fragment, String string){
        fragments.add(fragment);
        titles.add(string);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        prepareData();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void prepareData() {
        adddata(new SignInActivity(), "Sign Up");
        adddata(new LoginActivity(), "Sign In");
    }

    }
