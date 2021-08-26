package com.example.workpermit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PermitHolder extends RecyclerView.ViewHolder {
    TextView permit_no_value, permit_area_kerja_value, permit_status_value;
    LinearLayoutCompat notification_permit;
    ImageView notification_icon;

    public PermitHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

        permit_no_value = itemView.findViewById(R.id.permit_no_value);
        permit_area_kerja_value = itemView.findViewById(R.id.permit_area_kerja_value);
        permit_status_value = itemView.findViewById(R.id.permit_status_value);
        notification_permit = itemView.findViewById(R.id.notification_permit);
        notification_icon = itemView.findViewById(R.id.notification_icon);
    }

    private PermitHolder.ClickListener mClickListener;
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(PermitHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
