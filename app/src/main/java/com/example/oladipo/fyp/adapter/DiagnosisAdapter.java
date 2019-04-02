package com.example.oladipo.fyp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.model.Diagnosis;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.viewHolder> {
    private Context context;
    private List<Diagnosis> diagnosisList;

    public DiagnosisAdapter(Context context, List<Diagnosis> diagnosisList) {
        this.context = context;
        this.diagnosisList = diagnosisList;
    }

    @NonNull
    @Override
    public DiagnosisAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.diagnosis_row, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisAdapter.viewHolder holder, int position) {
        holder.name.setText(diagnosisList.get(position).getName());
        int acc = diagnosisList.get(position).getAccuracy();
        if (acc>=1) {
            holder.accuracy.setText(String.valueOf(acc) + "%");
        }
        holder.accuracy.setText("");
    }

    @Override
    public int getItemCount() {
        if (diagnosisList.isEmpty()){
            return 0;
        }
        return diagnosisList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.diagnosis_name)
        TextView name;
        @BindView(R.id.diagnosis_accuracy)
        TextView accuracy;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
