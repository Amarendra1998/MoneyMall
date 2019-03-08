package com.example.computer.moneymall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computer.moneymall.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Document extends AppCompatActivity {
private ImageButton imageButton1,imageButton2,imageButton3;
private static final int Camera_Request_Code=1;
private StorageReference mStorage;
private ImageView imageView;
private FirebaseUser mCurrentUser;
private FirebaseAuth mauth;
private TextView add,add1,add2,add3,add4;
    FirebaseStorage storage;
    private CircleImageView circleImageView;
    private DatabaseReference mRoot;
    private String CurrentUserId;
    private static final int GalleryPick = 1;
    private StorageReference UserProfileImageRef;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        add = (TextView)findViewById(R.id.add);
        add1 = (TextView)findViewById(R.id.add1);
        add2 = (TextView)findViewById(R.id.add2);
        add3 = (TextView)findViewById(R.id.add3);
        add4 = (TextView)findViewById(R.id.add4);
        mauth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUserId = mauth.getCurrentUser().getUid();
        mRoot = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef=FirebaseStorage.getInstance().getReference().child("Profile Images");
        imageButton1 = (ImageButton)findViewById(R.id.camera);
        loadingbar = new ProgressDialog(this);
        imageView = (ImageView)findViewById(R.id.imageView);
       // Retrieveinfo();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage();
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage();
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,Camera_Request_Code);
            }
        });
    }

    private void Retrieveinfo() {
        mRoot.child("User").child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && ((dataSnapshot.hasChild("image")))) {
                    String retrieveprofile = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retrieveprofile).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void storage(){
      Intent galleryIntent = new Intent();
      galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
      galleryIntent.setType("image/*");
      startActivityForResult(galleryIntent,GalleryPick);
  }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            Uri downloadurls = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please wait your profile image is updating...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                final Uri resulturi = result.getUri();
                final StorageReference filepath = UserProfileImageRef.child( CurrentUserId+".jpg");
                if (resulturi != null) {
                    filepath.putFile(resulturi)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String profileImageUrl = task.getResult().toString();
                                            mRoot.child("User").child(CurrentUserId).child("image").setValue(profileImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        loadingbar.dismiss();
                                                        Toast.makeText(Document.this, "Successfully added in databse.", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(Document.this, "Error in uploading image.", Toast.LENGTH_LONG).show();
                                                        loadingbar.dismiss();
                                                    }
                                                }
                                            });
                                            Log.i("URL", profileImageUrl);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingbar.dismiss();
                                    Toast.makeText(Document.this, "aaa " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }
    }

}