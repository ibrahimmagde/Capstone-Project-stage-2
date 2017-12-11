package com.hema.mypetslover;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password,name;
    private Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ( (MyApplication) getApplication()).startTracking();

        mAuth = FirebaseAuth.getInstance(); // important Call

        signin = (Button)findViewById(R.id.signin);
        signup = (Button)findViewById(R.id.signup);
        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etPassword);

        //Check if User is Already LoggedIn
        if(mAuth.getCurrentUser() != null)
        {
            //User NOT logged In
            finish();
            startActivity(new Intent(getApplicationContext(),ShowData.class));
        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getemail = email.getText().toString().trim();
                String getepassword = password.getText().toString().trim();

                if( getemail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"please enter your email",Toast.LENGTH_LONG).show();
                }
                else if(getepassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"please enter your password",Toast.LENGTH_LONG).show();
                }
                else {

                    callsignin(getemail, getepassword);
                }


            }
        });


    }




    //Now start Sign In Process
    //SignIn Process
    private void callsignin(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "sign In Successful:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TESTING", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent i = new Intent(MainActivity.this, ShowData.class);
                            finish();
                            startActivity(i);
                        }
                    }
                });

    }

    public void goSignup(View v){

        startActivity(new Intent(this,SignUp.class));
    }


}
