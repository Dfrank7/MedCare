package com.example.oladipo.fyp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.activities.AddSymptomActivity;
import com.example.oladipo.fyp.activities.ProfileActivity;
import com.example.oladipo.fyp.activities.SignInActivity;
import com.example.oladipo.fyp.adapter.SymptomAdapter;
import com.example.oladipo.fyp.model.NewSymptom;
import com.example.oladipo.fyp.model.Symptom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SymptomView extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.message)
    TextView text;
    List<Symptom> symptomList = new ArrayList<>();

    private SymptomAdapter symptomAdapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String name, age, email,gender;

    public SymptomView(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.symptom_fragment, container, false);
        ButterKnife.bind(this, view);
        setToolbarTitle("Home");
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        symptomAdapter = new SymptomAdapter(symptomList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(symptomAdapter);
        symptomList.clear();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUserData();
        //loadList();
        //fetchUserData();
//        ((MainActivity) getActivity()).setDrawerIconToHome();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isConnectionAvailable(getActivity())){
            progressBar.setVisibility(View.GONE);
            loadList();
            Snackbar.make(getActivity().findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                    .show();

        }else {
            loadList();
            symptomList.clear();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        symptomList.clear();
    }



    private void loadList(){
        firestore.collection("Patients/" +mAuth.getCurrentUser().getUid()+ "/Symptoms").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc: task.getResult()){
                                //fetchUserData();
                                Symptom symptom = doc.toObject(Symptom.class);
                                symptom.setSymp_id(doc.getId());
                                symptomList.add(symptom);
                            }
                            if (!symptomList.isEmpty()) {
                                symptomAdapter = new SymptomAdapter(symptomList, getActivity());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(symptomAdapter);
                            }else {
                                recyclerView.setVisibility(View.GONE);
                                text.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }

                    }
                });
    }

    private void fetchUserData(){
        DocumentReference doc = firestore.collection("Available Patients").
                document(mAuth.getCurrentUser().getUid());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    showToast("Details successfully loaded");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.get("name").toString();
                        Log.d("Name", name);
                        gender = document.get("gender").toString();
                        email = document.get("email").toString();
                        age = document.get("age").toString();
                        progressBar.setVisibility(View.GONE);
                    }else {
                        showToast("No Document");
                    }
                }else {
                    showloading(task.getException().getMessage());
                }
            }
        });
    }

    void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick(R.id.fab)
    void onClickFab(){
        navigateToFragment(AddSymptoms.newInstance(null, age,gender));
        //startActivity(new Intent(getContext(), AddSymptomActivity.class));
    }

    private void movetoProfile(){
        Intent intent = new Intent(getActivity(), ProfileActivity.class)
                .putExtra("name", name)
                .putExtra("age", age)
                .putExtra("email", email)
                .putExtra("gender", gender);
        startActivity(intent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                signOut();
                break;
            case R.id.profile:
                movetoProfile();
                //checkProfile
                break;
        }
        return true;
    }

    private void signOut(){
        mAuth.signOut();
        startActivity(new Intent(getActivity(), SignInActivity.class));
    }

    public boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
