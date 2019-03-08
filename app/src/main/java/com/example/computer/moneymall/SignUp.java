package com.example.computer.moneymall;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.computer.moneymall.Common.Common;
import com.example.computer.moneymall.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private EditText edtpass,edtphone,edtname,edtEmail;
    private Button btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtpass = (EditText)findViewById(R.id.editpass);
        edtphone = (EditText)findViewById(R.id.editphone);
        edtname = (EditText)findViewById(R.id.editname);
        edtEmail = (EditText)findViewById(R.id.editemail);
        btnsignup = (Button)findViewById(R.id.btnsignup);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference().child("User");
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //check if already user phone is registered
                            if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                User user = new User(edtname.getText().toString(),edtpass.getText().toString(),edtEmail.getText().toString());
                                table_user.child(edtphone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(SignUp.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}