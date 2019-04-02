package com.example.oladipo.fyp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.activities.MainActivity;
import com.example.oladipo.fyp.fragments.AddSymptoms;
import com.example.oladipo.fyp.model.Diagnosis;
import com.example.oladipo.fyp.model.NewSymptom;
import com.example.oladipo.fyp.model.Symptom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.viewHolder> {

    private List<Symptom> symptoms;
    private Context context;
    String symp1,symp2,symp3,diagnosis1;
    public SymptomAdapter(List<Symptom> symptoms, Context context) {
        this.symptoms = symptoms;
        this.context = context;
    }

    @NonNull
    @Override
    public SymptomAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.symp_list_item, viewGroup, false);
        return new viewHolder(view);

    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull SymptomAdapter.viewHolder viewHolder, int position) {
        viewHolder.feeling1.setText(symptoms.get(position).getSymptom1());
        viewHolder.feeling2.setText(symptoms.get(position).getSymtom2());
        symp1 = symptoms.get(position).getSymptom1();
        symp2 = symptoms.get(position).getSymtom2();
        symp3 = symptoms.get(position).getSymtom3();
        List<String> diagnoses = symptoms.get(position).getDiagnoses();

    }

    @Override
    public int getItemCount() {
        if (symptoms.isEmpty()){
            return 0;
        }
        return symptoms.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.list_feeling1)
        TextView feeling1;
        @BindView(R.id.list_feeling2) TextView feeling2;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Symptom symptom = symptoms.get(position);
                    FragmentTransaction transaction = ((MainActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, AddSymptoms.newInstance(symptom, null,null));
                    transaction.addToBackStack(null).commit();
                }
            });
        }
    }
}
