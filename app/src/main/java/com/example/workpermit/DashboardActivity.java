package com.example.workpermit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Collections;

public class DashboardActivity extends AppCompatActivity {

    private Session session;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView nav_nama, nav_level;
    private FirebaseFirestore db;
    private int count_notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        db = FirebaseFirestore.getInstance();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_notification, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View hView = navigationView.getHeaderView(0);

        nav_nama = hView.findViewById(R.id.nav_nama);
        nav_level = hView.findViewById(R.id.nav_level);
        Menu menu = navigationView.getMenu();

        final MenuItem nav_notification = menu.findItem(R.id.nav_notification);

        session = new Session(this);

        nav_nama.setText(session.getName());
        nav_level.setText(session.getLevel());

        if(session.getLevel().equals("Supervisor")){
            db.collection("permit").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    count_notif = 0;
                    for(QueryDocumentSnapshot doc : value){
                        if(doc.getString("notif_supervisor") == null){
                            if(doc.getString("uid").equals(session.getUid())){
                                count_notif = count_notif + 1;
                            }
                        }
                    }
                    nav_notification.setTitle("Notification (" + String.valueOf(count_notif) + ")");
                }
            });
        }else if(session.getLevel().equals("Manager")){
            db.collection("permit").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    count_notif = 0;
                    for(QueryDocumentSnapshot doc : value){
                        if(doc.getString("notif_manager") == null){
                            count_notif = count_notif + 1;
                        }
                    }
                    nav_notification.setTitle("Notification (" + String.valueOf(count_notif) + ")");
                }
            });
        }else if(session.getLevel().equals("HSE")){
            db.collection("permit").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    count_notif = 0;
                    for(QueryDocumentSnapshot doc : value){
                        if(doc.getString("notif_hse") == null){
                            count_notif = count_notif + 1;
                        }
                    }
                    nav_notification.setTitle("Notification (" + String.valueOf(count_notif) + ")");
                }
            });
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_notification){
                    Intent i = new Intent(DashboardActivity.this, HistoryPermitActivity.class);
                    startActivity(i);
                }else if(id == R.id.nav_logout){
                    Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}