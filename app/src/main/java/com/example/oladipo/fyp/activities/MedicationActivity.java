package com.example.oladipo.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.adapter.MedAdapter;
import com.example.oladipo.fyp.model.Medications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicationActivity extends BaseActivity {

    @BindView(R.id.med_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noMed)
    TextView text;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private List<Medications> medicationsList = new ArrayList<>();
    MedAdapter medAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_layout);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        setTitle("Medications");
        showProgressDialog("Loading Medications......");
        loadMedications();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loadMedications();
    }

    private void loadMedications(){
        firestore.collection("Available Patients/"+mAuth.getCurrentUser().getUid()+"/Medications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                //fetchUserData();
                                Medications medications = doc.toObject(Medications.class);
//                                symptom.setSymp_id(doc.getId());
                                medicationsList.add(medications);
                            }
                            if (!medicationsList.isEmpty()) {
                                medAdapter = new MedAdapter(getApplicationContext(), medicationsList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(medAdapter);
                            } else {
                                text.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                //progressBar.setVisibility(View.INVISIBLE);
                                Log.d("TAG", "Error getting documents: ", task.getException());

                            }

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.med_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.reminder:
                startActivity(new Intent(this,MedActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
