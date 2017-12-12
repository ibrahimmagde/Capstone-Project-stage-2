package com.hema.mypetslover;

import android.Manifest;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;


public class UploadInfo extends AppCompatActivity {

    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    static SharedPreferences.Editor editor2;
    Button selectImage, mUploadButton;
    ImageView mUserImage;
    EditText title, contactInfo, email, district;
    RadioGroup mRadioGroup;
    FirebaseUser user;
    String gender;
    private ProgressDialog mProgressDialog;
    private Firebase mRoofRef;
    private Firebase mUser;
    private Uri mImageUri = null;
    private DatabaseReference mdatabaseRef;
    private StorageReference mStorage;
    private String mName, contactINfo, mEmail, mDistrict;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        editor2 = getSharedPreferences("go", MODE_PRIVATE).edit();


        mAuth = FirebaseAuth.getInstance(); // important Call
        user = mAuth.getCurrentUser();
        Firebase.setAndroidContext(this);


        selectImage = (Button) findViewById(R.id.select_image);
        mUploadButton = (Button) findViewById(R.id.upload_bttn);
        mUserImage = (ImageView) findViewById(R.id.user_image);
        title = (EditText) findViewById(R.id.etTitle);
        contactInfo = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        district = (EditText) findViewById(R.id.district);

        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroupp);


        //Initialize the Progress Bar
        mProgressDialog = new ProgressDialog(UploadInfo.this);


        //Select image from External Storage...
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                } else {
                    callgalary();
                }
            }
        });

        //Initialize Firebase Database paths for database and Storage

        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        mRoofRef = new Firebase("https://my-pet-s-lover.firebaseio.com//").child("User_Details").push();  // Push will create new child every time we upload data
        mUser = new Firebase("https://my-pet-s-lover.firebaseio.com//").child(user.getUid()).push();  // Push will create new child every time we upload data

        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://my-pet-s-lover.appspot.com/");


        //Click on Upload Button Title will upload to Database
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = title.getText().toString().trim();
                mEmail = email.getText().toString().trim();
                mDistrict = district.getText().toString().trim();

                contactINfo = contactInfo.getText().toString().trim();
                int selectedId = mRadioGroup.getCheckedRadioButtonId();

                // find which radioButton is checked by id
                if (selectedId == R.id.male) {
                    gender = "Male";


                } else {

                    gender = "Female";
                }


                if (mName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please write the name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mEmail.isEmpty() && mDistrict.isEmpty() && contactINfo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please provide any contact method", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (gender.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please provide the gender", Toast.LENGTH_SHORT).show();
                    return;

                }

                SharedPreferences preferences = getSharedPreferences("val", MODE_PRIVATE);
                int x = preferences.getInt("i", 0);


                Firebase childRef_name = mRoofRef.child("Image_Title");
                childRef_name.setValue(mName);

                Firebase childRef_counter = mRoofRef.child("Counter");
                childRef_counter.setValue("0");

                Firebase mContactInfo = mRoofRef.child("Phone");
                mContactInfo.setValue(contactINfo);

                Firebase mEmailss = mRoofRef.child("Email");
                mEmailss.setValue(mEmail);

                Firebase mDistrictss = mRoofRef.child("District");
                mDistrictss.setValue(mDistrict);

                Firebase mGender = mRoofRef.child("Gender");
                mGender.setValue(gender);

                Firebase childRef_id = mRoofRef.child("User_Id");
                childRef_id.setValue(user.getUid());

                Firebase koko = mUser.child("User_Id");
                koko.setValue(user.getUid());


                Firebase id = mRoofRef.child("ID");
                int c = new Random().nextInt(10);
                id.setValue(String.valueOf(c));

                Firebase id2 = mUser.child("ID");
                id2.setValue(String.valueOf(c));


                Firebase childRef_name2 = mUser.child("Image_Title");
                childRef_name2.setValue(mName);


                Firebase childRef_counter2 = mUser.child("Counter");
                childRef_counter2.setValue("0");


                Firebase mContactInfo1 = mUser.child("Phone");
                mContactInfo1.setValue(contactINfo);


                Firebase mEmail1ss = mUser.child("Email");
                mEmail1ss.setValue(mEmail);


                Firebase mDistrict1ss = mUser.child("District");
                mDistrict1ss.setValue(mDistrict);


                Firebase mGender1 = mUser.child("Gender");
                mGender1.setValue(gender);

                startActivity(new Intent(getApplicationContext(), MyPets.class));

            }
        });

    }


    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }


    //If Access Granted gallery Will open
    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    //After Selecting image from gallery image will directly uploaded to Firebase Database
    //and Image will Show in Image View
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mImageUri = data.getData();


            mUserImage.setImageURI(mImageUri);

            StorageReference filePath = mStorage.child("User_Images").child(mImageUri.getLastPathSegment());


            mProgressDialog.setMessage("Uploading Image....");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error

                    editor2.putString("give", String.valueOf(downloadUri));
                    editor2.apply();
                    Intent intentwidget = new Intent(getApplicationContext(), Widget.class);
                    intentwidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int ids[] = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));
                    intentwidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    getApplicationContext().sendBroadcast(intentwidget);


                    mRoofRef.child("Image_URL").setValue(downloadUri.toString());
                    mUser.child("Image_URL").setValue(downloadUri.toString());

                    Glide.with(getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(mUserImage);
                    Toast.makeText(getApplicationContext(), "Updated.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_pets:
                startActivity(new Intent(this, ShowData.class));
                return true;
            case R.id.my_pets:
                startActivity(new Intent(this, MyPets.class));
                return true;
            case R.id.upload_pets:
                startActivity(new Intent(this, UploadInfo.class));
                return true;
            case R.id.sign_out:
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }


    }
}
