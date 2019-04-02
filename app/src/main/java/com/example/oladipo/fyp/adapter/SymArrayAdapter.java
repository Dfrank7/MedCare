package com.example.oladipo.fyp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.model.Symptoms;

import java.util.ArrayList;
import java.util.List;

public class SymArrayAdapter extends ArrayAdapter<Symptoms> {
    private List<Symptoms> symptoms, suggestions;
    private Context context;


    public SymArrayAdapter(Context context, List<Symptoms> symptoms) {
        super(context,0, symptoms);
        this.symptoms = symptoms;
        this.context = context;
    }

    public Filter getFilter(){
        return symptomFilter;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.rowcard, parent,
                        false);
            }
            Symptoms symptom = getItem(position);
            TextView name = convertView.findViewById(R.id.name);
            if (symptom!=null) {
                name.setText(symptom.getName());
                name.setTextColor(Color.BLACK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Symptoms getItem(int position) {
        return symptoms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return symptoms.size();
    }


    private Filter symptomFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions = new ArrayList<>();
            FilterResults filterResults = new FilterResults();
            if (constraint==null||constraint.length()==0) {
                suggestions.addAll(symptoms);
            }else {
                String filterPattern = constraint.toString().toLowerCase();
                for (Symptoms symptom : symptoms){
                    if(
                            symptom.getName().toLowerCase().
                                    startsWith(filterPattern)){
                        suggestions.add(symptom);
                    }
                }
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Symptoms sym = (Symptoms) resultValue;
            return sym.getName();
        }
    };
}

