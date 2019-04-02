package com.example.oladipo.fyp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.widget.Button;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.adapter.SymArrayAdapter;
import com.example.oladipo.fyp.api.Client;
import com.example.oladipo.fyp.api.Service;
import com.example.oladipo.fyp.model.Symptoms;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSymptomActivity extends BaseActivity {

    @BindView(R.id.symp1)
    AppCompatAutoCompleteTextView editText1;
    @BindView(R.id.symp2)
    AppCompatAutoCompleteTextView editText2;
    @BindView(R.id.symp3)
    AppCompatAutoCompleteTextView editText3;
    @BindView(R.id.symp4)
    AppCompatAutoCompleteTextView editText4;
    @BindView(R.id.validate)
    Button checkAilments;

    private List<Symptoms> symptomList = new ArrayList<>();
    private List<Symptoms> symptomList1 = new ArrayList<>();
    private List<Symptoms> symptomList2 = new ArrayList<>();
    private List<Symptoms> symptomList3 = new ArrayList<>();
    private SymArrayAdapter recyclerAdapter,adapter, adapter1, adapter2;
    JsonArray jsonArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_symptoms);
        ButterKnife.bind(this);
        showProgressDialog("Please Wait");

        getResponse();
    }

    private void getResponse(){
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<JsonArray> call = service.getSymptons();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                hideProgressDialog();
                toast("Data Succesfully Loaded");
                jsonArray = response.body();
//                if(jsonArray==null){
//                    Toast.makeText(MainActivity.this, "Bad Network", Toast.LENGTH_SHORT).show();
//                }
                for (int i = 0; i<jsonArray.size(); i++){
                    String name = jsonArray.get(i).getAsJsonObject().get("Name").getAsString();
                    int id = jsonArray.get(i).getAsJsonObject().get("ID").getAsInt();
                    symptomList.add(new Symptoms(name));
                    symptomList1.add(new Symptoms(name));
                    symptomList2.add(new Symptoms(name));
                    symptomList3.add(new Symptoms(name));
                    //Log.d("Tag",name);
                }
                recyclerAdapter = new SymArrayAdapter(getApplicationContext(), symptomList);
                adapter = new SymArrayAdapter(getApplicationContext(), symptomList1);
                adapter1 = new SymArrayAdapter(getApplicationContext(), symptomList2);
                adapter2 = new SymArrayAdapter(getApplicationContext(), symptomList3);
                editText1.setThreshold(1);
                editText1.setAdapter(recyclerAdapter);
                editText2.setThreshold(1);
                editText2.setAdapter(adapter);
                editText3.setThreshold(1);
                editText3.setAdapter(adapter1);
                editText4.setThreshold(1);
                editText4.setAdapter(adapter2);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                hideProgressDialog();
                toast("No/poor Internet");
            }
        });
    }
}
