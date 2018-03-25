package com.blog.chhun.learnblog;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageUri = null;

    private Button setupBtn;
    private EditText setupName;

    private StorageReference strRef;
    private FirebaseAuth firebaseAuth;

    private ProgressBar setupProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupImage = findViewById(R.id.setupImage);

        setupName = (EditText) findViewById(R.id.setup_name);
        setupBtn = (Button) findViewById(R.id.setup_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        strRef = FirebaseStorage.getInstance().getReference();

        setupProgressBar = (ProgressBar) findViewById(R.id.setup_progress_bar);

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usrName = setupName.getText().toString();
                if (!TextUtils.isEmpty(usrName) && mainImageUri != null ) {
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    setupProgressBar.setVisibility(View.VISIBLE);

                    StorageReference imagePath = strRef.child("profile_image").child(user_id + ".jpg");

                    imagePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful() ) {
                                Uri download_uri = task.getResult().getDownloadUrl();
                                Toast.makeText(SetupActivity.this, "Image is uploaded.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                            }

                            setupProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }
        });

        android.support.v7.widget.Toolbar setupToolbar = findViewById(R.id.setuoToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Acoount Setup");

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PermissionChecker.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(SetupActivity.this);

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                setupImage.setImageURI(mainImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
