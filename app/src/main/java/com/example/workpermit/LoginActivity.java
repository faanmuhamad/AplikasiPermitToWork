package com.example.workpermit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText login_username, login_password;
    private Button login_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog loading;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loading = new ProgressDialog(this);
        loading.setMessage("Please Wait.....");
        loading.setCancelable(false);
        loading.setIndeterminate(true);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                CheckDataEntered();
            }
        });

    }

    private void CheckDataEntered() {
        boolean isValid = true;
        if(isEmpty(login_username)){
            login_username.setError("Email harus diisi!");
            isValid = false;
            loading.hide();
        }else{
            if(!isEmail(login_username)){
                login_username.setError("Masukkan Email yang valid!");
                isValid = false;
                loading.hide();
            }
        }
        if(isEmpty(login_password)){
            login_password.setError("Password harus diisi");
            isValid = false;
            loading.hide();
        }

        if(isValid){
            String txt_email = login_username.getText().toString();
            String txt_pass = login_password.getText().toString();

            mAuth.signInWithEmailAndPassword(txt_email, txt_pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                CheckDb();
                            }else{
                                Log.e("login process", "Sign-in Failed: " + task.getException().getMessage());
                                Toast.makeText(LoginActivity.this, "Email atau password yang anda masukkan salah!", Toast.LENGTH_LONG).show();
                                loading.hide();
                            }
                        }
                    });
        }
    }

    private void CheckDb() {
        FirebaseUser user = mAuth.getCurrentUser();
        final String current_id = user.getUid();

        session = new Session(LoginActivity.this);

        db.collection("users").document(current_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    final DocumentSnapshot document = task.getResult();
                    if(document.exists()){

                        String token_id = FirebaseInstanceId.getInstance().getToken();

                        Map<String, Object> tokenMap = new HashMap<>();
                        tokenMap.put("token_id", token_id);

                        db.collection("users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String name = document.getString("name");
                                String level_user = document.getString("level");
                                session.setUid(current_id);
                                session.setName(name);
                                session.setLevel(level_user);
                                Toast.makeText(LoginActivity.this, "Login berhasil! selamat datang "+document.getString("name")+"!", Toast.LENGTH_LONG).show();
                                GoToDashboard();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("login process", "Sign-in Failed: " + e.getMessage());
                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this, "Akun anda bermasalah, silahkan hubungi admin untuk perbaikan!", Toast.LENGTH_LONG).show();
                        loading.hide();
                    }
                }else{
                    Log.e("login process", "Sign-in Failed: " + task.getException().getMessage());
                    Toast.makeText(LoginActivity.this, "Error App: Database Bermasalah!", Toast.LENGTH_LONG).show();
                    loading.hide();
                }
            }
        });
    }

    private void GoToDashboard() {
        loading.dismiss();
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(i);
        finish();
    }

    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}