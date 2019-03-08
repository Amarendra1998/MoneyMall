package com.example.computer.moneymall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Detail extends AppCompatActivity {
    private EditText salary,salary1,salary2,salary3;
    private Button back,mcontinue;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mauth;
    private DatabaseReference mRoot;
    private String CurrentUserId;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mauth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
       CurrentUserId = mauth.getCurrentUser().getUid();
        mRoot = FirebaseDatabase.getInstance().getReference("User").child(CurrentUserId);
        salary= (EditText)findViewById(R.id.salary);
        salary1= (EditText)findViewById(R.id.salary1);
        salary2= (EditText)findViewById(R.id.salary2);
        salary3= (EditText)findViewById(R.id.salary3);
        back= (Button) findViewById(R.id.button6);
        mcontinue= (Button) findViewById(R.id.button7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginInt = new Intent(Detail.this,EmploymentDetail.class);
                loginInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginInt);
                finish();
            }
        });
        mcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfomation();
            }
        });
    }

    private void updateinfomation() {
        String msalary = salary.getText().toString();
        String msalary1= salary1.getText().toString();
        String msalary2 = salary2.getText().toString();
        String msalary3 = salary3.getText().toString();
        if (TextUtils.isEmpty(msalary)){
            Toast.makeText(Detail.this,"Please write your income...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(msalary1)){
            Toast.makeText(Detail.this,"Please enter your salary...",Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(msalary2)){
            Toast.makeText(Detail.this,"Please enter your salary...",Toast.LENGTH_SHORT).show();
        }if (TextUtils.isEmpty(msalary3)){
            Toast.makeText(Detail.this,"Please enter your salary...",Toast.LENGTH_SHORT).show();
        }
        else {
            Map<String,Object> profilemap =new HashMap<>();
            profilemap.put("salary",msalary);
            profilemap.put("salary1",msalary1);
            profilemap.put("salary2",msalary2);
            profilemap.put("salary3",msalary3);
            mRoot.setValue(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        SendUserToDetailsActivity();
                        loadingbar.dismiss();
                        Toast.makeText(Detail.this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = task.getException().toString();
                        loadingbar.dismiss();
                        Toast.makeText(Detail.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void SendUserToDetailsActivity() {
        Intent loginInt = new Intent(Detail.this,Details.class);
        startActivity(loginInt);
        finish();
    }
}
