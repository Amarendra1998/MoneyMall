package com.example.computer.moneymall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computer.moneymall.Common.Common;
import com.example.computer.moneymall.Model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;


import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.computer.moneymall.Common.Common.currentUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "FACELOG";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    Spinner spinner;
    private String mVerificationId;
    ProgressDialog progressDialog;
    private SignInButton signInButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 2;
    private Button msend,btnSignup,mverify;
    private TextView textslogan;
    FirebaseDatabase database;
    String UserId;
    DatabaseReference users;
    private EditText edtphone,mcode;
    private TextView textView;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton loginButton = findViewById(R.id.login_button);
        signInButton = (SignInButton)findViewById(R.id.google_btn);
        msend = (Button)findViewById(R.id.button2);
        btnSignup = (Button)findViewById(R.id.button3);
        mverify = (Button)findViewById(R.id.button10);
        progressDialog = new ProgressDialog(this);
        edtphone = (EditText)findViewById(R.id.editPhone);
        mcode = (EditText)findViewById(R.id.editText3);
        mverify.setVisibility(View.INVISIBLE);
        mcode.setVisibility(View.INVISIBLE);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
       // users = database.getReference("User");
       // UserId = mAuth.getCurrentUser().getUid();

        authStateListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(LoginActivity.this,Home.class));
                }
            }
        };
        table_user = database.getReference().child("User");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                      Toast.makeText(LoginActivity.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.computer.moneymall",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryCode.countryname));
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (Common.isConnectedToInternet(getBaseContext())) {
                            String code = CountryCode.countrycode[spinner.getSelectedItemPosition()];
                            String mobile = edtphone.getText().toString().trim();

                            if(mobile.isEmpty() || mobile.length() < 10){
                                edtphone.setError("Enter a valid mobile");
                                edtphone.requestFocus();
                                return;
                            }
                            String phonenumber = "+" + code + mobile;
                            mverify.setVisibility(View.VISIBLE);
                            mcode.setVisibility(View.VISIBLE);
                            msend.setVisibility(View.INVISIBLE);
                            edtphone.setVisibility(View.INVISIBLE);
                            sendVerificationCode(phonenumber);

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                            return;
                        }


            }
        });

        mverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mcode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    mcode.setError("Enter valid code");
                    mcode.requestFocus();
                    return;
                }
                progressDialog.dismiss();
                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                mcode.setText(code);
                //verifying the code
                progressDialog.dismiss();
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String userid = user.getUid();
                            String mphone = edtphone.getText().toString();
                            Map<String,Object> profilemap =new HashMap<>();
                            profilemap.put("phone",mphone);
                            table_user.child(userid).setValue(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, CreditScore.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(LoginActivity.this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(LoginActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //verification successful we will start the profile activity


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent =Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(authStateListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(authStateListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            updateUI();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    private void updateUI() {
        Toast.makeText(LoginActivity.this,"You are logged in...",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this,CreditScore.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
           GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           if (result.isSuccess()){
               GoogleSignInAccount account = result.getSignInAccount();
               firebaseAuthWithGoogle(account);
           }else {
               Toast.makeText(LoginActivity.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
               updateUI();
           }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.action_settings), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI();
                        }

                        // ...
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }



}
