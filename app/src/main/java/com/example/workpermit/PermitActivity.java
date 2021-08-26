package com.example.workpermit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PermitActivity extends AppCompatActivity {

    List<PermitModel> permitModels = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageView permit_add_btn;
    EditText permit_search;
    FirebaseFirestore db;
    PermitAdapter adapter;
    ProgressDialog pd;
    FirebaseAuth mAuth;
    Session session;
    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permit);

        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        session = new Session(this);
        level = session.getLevel();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.permit_recycler);
        permit_add_btn = findViewById(R.id.permit_add_btn);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        permit_search = findViewById(R.id.permit_search);

        if(session.getLevel().equals("Manager")){
            permit_add_btn.setVisibility(View.GONE);
        }

        if(session.getLevel().equals("HSE")){
            permit_add_btn.setVisibility(View.GONE);
        }

        permit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showData(""+charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pd.setMessage("Loading.....");
                        pd.show();
                        showData("");
                    }
                });



        permit_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PermitActivity.this, PengajuanPermitActivity.class);
                startActivity(i);
            }
        });
    }

    private void showData(final String character) {
        if(level.equals("Supervisor")){
            db.collection("permit")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
//                    .whereEqualTo("uid", mAuth.getUid())
//                    .whereEqualTo("status", "waiting")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            pd.dismiss();
                            if(e != null) {
                                Toast.makeText(PermitActivity.this, "Listen failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            permitModels.clear();

                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                if(doc.getString("uid").equals(mAuth.getUid())) {
                                    if(doc.getString("status").equals("waiting")) {
                                        if(doc.getString("area_kerja").contains(""+character)){
                                            PermitModel model = new PermitModel(
                                                    doc.getId().toString(),
                                                    doc.getString("no_permit"),
                                                    doc.getString("area_kerja"),
                                                    doc.getString("status"),
                                                    doc.getString("notif_supervisor"));

                                            permitModels.add(model);
                                        }
                                    }
                                }
                            }

                            adapter = new PermitAdapter(permitModels, PermitActivity.this);

                            recyclerView.setAdapter(adapter);
                        }
                    });
        }else if(level.equals("Manager")){
            db.collection("permit")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            pd.dismiss();
                            if(e != null) {
                                Toast.makeText(PermitActivity.this, "Listen failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            permitModels.clear();

                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                if(doc.getString("area_kerja").contains(""+character)){
                                    PermitModel model = new PermitModel(
                                            doc.getId().toString(),
                                            doc.getString("no_permit"),
                                            doc.getString("area_kerja"),
                                            doc.getString("status"),
                                            doc.getString("notif_supervisor"));

                                    permitModels.add(model);
                                }
                            }

                            adapter = new PermitAdapter(permitModels, PermitActivity.this);

                            recyclerView.setAdapter(adapter);
                        }
                    });
        }else if(level.equals("HSE")){
            db.collection("permit")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            pd.dismiss();
                            if(e != null) {
                                Toast.makeText(PermitActivity.this, "Listen failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            permitModels.clear();

                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                if(doc.getString("area_kerja").contains(""+character)){
                                    PermitModel model = new PermitModel(
                                            doc.getId().toString(),
                                            doc.getString("no_permit"),
                                            doc.getString("area_kerja"),
                                            doc.getString("status"),
                                            doc.getString("notif_supervisor"));

                                    permitModels.add(model);
                                }
                            }

                            adapter = new PermitAdapter(permitModels, PermitActivity.this);

                            recyclerView.setAdapter(adapter);
                        }
                    });
        }
    }
}