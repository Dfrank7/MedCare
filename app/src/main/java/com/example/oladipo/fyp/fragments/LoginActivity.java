package com.example.oladipo.fyp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseFragment {
    @BindView(R.id.register_email_text)
    EditText emailField;
    @BindView(R.id.password)
    EditText passwordField;
    @BindView(R.id.register_signInButton)
    Button signIn;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container,false);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        signIn();
        return view;
    }

    private void signIn(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    showloading("Please wait");
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideLoading();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                //getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                                getActivity().finish();
                                //backgroundActivity.finish();

                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(), "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }else{
                    showToast("Fill all fields");
                }
            }
        });
    }
}
