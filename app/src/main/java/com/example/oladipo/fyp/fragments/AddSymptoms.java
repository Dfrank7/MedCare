package com.example.oladipo.fyp.fragments;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.activities.MedicationActivity;
import com.example.oladipo.fyp.adapter.DiagnosisAdapter;
import com.example.oladipo.fyp.adapter.MedAdapter;
import com.example.oladipo.fyp.adapter.SymArrayAdapter;
import com.example.oladipo.fyp.api.Client;
import com.example.oladipo.fyp.api.Service;
import com.example.oladipo.fyp.model.Diagnosis;
import com.example.oladipo.fyp.model.Medications;
import com.example.oladipo.fyp.model.Symptom;
import com.example.oladipo.fyp.model.Symptoms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSymptoms extends BaseFragment {
    @BindView(R.id.symp1)
    AppCompatAutoCompleteTextView editText1;
    @BindView(R.id.symp2)
    AppCompatAutoCompleteTextView editText2;
    @BindView(R.id.symp3)
    AppCompatAutoCompleteTextView editText3;
    @BindView(R.id.symp4)
    AppCompatAutoCompleteTextView editText4;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    TextView emptyTextView;
    @BindView(R.id.validate)
    Button checkAilments;
    @BindView(R.id.saveButton)
    Button saveButton;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private Symptom symptomtoChange;
    private List<Symptoms> symptomList = new ArrayList<>();
    private List<Symptoms> symptomList1 = new ArrayList<>();
    private List<Symptoms> symptomList2 = new ArrayList<>();
    private List<Symptoms> symptomList3 = new ArrayList<>();
    private List<Diagnosis> diagnoses = new ArrayList<>();
    private List<String> mDiagnoses = new ArrayList<>();
    private SymArrayAdapter recyclerAdapter,adapter, adapter1, adapter2;
    private JsonArray jsonArray;
    private String id1,id2,id3,id4;
    private String age, gender;
    private static final int NOTIFICATION_ID = 100;
    private static final String NOTIFICATION_CHANEL_ID = "event_channel ";


    public static AddSymptoms newInstance(Symptom symptom, String age, String gender) {
        Bundle args = new Bundle();
        args.putParcelable("symp_bundle", symptom);
        args.putString("age", age);
        args.putString("gender", gender);
        AddSymptoms fragment = new AddSymptoms();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_symptoms, container, false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Symptom symptom = getArguments().getParcelable("symp_bundle");
        gender = getArguments().getString("gender");
        age = getArguments().getString("age");
        showloading("Please Wait");

        getResponse();
        if (getArguments()!=null && symptom != null){
            hideLoading();
            checkAilments.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
            symptomtoChange = symptom;
            setToolbarTitle("Edit Symptoms");
            editText1.setText(symptom.symptom1);
            editText2.setText(symptom.symtom2);
            editText3.setText(symptom.symtom3);
            List<String> names = symptom.diagnoses;
            for (int i=0; i<names.size(); i++){
               String dia_name = names.get(i);
               diagnoses.add(new Diagnosis(dia_name));
            }

            DiagnosisAdapter diagnosisAdapter = new DiagnosisAdapter(getActivity(), diagnoses);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(diagnosisAdapter);
            if (names.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
            saveButton.setText("Update");
        }else {
            setToolbarTitle("Add symptoms");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEditTexts(editText1)){
                    showToast("Please Enter feelings");
                    return;
                }
                if (!validateEditTexts(editText2)){
                    showToast("Please Enter feelings");
                    return;
                }
                if (!validateEditTexts(editText3)){
                    showToast("Please Enter feelings");
                    return;
                }

                buttonAction();
            }
        });
        editText1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id1 = String.valueOf(symptomList.get(position).getId());
                if(id1.equals(null)){
                    id1 = "0";
                }
                //showToast(String.valueOf(symptomList.get(position).getId()));
//                showToast(id1);
            }
        });
        editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id2 = String.valueOf(symptomList1.get(position).getId());
                if(id2.equals(null)){
                    id2 = "0";
                }

            }
        });
        editText3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id3 = String.valueOf(symptomList2.get(position).getId());
                //showToast(String.valueOf(id3));
                if(id3.equals(null)){
                    id3 = "0";
                }
            }
        });
        editText4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id4 = String.valueOf(symptomList3.get(position).getId());
                //showToast(String.valueOf(id4));
            }
        });
        checkData();
        }
        private void buttonAction(){
        Symptom symptom = new Symptom(editText1.getText().toString(),
                editText2.getText().toString(),
                editText3.getText().toString(),
                mAuth.getCurrentUser().getUid(),mDiagnoses);

        if (symptomtoChange!=null && symptomtoChange.symp_id!=null){
            updateData(symptomtoChange.symp_id, symptom);
        }else {
            saveData(symptom);
        }
        }
        private void checkData(){

        checkAilments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEditTexts(editText1)){
                    showToast("Please Enter at least 3 symptoms");
                    return;
                }
                if (!validateEditTexts(editText2)){
                    showToast("Please Enter at least 3 symptoms");
                    return;
                }
                if (!validateEditTexts(editText3)){
                    showToast("Please Enter at least 3 symptoms");
                    return;
                }
                showloading("Loading");

                getDiagnosis();
            }
        });
        }

        private void saveData(final Symptom symptom){
        showloading("Sending");
              firestore.collection("Patients/" +mAuth.getCurrentUser().getUid()+ "/Symptoms")
                      .document(mAuth.getCurrentUser().getUid())
                      .set(symptom)
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()){
//                                  showToast("Saved");
                                  savaData2(symptom);
                              }else{
                                  showToast(task.getException().getMessage());
                              }
                          }
                      });
        }
        private void savaData2(Symptom symptom){
            firestore.collection("Available Patients/" +mAuth.getCurrentUser().getUid()+ "/Symptoms")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(symptom)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                            if (task.isSuccessful()){
                                showToast("Saved");
                                sendNotification(getActivity().getApplicationContext());
                                navigateToFragment(new SymptomView());
                            }else{
                                showToast(task.getException().getMessage());
                            }
                        }
                    });
        }

