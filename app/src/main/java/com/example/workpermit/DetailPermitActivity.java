package com.example.workpermit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailPermitActivity extends AppCompatActivity {

    private String no_permit, permit_uid;
    private TextView detail_area_kerja, detail_lokasi, detail_uraian, detail_durasi_sebelum, detail_durasi_sesudah, detail_pengawas;
    private EditText detail_pengajuan_area_kerja, detail_pengajuan_lokasi, detail_pengajuan_uraian, detail_pengajuan_durasi_sebelum, detail_pengajuan_durasi_sesudah;
    private RadioGroup detail_q1, detail_q2, detail_q3, detail_q4, detail_q5, detail_q6,detail_q7, detail_q8, detail_q9,detail_q10, detail_q11, detail_q12, detail_q13, detail_q14, detail_q15,detail_q16, detail_q17, detail_q18;
    private FirebaseFirestore db;
    private LinearLayoutCompat layout_manager_level, layout_supervisor_level, detail_supervisor_level, detail_manager_level;
    private ImageView detail_approve, detail_review, detail_reject;
    private Button detail_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permit);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        no_permit = bundle.getString("no_permit");
        permit_uid = bundle.getString("permit_uid");

        Session session = new Session(this);

        final String level = session.getLevel();

        detail_area_kerja = findViewById(R.id.detail_area_kerja);
        detail_lokasi = findViewById(R.id.detail_lokasi);
        detail_uraian = findViewById(R.id.detail_uraian);
        detail_durasi_sebelum = findViewById(R.id.detail_durasi_sebelum);
        detail_durasi_sesudah = findViewById(R.id.detail_durasi_sesudah);

        detail_pengajuan_area_kerja = findViewById(R.id.detail_pengajuan_area_kerja);
        detail_pengajuan_lokasi = findViewById(R.id.detail_pengajuan_lokasi);
        detail_pengajuan_uraian = findViewById(R.id.detail_pengajuan_uraian);
        detail_pengajuan_durasi_sebelum = findViewById(R.id.detail_pengajuan_durasi_sebelum);
        detail_pengajuan_durasi_sesudah = findViewById(R.id.detail_pengajuan_durasi_sesudah);

        detail_approve = findViewById(R.id.detail_approve);
        detail_review = findViewById(R.id.detail_review);
        detail_reject = findViewById(R.id.detail_reject);

        detail_save = findViewById(R.id.detail_save);

        detail_q1 = findViewById(R.id.detail_q1);
        detail_q2 = findViewById(R.id.detail_q2);
        detail_q3 = findViewById(R.id.detail_q3);
        detail_q4 = findViewById(R.id.detail_q4);
        detail_q5 = findViewById(R.id.detail_q5);
        detail_q6 = findViewById(R.id.detail_q6);
        detail_q7 = findViewById(R.id.detail_q7);
        detail_q8 = findViewById(R.id.detail_q8);
        detail_q9 = findViewById(R.id.detail_q9);
        detail_q10 = findViewById(R.id.detail_q10);
        detail_q11 = findViewById(R.id.detail_q11);
        detail_q12 = findViewById(R.id.detail_q12);
        detail_q13 = findViewById(R.id.detail_q13);
        detail_q14 = findViewById(R.id.detail_q14);
        detail_q15 = findViewById(R.id.detail_q15);
        detail_q16 = findViewById(R.id.detail_q16);
        detail_q17 = findViewById(R.id.detail_q17);
        detail_q18 = findViewById(R.id.detail_q18);
        detail_pengawas = findViewById(R.id.detail_pengawas);

        layout_manager_level = findViewById(R.id.layout_manager_level);
        layout_supervisor_level = findViewById(R.id.layout_supervisor_level);

        detail_manager_level = findViewById(R.id.detail_manager_level);
        detail_supervisor_level = findViewById(R.id.detail_supervisor_level);

        final Map<String, Object> approve_permit = new HashMap<>();

        if(level.equals("Manager")){
            approve_permit.put("Man_approve", "approved");
            approve_permit.put("status_manager", "approved");
        }else if(level.equals("HSE")){
            approve_permit.put("Hse_approve", "approved");
            approve_permit.put("status_hse", "approved");
        }

        detail_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("permit").document(permit_uid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot snapshot = task.getResult();

                                    if(snapshot.getString("Man_approve") != null && snapshot.getString("Man_approve").equals("approved")){
                                        if(snapshot.getString("Hse_approve") != null && snapshot.getString("Hse_approve").equals("approved")){
                                            approve_permit.put("status", "approved");
                                            approve_permit.put("notif_supervisor", FieldValue.delete());
                                            if(level.equals("HSE")){
                                                approve_permit.put("notif_manager", FieldValue.delete());
                                            }else if(level.equals("Manager")){
                                                approve_permit.put("notif_hse", FieldValue.delete());
                                            }
                                        }else{
                                            if(level.equals("HSE")){
                                                approve_permit.put("notif_supervisor", FieldValue.delete());
                                                approve_permit.put("notif_manager", FieldValue.delete());
                                                approve_permit.put("status", "approved");
                                            }else{
                                                approve_permit.put("status", "waiting");
                                            }
                                        }
                                    }else{
                                        if(snapshot.getString("Hse_approve") != null && snapshot.getString("Hse_approve").equals("approved")){
                                            if(snapshot.getString("Man_approve") != null && snapshot.getString("Man_approve").equals("approved")){
                                                approve_permit.put("status", "approved");
                                                approve_permit.put("notif_supervisor", FieldValue.delete());
                                                if(level.equals("HSE")){
                                                    approve_permit.put("notif_manager", FieldValue.delete());
                                                }else if(level.equals("Manager")){
                                                    approve_permit.put("notif_hse", FieldValue.delete());
                                                }
                                            }else{
                                                if(level.equals("Manager")){
                                                    approve_permit.put("notif_hse", FieldValue.delete());
                                                    approve_permit.put("notif_supervisor", FieldValue.delete());
                                                    approve_permit.put("status", "approved");
                                                }else{
                                                    approve_permit.put("status", "waiting");
                                                }
                                            }
                                        }else{
                                            if(level.equals("HSE")){
                                                approve_permit.put("notif_manager", FieldValue.delete());
                                            }else{
                                                approve_permit.put("notif_hse", FieldValue.delete());
                                            }
                                            approve_permit.put("status", "waiting");
                                        }
                                    }

                                    db.collection("permit").document(permit_uid).update(approve_permit)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPermitActivity.this);
                                                        alertDialogBuilder.setTitle("Message");
                                                        alertDialogBuilder.setMessage("Work Permit berhasil diapprove!");
                                                        alertDialogBuilder.setCancelable(false);
                                                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent intent = new Intent(DetailPermitActivity.this, PermitActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        final Map<String, Object> review_permit = new HashMap<>();

        if(level.equals("Manager")){
            review_permit.put("Man_approve", "reviewed");
            review_permit.put("status_manager", "review");
        }else if(level.equals("HSE")){
            review_permit.put("Man_approve", "reviewed");
            review_permit.put("status_hse", "review");
        }

        detail_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("permit").document(permit_uid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot snapshot = task.getResult();

                                    if(snapshot.getString("Man_approve") != null && snapshot.getString("Man_approve").equals("reviewed")){
                                        if(snapshot.getString("Hse_approve") != null && snapshot.getString("Hse_approve").equals("reviewed")){
                                            review_permit.put("status", "reviewed");
                                            review_permit.put("notif_supervisor", FieldValue.delete());
                                            if(level.equals("HSE")){
                                                review_permit.put("notif_manager", FieldValue.delete());
                                            }else if(level.equals("Manager")){
                                                review_permit.put("notif_hse", FieldValue.delete());
                                            }
                                        }else{
                                            if(level.equals("HSE")){
                                                review_permit.put("notif_supervisor", FieldValue.delete());
                                                review_permit.put("notif_manager", FieldValue.delete());
                                                review_permit.put("status", "reviewed");
                                            }else{
                                                review_permit.put("status", "waiting");
                                            }
                                        }
                                    }else{
                                        if(snapshot.getString("Hse_approve") != null && snapshot.getString("Hse_approve").equals("reviewed")){
                                            if(snapshot.getString("Man_approve") != null && snapshot.getString("Man_approve").equals("reviewed")){
                                                review_permit.put("status", "reviewed");
                                                review_permit.put("notif_supervisor", FieldValue.delete());
                                                if(level.equals("HSE")){
                                                    review_permit.put("notif_manager", FieldValue.delete());
                                                }else if(level.equals("Manager")){
                                                    review_permit.put("notif_hse", FieldValue.delete());
                                                }
                                            }else{
                                                if(level.equals("Manager")){
                                                    review_permit.put("notif_supervisor", FieldValue.delete());
                                                    review_permit.put("notif_hse", FieldValue.delete());
                                                    review_permit.put("status", "reviewed");
                                                }else{
                                                    review_permit.put("status", "waiting");
                                                }
                                            }
                                        }else{
                                            if(level.equals("HSE")){
                                                review_permit.put("notif_manager", FieldValue.delete());
                                            }else{
                                                review_permit.put("notif_hse", FieldValue.delete());
                                            }
                                            review_permit.put("status", "waiting");
                                        }
                                    }

                                    db.collection("permit").document(permit_uid).update(review_permit)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPermitActivity.this);
                                                        alertDialogBuilder.setTitle("Message");
                                                        alertDialogBuilder.setMessage("Work Permit berhasil direview!");
                                                        alertDialogBuilder.setCancelable(false);
                                                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent intent = new Intent(DetailPermitActivity.this, PermitActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        final Map<String, Object> reject_permit = new HashMap<>();
        if(level.equals("Manager")){
            reject_permit.put("Man_approve", "rejected");
            reject_permit.put("status_manager", "rejected");
        }else if(level.equals("HSE")){
            reject_permit.put("Man_approve", "rejected");
            reject_permit.put("status_hse", "rejected");
        }

        detail_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("permit").document(permit_uid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot snapshot = task.getResult();

                                    if(snapshot.getString("Man_approve") != null && snapshot.getString("Man_approve").equals("rejected")){
                                        if(snapshot.getString("Hse_approve") != null && snapshot.getString("Hse_approve").equals("rejected")){
                                            reject_permit.put("status", "rejected");
                                            reject_permit.put("notif_supervisor", FieldValue.delete());
                                            if(level.equals("HSE")){
                                                reject_permit.put("notif_manager", FieldValue.delete());
                                            }else if(level.equals("Manager")){
                                                reject_permit.put("notif_hse", FieldValue.delete());
                                            }
                                        }else{
                                            if(level.equals("HSE")){
                                                reject_permit.put("notif_supervisor", FieldValue.delete());
                                                reject_permit.put("notif_manager", FieldValue.delete());
                                                reject_permit.put("status", "rejected");
                                            }else{
                                                reject_permit.put("status", "waiting");
                                            }
                                        }
                                    }else{
                                        if(snapshot.getString("Hse_approve") != null && snapshot.getString("Hse_approve").equals("rejected")){
                                            if(snapshot.getString("Man_approve") != null && snapshot.getString("Man_approve").equals("rejected")){
                                                reject_permit.put("status", "rejected");
                                                reject_permit.put("notif_supervisor", FieldValue.delete());
                                                if(level.equals("HSE")){
                                                    reject_permit.put("notif_manager", FieldValue.delete());
                                                }else if(level.equals("Manager")){
                                                    reject_permit.put("notif_hse", FieldValue.delete());
                                                }
                                            }else{
                                                if(level.equals("Manager")){
                                                    reject_permit.put("notif_supervisor", FieldValue.delete());
                                                    reject_permit.put("notif_hse", FieldValue.delete());
                                                    reject_permit.put("status", "rejected");
                                                }else{
                                                    reject_permit.put("status", "waiting");
                                                }
                                            }
                                        }else{
                                            if(level.equals("HSE")){
                                                reject_permit.put("notif_manager", FieldValue.delete());
                                            }else{
                                                reject_permit.put("notif_hse", FieldValue.delete());
                                            }
                                            reject_permit.put("status", "waiting");
                                        }
                                    }

                                    db.collection("permit").document(permit_uid).update(reject_permit)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPermitActivity.this);
                                                        alertDialogBuilder.setTitle("Message");
                                                        alertDialogBuilder.setMessage("Work Permit berhasil direject!");
                                                        alertDialogBuilder.setCancelable(false);
                                                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent intent = new Intent(DetailPermitActivity.this, PermitActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        detail_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value_area_kerja = detail_pengajuan_area_kerja.getText().toString();
                String value_lokasi = detail_pengajuan_lokasi.getText().toString();
                String value_uraian = detail_pengajuan_uraian.getText().toString();
                String value_durasi_sebelum = detail_pengajuan_durasi_sebelum.getText().toString();
                String value_durasi_sesudah = detail_pengajuan_durasi_sesudah.getText().toString();

                int q1Id =  detail_q1.getCheckedRadioButtonId();
                View q1Btn =  detail_q1.findViewById(q1Id);
                int idx1 =  detail_q1.indexOfChild(q1Btn);
                RadioButton rq1 = (RadioButton)  detail_q1.getChildAt(idx1);

                int q2Id = detail_q2.getCheckedRadioButtonId();
                View q2Btn = detail_q2.findViewById(q2Id);
                int idx2 = detail_q2.indexOfChild(q2Btn);
                RadioButton rq2 = (RadioButton) detail_q2.getChildAt(idx2);

                int q3Id = detail_q3.getCheckedRadioButtonId();
                View q3Btn = detail_q3.findViewById(q3Id);
                int idx3 = detail_q3.indexOfChild(q3Btn);
                RadioButton rq3 = (RadioButton) detail_q3.getChildAt(idx3);

                int q4Id = detail_q4.getCheckedRadioButtonId();
                View q4Btn = detail_q4.findViewById(q4Id);
                int idx4 = detail_q4.indexOfChild(q4Btn);
                RadioButton rq4 = (RadioButton) detail_q4.getChildAt(idx4);

                int q5Id = detail_q5.getCheckedRadioButtonId();
                View q5Btn = detail_q5.findViewById(q5Id);
                int idx5 = detail_q5.indexOfChild(q5Btn);
                RadioButton rq5 = (RadioButton) detail_q5.getChildAt(idx5);

                int q6Id = detail_q6.getCheckedRadioButtonId();
                View q6Btn = detail_q6.findViewById(q6Id);
                int idx6 = detail_q6.indexOfChild(q6Btn);
                RadioButton rq6 = (RadioButton) detail_q6.getChildAt(idx6);

                int q7Id = detail_q7.getCheckedRadioButtonId();
                View q7Btn = detail_q7.findViewById(q7Id);
                int idx7 = detail_q7.indexOfChild(q7Btn);
                RadioButton rq7 = (RadioButton) detail_q7.getChildAt(idx7);

                int q8Id = detail_q8.getCheckedRadioButtonId();
                View q8Btn = detail_q8.findViewById(q8Id);
                int idx8 = detail_q8.indexOfChild(q8Btn);
                RadioButton rq8 = (RadioButton) detail_q8.getChildAt(idx8);

                int q9Id = detail_q9.getCheckedRadioButtonId();
                View q9Btn = detail_q9.findViewById(q9Id);
                int idx9 = detail_q9.indexOfChild(q9Btn);
                RadioButton rq9 = (RadioButton) detail_q9.getChildAt(idx9);

                int q10Id = detail_q10.getCheckedRadioButtonId();
                View q10Btn = detail_q10.findViewById(q10Id);
                int idx10 = detail_q10.indexOfChild(q10Btn);
                RadioButton rq10 = (RadioButton) detail_q10.getChildAt(idx10);

                int q11Id = detail_q11.getCheckedRadioButtonId();
                View q11Btn = detail_q11.findViewById(q11Id);
                int idx11 = detail_q11.indexOfChild(q11Btn);
                RadioButton rq11 = (RadioButton) detail_q11.getChildAt(idx11);

                int q12Id = detail_q12.getCheckedRadioButtonId();
                View q12Btn = detail_q12.findViewById(q12Id);
                int idx12 = detail_q12.indexOfChild(q12Btn);
                RadioButton rq12 = (RadioButton) detail_q12.getChildAt(idx12);

                int q13Id = detail_q13.getCheckedRadioButtonId();
                View q13Btn = detail_q13.findViewById(q13Id);
                int idx13 = detail_q13.indexOfChild(q13Btn);
                RadioButton rq13 = (RadioButton) detail_q13.getChildAt(idx13);

                int q14Id = detail_q14.getCheckedRadioButtonId();
                View q14Btn = detail_q14.findViewById(q14Id);
                int idx14 = detail_q14.indexOfChild(q14Btn);
                RadioButton rq14 = (RadioButton) detail_q14.getChildAt(idx14);

                int q15Id = detail_q15.getCheckedRadioButtonId();
                View q15Btn = detail_q15.findViewById(q15Id);
                int idx15 = detail_q15.indexOfChild(q15Btn);
                RadioButton rq15 = (RadioButton) detail_q15.getChildAt(idx15);

                int q16Id = detail_q16.getCheckedRadioButtonId();
                View q16Btn = detail_q16.findViewById(q16Id);
                int idx16 = detail_q16.indexOfChild(q16Btn);
                RadioButton rq16 = (RadioButton) detail_q16.getChildAt(idx16);

                int q17Id = detail_q17.getCheckedRadioButtonId();
                View q17Btn = detail_q17.findViewById(q17Id);
                int idx17 = detail_q17.indexOfChild(q17Btn);
                RadioButton rq17 = (RadioButton) detail_q17.getChildAt(idx17);

                int q18Id = detail_q18.getCheckedRadioButtonId();
                View q18Btn = detail_q18.findViewById(q18Id);
                int idx18 = detail_q18.indexOfChild(q18Btn);
                RadioButton rq18 = (RadioButton) detail_q18.getChildAt(idx18);

                String value_q1 = rq1.getText().toString();
                String value_q2 = rq2.getText().toString();
                String value_q3 = rq3.getText().toString();
                String value_q4 = rq4.getText().toString();
                String value_q5 = rq5.getText().toString();
                String value_q6 = rq6.getText().toString();
                String value_q7 = rq7.getText().toString();
                String value_q8 = rq8.getText().toString();
                String value_q9 = rq9.getText().toString();
                String value_q10 = rq10.getText().toString();
                String value_q11 = rq11.getText().toString();
                String value_q12 = rq12.getText().toString();
                String value_q13 = rq13.getText().toString();
                String value_q14 = rq14.getText().toString();
                String value_q15 = rq15.getText().toString();
                String value_q16 = rq16.getText().toString();
                String value_q17 = rq17.getText().toString();
                String value_q18 = rq18.getText().toString();

                Map<String, Object> update_permit = new HashMap<>();
                update_permit.put("area_kerja", value_area_kerja);
                update_permit.put("lokasi", value_lokasi);
                update_permit.put("uraian", value_uraian);
                update_permit.put("durasi_sebelum", value_durasi_sebelum);
                update_permit.put("durasi_sesudah", value_durasi_sesudah);
                update_permit.put("q1", value_q1);
                update_permit.put("q2", value_q2);
                update_permit.put("q3", value_q3);
                update_permit.put("q4", value_q4);
                update_permit.put("q5", value_q5);
                update_permit.put("q6", value_q6);
                update_permit.put("q7", value_q7);
                update_permit.put("q8", value_q8);
                update_permit.put("q9", value_q9);
                update_permit.put("q10", value_q10);
                update_permit.put("q11", value_q11);
                update_permit.put("q12", value_q12);
                update_permit.put("q13", value_q13);
                update_permit.put("q14", value_q14);
                update_permit.put("q15", value_q15);
                update_permit.put("q16", value_q16);
                update_permit.put("q17", value_q17);
                update_permit.put("q18", value_q18);
                update_permit.put("status", "Waiting");

                db.collection("permit").document(permit_uid).update(update_permit)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPermitActivity.this);
                                    alertDialogBuilder.setTitle("Message");
                                    alertDialogBuilder.setMessage("Work Permit telah diedit!");
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(DetailPermitActivity.this, PermitActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                                }
                            }
                        });
            }
        });


        if(level.equals("Supervisor")){
            layout_manager_level.setVisibility(View.GONE);
            detail_manager_level.setVisibility(View.GONE);
        }else{
            layout_supervisor_level.setVisibility(View.GONE);
            detail_supervisor_level.setVisibility(View.GONE);
        }

        db.collection("permit").document(permit_uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String no_permit = documentSnapshot.getString("no_permit");
                            String area_kerja = documentSnapshot.getString("area_kerja");
                            String lokasi = documentSnapshot.getString("lokasi");
                            String uraian = documentSnapshot.getString("uraian");
                            String durasi_sebelum = documentSnapshot.getString("durasi_sebelum");
                            String durasi_sesudah = documentSnapshot.getString("durasi_sesudah");
                            String q1 = documentSnapshot.getString("q1");
                            String q2 = documentSnapshot.getString("q2");
                            String q3 = documentSnapshot.getString("q3");
                            String q4 = documentSnapshot.getString("q4");
                            String q5 = documentSnapshot.getString("q5");
                            String q6 = documentSnapshot.getString("q6");
                            String q7 = documentSnapshot.getString("q7");
                            String q8 = documentSnapshot.getString("q8");
                            String q9 = documentSnapshot.getString("q9");
                            String q10 = documentSnapshot.getString("q10");
                            String q11 = documentSnapshot.getString("q11");
                            String q12 = documentSnapshot.getString("q12");
                            String q13 = documentSnapshot.getString("q13");
                            String q14 = documentSnapshot.getString("q14");
                            String q15 = documentSnapshot.getString("q15");
                            String q16 = documentSnapshot.getString("q16");
                            String q17 = documentSnapshot.getString("q17");
                            String q18 = documentSnapshot.getString("q18");
                            String status = documentSnapshot.getString("status");
                            String uid = documentSnapshot.getString("uid");

                            detail_area_kerja.setText(area_kerja);
                            detail_lokasi.setText(lokasi);
                            detail_uraian.setText(uraian);
                            detail_durasi_sebelum.setText(durasi_sebelum);
                            detail_durasi_sesudah.setText(durasi_sesudah);

                            detail_pengajuan_area_kerja.setText(area_kerja);
                            detail_pengajuan_lokasi.setText(lokasi);
                            detail_pengajuan_uraian.setText(uraian);
                            detail_pengajuan_durasi_sebelum.setText(durasi_sebelum);
                            detail_pengajuan_durasi_sesudah.setText(durasi_sesudah);

                            db.collection("users").document(uid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot snapshot = task.getResult();
                                                detail_pengawas.setText(snapshot.getString("name"));
                                            }
                                        }
                                    });

                            if(q1 != null){
                                if(q1.equals("Yes")){
                                    detail_q1.check(R.id.detail_q1_yes);
                                }else{
                                    detail_q1.check(R.id.detail_q1_no);
                                }
                            }

                            if(q2 != null){
                                if(q2.equals("Yes")){
                                    detail_q2.check(R.id.detail_q2_yes);
                                }else{
                                    detail_q2.check(R.id.detail_q2_no);
                                }
                            }

                            if(q3 != null){
                                if(q3.equals("Yes")){
                                    detail_q3.check(R.id.detail_q3_yes);
                                }else{
                                    detail_q3.check(R.id.detail_q3_no);
                                }
                            }

                            if(q4 != null){
                                if(q4.equals("Yes")){
                                    detail_q4.check(R.id.detail_q4_yes);
                                }else{
                                    detail_q4.check(R.id.detail_q4_no);
                                }
                            }

                            if(q5 != null){
                                if(q5.equals("Yes")){
                                    detail_q5.check(R.id.detail_q5_yes);
                                }else{
                                    detail_q5.check(R.id.detail_q5_no);
                                }
                            }

                            if(q6 != null){
                                if(q6.equals("Yes")){
                                    detail_q6.check(R.id.detail_q6_yes);
                                }else{
                                    detail_q6.check(R.id.detail_q6_no);
                                }
                            }

                            if(q7 != null){
                                if(q7.equals("Yes")){
                                    detail_q7.check(R.id.detail_q7_yes);
                                }else{
                                    detail_q7.check(R.id.detail_q7_no);
                                }
                            }

                            if(q8 != null){
                                if(q8.equals("Yes")){
                                    detail_q8.check(R.id.detail_q8_yes);
                                }else{
                                    detail_q8.check(R.id.detail_q8_no);
                                }
                            }

                            if(q9 != null){
                                if(q9.equals("Yes")){
                                    detail_q9.check(R.id.detail_q9_yes);
                                }else{
                                    detail_q9.check(R.id.detail_q9_no);
                                }
                            }

                            if(q10 != null){
                                if(q10.equals("Yes")){
                                    detail_q10.check(R.id.detail_q10_yes);
                                }else{
                                    detail_q10.check(R.id.detail_q10_no);
                                }
                            }

                            if(q11 != null){
                                if(q11.equals("Yes")){
                                    detail_q11.check(R.id.detail_q11_yes);
                                }else{
                                    detail_q11.check(R.id.detail_q11_no);
                                }
                            }

                            if(q12 != null){
                                if(q12.equals("Yes")){
                                    detail_q12.check(R.id.detail_q12_yes);
                                }else{
                                    detail_q12.check(R.id.detail_q12_no);
                                }
                            }

                            if(q13 != null){
                                if(q13.equals("Yes")){
                                    detail_q13.check(R.id.detail_q13_yes);
                                }else{
                                    detail_q13.check(R.id.detail_q13_no);
                                }
                            }

                            if(q14 != null){
                                if(q14.equals("Yes")){
                                    detail_q14.check(R.id.detail_q14_yes);
                                }else{
                                    detail_q14.check(R.id.detail_q14_no);
                                }
                            }

                            if(q15 != null){
                                if(q15.equals("Yes")){
                                    detail_q15.check(R.id.detail_q15_yes);
                                }else{
                                    detail_q15.check(R.id.detail_q15_no);
                                }
                            }

                            if(q16 != null){
                                if(q16.equals("Yes")){
                                    detail_q16.check(R.id.detail_q16_yes);
                                }else{
                                    detail_q16.check(R.id.detail_q16_no);
                                }
                            }

                            if(q17 != null){
                                if(q17.equals("Yes")){
                                    detail_q17.check(R.id.detail_q17_yes);
                                }else{
                                    detail_q17.check(R.id.detail_q17_no);
                                }
                            }

                            if(q18 != null){
                                if(q18.equals("Yes")){
                                    detail_q18.check(R.id.detail_q18_yes);
                                }else{
                                    detail_q18.check(R.id.detail_q18_no);
                                }
                            }
                        } else {
                            Log.d("Error", "get failed with "+task.getException());
                        }
                    }
                });
    }
}