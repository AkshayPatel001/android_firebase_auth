package patel.akshay.firebaseemailauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText emailET, passET;
    private TextView signin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //init method is used to initialize all the widgets in the xml file
        //it will be easy to to find the name of any widget in case if we forget the name
        init();


        //go to sign in screen
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });


        //perform sign up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                emailET.setError(null);
                passET.setError(null);

                //if email field is blank
                if (email.matches(""))
                    emailET.setError("Enter email first !!");

                //if password field is blank
                else if (pass.matches(""))
                    passET.setError("Enter password for your account");
                else{
                    //pass email and pass to sign up function to perform sign up
                    signup(email, pass);
                }
            }
        });
    }


    //sign up function
    private void signup(String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //sign up successful
                            Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //in case of sign up failed, get the error message
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.signup_email);
        passET = findViewById(R.id.signup_pass);
        signin = findViewById(R.id.already_member);
        btnSignup = findViewById(R.id.btnSignup);
    }
}
