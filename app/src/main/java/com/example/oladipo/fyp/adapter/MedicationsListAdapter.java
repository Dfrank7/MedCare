package com.example.oladipo.fyp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.activities.MedActivity;
import com.example.oladipo.fyp.model.Medication;
import com.example.oladipo.fyp.newmediacation.NewMedicationFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicationsListAdapter extends RecyclerView.Adapter<MedicationsListAdapter.viewHolder> {

    private ArrayList<Medication> medicationArrayList;
    private boolean isFromSearchActivity;

    public MedicationsListAdapter(ArrayList<Medication> medicationArrayList, boolean isFromSearchActivity) {
        this.medicationArrayList = medicationArrayList;
        this.isFromSearchActivity = isFromSearchActivity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Medication medication = medicationArrayList.get(position);
        SimpleDateFormat month_date = new SimpleDateFormat("hh:mm a, MMMM d, yyyy", Locale.getDefault());
        Date date = new Date(medication.startTime);
        holder.medicationName.setText(medication.name);
        holder.medicationStartDate.setText(String.format("Start date: %s", month_date.format(date)));
        date.setTime(medication.endTime);
        month_date.applyPattern("MMMM d, yyyy");
        holder.medicationEndDate.setText(String.format("End date: %s", month_date.format(date)));
    }

    @Override
    public int getItemCount() {
        return medicationArrayList.size();
    }

    public void updateData(ArrayList<Medication> data) {
        medicationArrayList = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        medicationArrayList.clear();
        notifyDataSetChanged();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.medication_name)
        TextView medicationName;
        @BindView(R.id.medication_start_date)
        TextView medicationStartDate;
        @BindView(R.id.medication_end_date)
        TextView medicationEndDate;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Medication medication = medicationArrayList.get(getAdapterPosition());
                FragmentTransaction transaction = ((MedActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, NewMedicationFragment.newInstance(medication));
                transaction.addToBackStack(null).commit();
        }
    }
}
