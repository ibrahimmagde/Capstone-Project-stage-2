package com.hema.mypetslover;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.direction;
import static java.security.AccessController.getContext;


public class myPets extends AppCompatActivity {

    FirebaseUser user;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef2;
    DatabaseReference myRef;


    String idd;
    private FirebaseRecyclerAdapter<ShowDataItems, ShowDataViewHolder> mFirebaseAdapter;



    public myPets() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);




        firebaseDatabase = FirebaseDatabase.getInstance();
         myRef = firebaseDatabase.getReference("User_Details");

        mAuth = FirebaseAuth.getInstance(); // important Call
        user= mAuth.getCurrentUser();
        myRef2 = FirebaseDatabase.getInstance().getReference(user.getUid());

        recyclerView = (RecyclerView)findViewById(R.id.mypet_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(myPets.this));

        recyclerView2 = (RecyclerView)findViewById(R.id.show_data_recycler_view);


        Toast.makeText(myPets.this, "Wait !  Fetching List...", Toast.LENGTH_SHORT).show();





    }

    @Override
    public void onStart() {
        super.onStart();


        //Log.d("LOGGED", "IN onStart ");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems,
                ShowDataViewHolder>(ShowDataItems.class, R.layout.show_data_single_item, ShowDataViewHolder.class, myRef2) {

            public void populateViewHolder(final ShowDataViewHolder viewHolder, final ShowDataItems model, final int position) {

                viewHolder.Image_URL(model.getImage_URL());
                viewHolder.Image_Title(model.getImage_Title());
                viewHolder.Phone(model.getPhone());
                viewHolder.Email(model.getEmail());
                viewHolder.District(model.getDistrict());
                viewHolder.SearchFor(model.getGender());




                //OnClick Item
                viewHolder.del.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick (final View v) {

                        DatabaseReference mostafa = mFirebaseAdapter.getRef(position).child("User_Id");

                        mostafa.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                idd = dataSnapshot.getValue(String.class);
                                //do what you want with the email

                                    AlertDialog.Builder builder = new AlertDialog.Builder(myPets.this,R.style.MyDialogTheme);
                                    builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot val : dataSnapshot.getChildren()){


                                                                //This is if your are querying for the hotel child
                                                                if(val.child("Image_URL").getValue(String.class).contains(model.getImage_URL())){
                                                                    //Do what you want with the record
                                                                    val.getRef().removeValue();
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot val : dataSnapshot.getChildren()){


                                                                //This is if your are querying for the hotel child
                                                                if(val.child("Image_URL").getValue(String.class).contains(model.getImage_URL())){
                                                                    //Do what you want with the record
                                                                    val.getRef().removeValue();
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                    int selectedItems = position;

                                                    mFirebaseAdapter.getRef(selectedItems).removeValue();
                                                    mFirebaseAdapter.notifyItemRemoved(selectedItems);
                                                    recyclerView.invalidate();
                                                    onStart();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.setTitle("Confirm");
                               /* dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000"));
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000"));*/



                                dialog.show();
                                }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                });

            }

        };



        recyclerView.setAdapter(mFirebaseAdapter);


    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title;
        private final ImageView image_url;
        private final TextView phone,email,district,searchfor,gender;
        private Button del;





        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            image_url = (ImageView) itemView.findViewById(R.id.fetch_image);
            image_title = (TextView) itemView.findViewById(R.id.fetch_image_title);
            phone = (TextView) itemView.findViewById(R.id.phone);
            email = (TextView) itemView.findViewById(R.id.email);
            district = (TextView) itemView.findViewById(R.id.district);
            searchfor = (TextView) itemView.findViewById(R.id.searchfor);
            gender = (TextView) itemView.findViewById(R.id.gender);
            del=(Button)itemView.findViewById(R.id.dele);

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