//        private void saveData( Symptom symptom){
//            firestore.collection("Patients/" +mAuth.getCurrentUser().getUid()+ "/Symptoms")
//                    .add(symptom)
//                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            if (task.isSuccessful()){
//                                showToast("Saved");
//                            }else {
//                                showToast(task.getException().getMessage());
//                            }
//                        }
//                    });
//        }



        private void updateData(String id, Symptom symptom){
        firestore.collection("Patients/" +mAuth.getCurrentUser().getUid()+ "/Symptoms")
                .document(id)
                .set(symptom).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    showToast("Data Update Successful");
                }else {
                    showToast(task.getException().getMessage());
                }
            }
        });
        }

    void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void getResponse(){
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<JsonArray> call = service.getSymptons();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                hideLoading();
                showToast("Data Succesfully Loaded");
                jsonArray = response.body();
//                if(jsonArray==null){
//                    Toast.makeText(MainActivity.this, "Bad Network", Toast.LENGTH_SHORT).show();
//                }
                for (int i = 0; i < jsonArray.size(); i++) {
                    String name = jsonArray.get(i).getAsJsonObject().get("Name").getAsString();
                    int id = jsonArray.get(i).getAsJsonObject().get("ID").getAsInt();
                    symptomList.add(new Symptoms(id, name));
                    symptomList1.add(new Symptoms(id, name));
                    symptomList2.add(new Symptoms(id, name));
                    symptomList3.add(new Symptoms(id, name));
                    //Log.d("Tag",name);
                }
                recyclerAdapter = new SymArrayAdapter(getActivity(), symptomList);
                adapter = new SymArrayAdapter(getActivity(), symptomList1);
                adapter1 = new SymArrayAdapter(getActivity(), symptomList2);
                adapter2 = new SymArrayAdapter(getActivity(), symptomList3);
                editText1.setThreshold(1);
                editText1.setAdapter(recyclerAdapter);
                editText2.setThreshold(1);
                editText2.setAdapter(adapter);
                editText3.setThreshold(1);
                editText3.setAdapter(adapter1);
                editText4.setThreshold(1);
                editText4.setAdapter(adapter2);
                hideLoading();


            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                hideLoading();
                showToast("No/poor Internet");
            }
        });
    }
    private int getYear(int age){
        int now_year = Calendar.getInstance().get(Calendar.YEAR);
        int year_born = now_year-age;
        return year_born;
    }

    private void getDiagnosis(){
        int i1 = Integer.valueOf(id1);
        int i2 = Integer.valueOf(id2);
        int i3 = Integer.valueOf(id3);
        int[] maps = {i1, i2, i3};
        String val = Arrays.toString(maps).trim();
        Log.d("TAG", val);
        String format = "json";
        String language = "en-gb";
        int year = Integer.valueOf(age);
        String year_birth = String.valueOf(getYear(year));


        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<JsonArray> call = service.getDiagnosis(val,gender,year_birth,
                getToken(),format,language);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                hideLoading();
                JsonArray jsonArray = response.body();
                if (jsonArray!=null && jsonArray.size()>0){
                for (int i=0; i<jsonArray.size(); i++){
                    String name = jsonArray.get(i).getAsJsonObject().get("Issue")
                            .getAsJsonObject().get("Name").getAsString();
                    int accuracy = jsonArray.get(i).getAsJsonObject().get("Issue")
                            .getAsJsonObject().get("Accuracy").getAsInt();
                    diagnoses.add(new Diagnosis(accuracy,name));
                    mDiagnoses.add(name);
                    //Log.d("TAG", name);
                }

                if (!diagnoses.isEmpty()) {
                    saveButton.setVisibility(View.VISIBLE);
                    checkAilments.setVisibility(View.GONE);
                    DiagnosisAdapter diagnosisAdapter = new DiagnosisAdapter(getActivity(), diagnoses);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(diagnosisAdapter);
                }else {
                    saveButton.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    public static boolean validateEditTexts(EditText... editTexts) {
        for (EditText newtext : editTexts) {
            if (newtext.getText().toString().trim().length() < 1) {
                newtext.setError("This Form is required");
                return false;
            }
        }
        return true;
    }
//    private void loadMedications(){
//        firestore.collection("Available Patients/"+mAuth.getCurrentUser().getUid()+"/Medications").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        hideProgressDialog();
//                        if (task.isSuccessful()){
//                            QuerySnapshot document = task.getResult();
//                            if (document.){}
//                        }else {
//                            //progressBar.setVisibility(View.INVISIBLE);
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//
//                        }
//
//                    }
//                });
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.medication :
                startActivity(new Intent(getActivity(), MedicationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendNotification(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setUPNotificationChannel(notificationManager);
        }
        notificationManager.notify(NOTIFICATION_ID, notifyManager());
        Log.d("TAG", "About to send notification");
    }

    private Notification notifyManager(){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(getActivity().getApplicationContext(), NOTIFICATION_CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(" A new Patient has been added ")
                .setContentText("This is to tell you that a new patient has been added")
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(soundUri)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setUPNotificationChannel(NotificationManager notificationManager) {

        CharSequence name = "A new Patient has been added";
        String description = "This is to tell you that a new patient has been added";
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID, name,
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);

    }
}
