package com.example.workpermit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PengajuanPermitActivity extends AppCompatActivity {

    private EditText pengajuan_area_kerja, pengajuan_lokasi, pengajuan_uraian, pengajuan_durasi_sebelum, pengajuan_durasi_sesudah;
    private TextView pengajuan_pengawas;
    private Button pengajuan_submit;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog loading;
    private Session session;
    int count;
    private RadioGroup q1, q2, q3, q4, q5, q6,q7, q8, q9,q10, q11, q12, q13, q14, q15,q16, q17, q18;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_permit);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        session = new Session(this);

        final FirebaseUser user = mAuth.getCurrentUser();

        pengajuan_area_kerja = findViewById(R.id.pengajuan_area_kerja);
        pengajuan_lokasi = findViewById(R.id.pengajuan_lokasi);
        pengajuan_uraian = findViewById(R.id.pengajuan_uraian);
        pengajuan_durasi_sebelum = findViewById(R.id.pengajuan_durasi_sebelum);
        pengajuan_durasi_sesudah = findViewById(R.id.pengajuan_durasi_sesudah);
        pengajuan_submit = findViewById(R.id.pengajuan_submit);
        pengajuan_pengawas = findViewById(R.id.pengajuan_pengawas);

        pengajuan_pengawas.setText(session.getName());

        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);
        q6 = findViewById(R.id.q6);
        q7 = findViewById(R.id.q7);
        q8 = findViewById(R.id.q8);
        q9 = findViewById(R.id.q9);
        q10 = findViewById(R.id.q10);
        q11 = findViewById(R.id.q11);
        q12 = findViewById(R.id.q12);
        q13 = findViewById(R.id.q13);
        q14 = findViewById(R.id.q14);
        q15 = findViewById(R.id.q15);
        q16 = findViewById(R.id.q16);
        q17 = findViewById(R.id.q17);
        q18 = findViewById(R.id.q18);

        db.collection("permit").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document: task.getResult()){
                                count++;
                            }
                            count++;
                        }
                    }
                });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                updateLabel();
            }
        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                updateLabel2();
            }
        };
        pengajuan_durasi_sebelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PengajuanPermitActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pengajuan_durasi_sesudah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PengajuanPermitActivity.this, date2, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pengajuan_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value_area_kerja = pengajuan_area_kerja.getText().toString();
                String value_lokasi = pengajuan_lokasi.getText().toString();
                String value_uraian = pengajuan_uraian.getText().toString();
                String value_durasi_sebelum = pengajuan_durasi_sebelum.getText().toString();
                String value_durasi_sesudah = pengajuan_durasi_sesudah.getText().toString();
                String value_count = "PTW"+String.format("%02d", count);


                int q1Id = q1.getCheckedRadioButtonId();
                View q1Btn = q1.findViewById(q1Id);
                int idx1 = q1.indexOfChild(q1Btn);
                RadioButton rq1 = (RadioButton) q1.getChildAt(idx1);

                int q2Id = q2.getCheckedRadioButtonId();
                View q2Btn = q2.findViewById(q2Id);
                int idx2 = q2.indexOfChild(q2Btn);
                RadioButton rq2 = (RadioButton) q2.getChildAt(idx2);

                int q3Id = q3.getCheckedRadioButtonId();
                View q3Btn = q3.findViewById(q3Id);
                int idx3 = q3.indexOfChild(q3Btn);
                RadioButton rq3 = (RadioButton) q3.getChildAt(idx3);

                int q4Id = q4.getCheckedRadioButtonId();
                View q4Btn = q4.findViewById(q4Id);
                int idx4 = q4.indexOfChild(q4Btn);
                RadioButton rq4 = (RadioButton) q4.getChildAt(idx4);

                int q5Id = q5.getCheckedRadioButtonId();
                View q5Btn = q5.findViewById(q5Id);
                int idx5 = q5.indexOfChild(q5Btn);
                RadioButton rq5 = (RadioButton) q5.getChildAt(idx5);

                int q6Id = q6.getCheckedRadioButtonId();
                View q6Btn = q6.findViewById(q6Id);
                int idx6 = q6.indexOfChild(q6Btn);
                RadioButton rq6 = (RadioButton) q6.getChildAt(idx6);

                int q7Id = q7.getCheckedRadioButtonId();
                View q7Btn = q7.findViewById(q7Id);
                int idx7 = q7.indexOfChild(q7Btn);
                RadioButton rq7 = (RadioButton) q7.getChildAt(idx7);

                int q8Id = q8.getCheckedRadioButtonId();
                View q8Btn = q8.findViewById(q8Id);
                int idx8 = q8.indexOfChild(q8Btn);
                RadioButton rq8 = (RadioButton) q8.getChildAt(idx8);

                int q9Id = q9.getCheckedRadioButtonId();
                View q9Btn = q9.findViewById(q9Id);
                int idx9 = q9.indexOfChild(q9Btn);
                RadioButton rq9 = (RadioButton) q9.getChildAt(idx9);

                int q10Id = q10.getCheckedRadioButtonId();
                View q10Btn = q10.findViewById(q10Id);
                int idx10 = q10.indexOfChild(q10Btn);
                RadioButton rq10 = (RadioButton) q10.getChildAt(idx10);

                int q11Id = q11.getCheckedRadioButtonId();
                View q11Btn = q11.findViewById(q11Id);
                int idx11 = q11.indexOfChild(q11Btn);
                RadioButton rq11 = (RadioButton) q11.getChildAt(idx11);

                int q12Id = q12.getCheckedRadioButtonId();
                View q12Btn = q12.findViewById(q12Id);
                int idx12 = q12.indexOfChild(q12Btn);
                RadioButton rq12 = (RadioButton) q12.getChildAt(idx12);

                int q13Id = q13.getCheckedRadioButtonId();
                View q13Btn = q13.findViewById(q13Id);
                int idx13 = q13.indexOfChild(q13Btn);
                RadioButton rq13 = (RadioButton) q13.getChildAt(idx13);

                int q14Id = q14.getCheckedRadioButtonId();
                View q14Btn = q14.findViewById(q14Id);
                int idx14 = q14.indexOfChild(q14Btn);
                RadioButton rq14 = (RadioButton) q14.getChildAt(idx14);

                int q15Id = q15.getCheckedRadioButtonId();
                View q15Btn = q15.findViewById(q15Id);
                int idx15 = q15.indexOfChild(q15Btn);
                RadioButton rq15 = (RadioButton) q15.getChildAt(idx15);

                int q16Id = q16.getCheckedRadioButtonId();
                View q16Btn = q16.findViewById(q16Id);
                int idx16 = q16.indexOfChild(q16Btn);
                RadioButton rq16 = (RadioButton) q16.getChildAt(idx16);

                int q17Id = q17.getCheckedRadioButtonId();
                View q17Btn = q17.findViewById(q17Id);
                int idx17 = q17.indexOfChild(q17Btn);
                RadioButton rq17 = (RadioButton) q17.getChildAt(idx17);

                int q18Id = q18.getCheckedRadioButtonId();
                View q18Btn = q18.findViewById(q18Id);
                int idx18 = q18.indexOfChild(q18Btn);
                RadioButton rq18 = (RadioButton) q18.getChildAt(idx18);


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

                Map<String, Object> permit = new HashMap<>();
                permit.put("area_kerja", value_area_kerja);
                permit.put("lokasi", value_lokasi);
                permit.put("uraian", value_uraian);
                permit.put("durasi_sebelum", value_durasi_sebelum);
                permit.put("durasi_sesudah", value_durasi_sesudah);
                permit.put("uid", user.getUid());
                permit.put("no_permit", value_count);
                permit.put("q1", value_q1);
                permit.put("q2", value_q2);
                permit.put("q3", value_q3);
                permit.put("q4", value_q4);
                permit.put("q5", value_q5);
                permit.put("q6", value_q6);
                permit.put("q7", value_q7);
                permit.put("q8", value_q8);
                permit.put("q9", value_q9);
                permit.put("q10", value_q10);
                permit.put("q11", value_q11);
                permit.put("q12", value_q12);
                permit.put("q13", value_q13);
                permit.put("q14", value_q14);
                permit.put("q15", value_q15);
                permit.put("q16", value_q16);
                permit.put("q17", value_q17);
                permit.put("q18", value_q18);
                permit.put("timestamp", FieldValue.serverTimestamp());
                permit.put("status", "waiting");
                permit.put("status_manager", "waiting");
                permit.put("status_hse", "waiting");

                db.collection("permit").add(permit)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PengajuanPermitActivity.this);
                                    alertDialogBuilder.setTitle("Message");
                                    alertDialogBuilder.setMessage("Work Permit telah disubmit, menunggu verifikasi dari manager!");
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(PengajuanPermitActivity.this, PermitActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                                }
                            }
                        });
            }
        });
    }

    private void updateLabel2() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        pengajuan_durasi_sesudah.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabel() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        pengajuan_durasi_sebelum.setText(sdf.format(calendar.getTime()));
    }
}