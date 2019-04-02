package com.example.oladipo.fyp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.model.Medications;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.viewHolder> {
    private Context context;
    private List<Medications> medications;

    public MedAdapter(Context context, List<Medications> medications) {
        this.context = context;
        this.medications = medications;
    }

    @NonNull
    @Override
    public MedAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.med_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedAdapter.viewHolder holder, int position) {
        holder.medName.setText("Med Name: "+medications.get(position).name);
        holder.days.setText(medications.get(position).days+" Day(s)");
        holder.interval.setText(medications.get(position).interval+" Interval");
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.inteval)
        TextView interval;
        @BindView(R.id.days)
        TextView days;
        @BindView(R.id.name)
        TextView medName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
    }
