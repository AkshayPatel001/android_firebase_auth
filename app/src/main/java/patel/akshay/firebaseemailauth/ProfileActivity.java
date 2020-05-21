package patel.akshay.firebaseemailauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextView email, verified;
    private Button btnLogout, btnVerify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init method is used to initialize all the widgets in the xml file
        //it will be easy to to find the name of any widget in case if we forget the name
        init();

        //set the user email to textview
        email.setText(user.getEmail());


        //log out the user
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
            }
        });


        //verify user email in case if it is not verified
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    //verification email sent successfully
                                    Toast.makeText(ProfileActivity.this, "Verification email sent to "+user.getEmail(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //if some error occurs, it will be displyed vie a toast
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = findViewById(R.id.user_email);
        btnLogout = findViewById(R.id.logout);
        verified = findViewById(R.id.verified_email);
        btnVerify = findViewById(R.id.btnVerify);

    }



    @Override
    protected void onStart() {
        super.onStart();

        //check if user is loggen in or not
        //if not, we can redirect him to signin screen
        FirebaseUser user = auth.getCurrentUser();
        if (user == null)
        {
            startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
            finish();

        }

        //if user is verified, show the status at the text box that user is verified
        //and disable the verify button, since it is already verified
        if (user.isEmailVerified()){
            verified.setText("User email verified");
            btnVerify.setEnabled(false);
        }

        //if user isn't verified, set the status that user isn't verified at the textbox
        //and also enable the button, so we can send verification email
        if (!user.isEmailVerified()){
            verified.setText("User email is not verified");
            btnVerify.setEnabled(true);
        }
    }


}
