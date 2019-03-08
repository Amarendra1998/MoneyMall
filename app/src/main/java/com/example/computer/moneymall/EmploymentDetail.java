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
import android.widget.Toast;

import com.example.computer.moneymall.Common.Common;
import com.example.computer.moneymall.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EmploymentDetail extends AppCompatActivity {
   private EditText salary,name,nationality;
   private Button mcontinue;
   private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private String CurrentUserId;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_detail);
        salary = (EditText)findViewById(R.id.salary);
        name=(EditText)findViewById(R.id.name);
        nationality = (EditText)findViewById(R.id.nationality);
        mcontinue = (Button)findViewById(R.id.button5);
        mAuth = FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);
       CurrentUserId = mAuth.getCurrentUser().getUid();
        mRoot = FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUserId);
        mRoot.keepSynced(true);
        mcontinue.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              updateinfomation();
          }
      });
    }

    private void updateinfomation() {
        String msetusername = name.getText().toString();
        String msalary= salary.getText().toString();
        String mnationality = nationality.getText().toString();
        if (TextUtils.isEmpty(msetusername)){
            Toast.makeText(EmploymentDetail.this,"Please write your username...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(msalary)){
            Toast.makeText(EmploymentDetail.this,"Please enter your salary...",Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(mnationality)){
            Toast.makeText(EmploymentDetail.this,"Please enter your nationality...",Toast.LENGTH_SHORT).show();
        }
        else {
                Map<String,Object> profilemap =new HashMap<>();
                profilemap.put("username",msetusername);
                profilemap.put("salary",msalary);
                profilemap.put("nationality",mnationality);
                mRoot.setValue(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            SendUserToMainActivity();
                            loadingbar.dismiss();
                            Toast.makeText(EmploymentDetail.this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            loadingbar.dismiss();
                            Toast.makeText(EmploymentDetail.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
    }
    private void SendUserToMainActivity() {
        Intent loginInt = new Intent(EmploymentDetail.this,Detail.class);
        //loginInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginInt);
        finish();
    }

}
