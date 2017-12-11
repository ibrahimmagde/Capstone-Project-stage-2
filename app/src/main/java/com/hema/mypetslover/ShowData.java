package com.hema.mypetslover;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.direction;
import static java.security.AccessController.getContext;



public class ShowData extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference myRef2;

    private FirebaseAuth mAuth;
    FirebaseUser user;

    int x;

    private FirebaseRecyclerAdapter<ShowDataItems, ShowDataViewHolder> mFirebaseAdapter;


    static   SharedPreferences.Editor editor,editor2;
    String mk="";

    ArrayList<String> pl;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_data_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        pl=new ArrayList<>();


        editor = getSharedPreferences("SHARED", MODE_PRIVATE).edit();



        Firebase.setAndroidContext(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance(); // important Call
        user = mAuth.getCurrentUser();
        myRef = firebaseDatabase.getReference("User_Details");
        myRef2 = FirebaseDatabase.getInstance().getReference(user.getUid());


        recyclerView = (RecyclerView) findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowData.this));
        Toast.makeText(ShowData.this, "Wait !  Fetching List...", Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d("LOGGED", "IN onStart ");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems,
                ShowDataViewHolder>(ShowDataItems.class, R.layout.show_data_single_item_general, ShowDataViewHolder.class, myRef) {

            public void populateViewHolder(final ShowDataViewHolder viewHolder, final ShowDataItems model, final int position) {


                viewHolder.Image_URL(model.getImage_URL());
                viewHolder.Image_Title(model.getImage_Title());
                viewHolder.Phone(model.getPhone());
                viewHolder.Email(model.getEmail());
                viewHolder.District(model.getDistrict());
                viewHolder.SearchFor(model.getGender());


            }



        };


        recyclerView.setAdapter(mFirebaseAdapter);
    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title;
        private final ImageView image_url;
        private final TextView  phone,email,district,searchfor,gender;





        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            image_url = (ImageView) itemView.findViewById(R.id.fetch_image);
            image_title = (TextView) itemView.findViewById(R.id.fetch_image_title);
            phone = (TextView) itemView.findViewById(R.id.phone);
            email = (TextView) itemView.findViewById(R.id.email);
            district = (TextView) itemView.findViewById(R.id.district);
            searchfor = (TextView) itemView.findViewById(R.id.searchfor);
            gender = (TextView) itemView.findViewById(R.id.gender);




        }

        private void Image_Title(String title) {
            image_title.setText(title);
        }

        private void Image_URL(String title) {
            // image_url.setImageResource(R.drawable.loading);
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    .placeholder(R.drawable.loading)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_url);
        }

        private void Phone(String phones){
            phone.setText(phones);
        }
        private void Email(String emails){
            email.setText(emails);
        }
        private void District(String districts){
            district.setText(districts);
        }
        private void SearchFor(String genders){
            gender.setText(genders);
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
                startActivity(new Intent(this, myPets.class));
                return true;
            case R.id.upload_pets:
                startActivity(new Intent(this, Uploadinfo.class));
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