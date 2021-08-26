package com.example.workpermit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermitAdapter extends RecyclerView.Adapter<PermitHolder> {

    List<PermitModel> permitModels;
    PermitActivity permitActivity;
    Context context;
    FirebaseFirestore db;

    public PermitAdapter(List<PermitModel> permitModels, PermitActivity permitActivity) {
        this.permitModels = permitModels;
        this.permitActivity = permitActivity;
    }

    @NonNull
    @Override
    public PermitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.permit_list, parent, false);

        PermitHolder permitHolder = new PermitHolder(view);

        permitHolder.setOnClickListener(new PermitHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final String no_permit = permitModels.get(position).getNo_permit();
                final String uid = permitModels.get(position).getUid();
                Session session = new Session(permitActivity);

                Map<String, Object> notification_update = new HashMap<>();
                if(session.getLevel().equals("Supervisor")){
                    notification_update.put("notif_supervisor", session.getUid());
                }else if(session.getLevel().equals("Manager")){
                    notification_update.put("notif_manager", session.getUid());
                }else if(session.getLevel().equals("HSE")){
                    notification_update.put("notif_hse", session.getUid());
                }

                Log.d("NOTIFICATION", session.getUid());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("permit").document(permitModels.get(position).getUid()).update(notification_update)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(permitActivity, DetailPermitActivity.class);
                                intent.putExtra("no_permit", no_permit);
                                intent.putExtra("permit_uid", uid);

                                permitActivity.startActivity(intent);
                            }
                        });
            }

            @Override
            public void onItemLongClick(final View view, int position) {
                String no_permit = permitModels.get(position).getNo_permit();
                final String area_kerja = permitModels.get(position).getArea_kerja();
                String status = permitModels.get(position).getStatus();
                final String uid = permitModels.get(position).getUid();

                final CharSequence[] items = {"Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("Select Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db = FirebaseFirestore.getInstance();
                        db.collection("permit").document(uid)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(), "Successful Delete", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "Failed Delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.show();
            }
        });

        return permitHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PermitHolder holder, int position) {
        holder.permit_no_value.setText(permitModels.get(position).getNo_permit());
        holder.permit_area_kerja_value.setText(permitModels.get(position).getArea_kerja());
        holder.permit_status_value.setText(permitModels.get(position).getStatus());

        if(permitModels.get(position).getUserUid() == null){
            holder.notification_icon.setImageResource(R.drawable.ic_baseline_notifications_24);
        }else{
            holder.notification_icon.setImageResource(R.drawable.ic_baseline_notifications_white_24);
        }
    }

    @Override
    public int getItemCount() {
        return permitModels.size();
    }
}