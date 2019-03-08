package com.example.computer.moneymall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner dd,mm,yyyy,income,work;
    private Button mcontinue;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private String CurrentUserId;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        dd = (Spinner)findViewById(R.id.spinner4);
        mm = (Spinner)findViewById(R.id.spinner5);
        yyyy = (Spinner)findViewById(R.id.spinner6);
        income = (Spinner)findViewById(R.id.spinner);
        mcontinue = (Button)findViewById(R.id.button9);
        work = (Spinner)findViewById(R.id.spinner1);
        mAuth = FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);
        CurrentUserId = mAuth.getCurrentUser().getUid();
        mRoot = FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUserId);
        mRoot.keepSynced(true);

        ArrayAdapter<CharSequence>dayAdapter = ArrayAdapter.createFromResource(this,R.array.Date,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence>monthadapter = ArrayAdapter.createFromResource(this,R.array.Month,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence>yearsAdapters = ArrayAdapter.createFromResource(this,R.array.Year,android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence>incomeadapter = ArrayAdapter.createFromResource(this,R.array.Income,android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence>workAdapter = ArrayAdapter.createFromResource(this,R.array.WorkingYears,android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dd.setAdapter(dayAdapter);
        mm.setAdapter(monthadapter);
        yyyy.setAdapter(yearsAdapters);
        income.setAdapter(incomeadapter);
        work.setAdapter(workAdapter);
        dd.setOnItemSelectedListener(this);
        mm.setOnItemSelectedListener(this);
        yyyy.setOnItemSelectedListener(this);
        income.setOnItemSelectedListener(this);
        work.setOnItemSelectedListener(this);

        mcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dd.getSelectedItem().equals(true) && mm.getSelectedItem().equals(true) && yyyy.getSelectedItem().equals(true) && income.getSelectedItem().equals(true)&& work.getSelectedItem().equals(true))
                {
                    Map<String,Object> profilemap =new HashMap<>();
                    profilemap.put("Date",dd.getSelectedItemPosition());
                    profilemap.put("Month",mm.getSelectedItemPosition());
                    profilemap.put("Year",yyyy.getSelectedItemPosition());
                    profilemap.put("Income",income.getSelectedItemPosition());
                    profilemap.put("Work Year",work.getSelectedItemPosition());
                    mRoot.setValue(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToFinalDetailActivity();
                                loadingbar.dismiss();
                                Toast.makeText(Details.this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                loadingbar.dismiss();
                                Toast.makeText(Details.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void SendUserToFinalDetailActivity() {

        Intent loginInt = new Intent(Details.this,FinalDetail.class);
        startActivity(loginInt);
        finish();
    }
}
