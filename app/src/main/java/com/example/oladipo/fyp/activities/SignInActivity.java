package com.example.oladipo.fyp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.fragments.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseFragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.linear)
    LinearLayout linearLayout;
    @BindView(R.id.register_email_text)
    EditText emailField;
    @BindView(R.id.staff_id)
    EditText passwordField;
    @BindView(R.id.staff_name)
    EditText nameField;
    @BindView(R.id.genderSpinner)
    Spinner genderField;
    @BindView(R.id.age_id)
    EditText ageField;
    @BindView(R.id.register_signInButton)
    Button signInButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String user_id;
    private String gender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_activity, container, false);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        genderField.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderField.setAdapter(adapter);

        register();

        return view;
    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.signin_activity);
//        ButterKnife.bind(this);
//        mAuth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();
//        genderField.setOnItemSelectedListener(this);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.gender, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        genderField.setAdapter(adapter);
//
//                if (mAuth.getCurrentUser() != null){
//
//                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//                register();
//            }

    private void register(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectionAvailable(getActivity().getApplicationContext())){
                    final String email = emailField.getText().toString();
                    final String password = passwordField.getText().toString();
                    final String name = nameField.getText().toString().trim();
                    final String age = ageField.getText().toString().trim();
                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)
                            || TextUtils.isEmpty(age)){
                        showToast("Fill all fields");
                    }else if (password.length()<8){
                        showToast("password too small (min of 8)");
                    }else {
                        //showProgressDialog("please wait");
                        showloading("please Wait");
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password). addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //hideProgressDialog();
                                if (task.isSuccessful()){
                                    storeUser(email, name, age);
                                }else {
                                    hideLoading();
                                    showToast(task.getException().getMessage());
                                }
                            }
                        });
                    }

                }else {
                    Snackbar.make(linearLayout, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeUser(final String email, final String name, final String age){
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("name", name);
        postMap.put("email", email);
        postMap.put("gender", gender);
        postMap.put("age", age);
        firestore.collection("Patients").document(user_id).set(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //hideProgressDialog();
                hideLoading();
                if(task.isSuccessful()){
//                    toast("Account Successfully Created.");
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    storeUser2(email,name,age);
                } else {

                    String error = task.getException().getMessage();
                    showToast("(FIRESTORE Error) : " + error);
                }

                //setupProgress.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void storeUser2(String email, String name, String age){
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("name", name);
        postMap.put("email", email);
        postMap.put("gender", gender);
        postMap.put("age", age);
        firestore.collection("Available Patients").document(user_id).set(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //hideProgressDialog();
                hideLoading();
                if(task.isSuccessful()){
                    showToast("Account Successfully Created.");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    //finish();

                } else {

                    String error = task.getException().getMessage();
                    showToast("(FIRESTORE Error) : " + error);
                }

                //setupProgress.setVisibility(View.INVISIBLE);

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

