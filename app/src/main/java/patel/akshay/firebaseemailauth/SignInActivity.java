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
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private EditText emailET, passET;
    private String strEmail, strPass;
    private FirebaseAuth firebaseAuth;
    private Button btnSignin;
    private TextView  forgotPass, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //init method is used to initialize all the widgets in the xml file
        //it will be easy to to find the name of any widget in case if we forget the name
        init();

        //signin starts from here
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = emailET.getText().toString().trim();
                strPass = passET.getText().toString().trim();
                emailET.setError(null);
                passET.setError(null);

                if (strEmail.matches("")) {
                    emailET.setError("Enter email first !!");
                }
                else if (strPass.matches("")){
                    passET.setError("Enter your passwordf");
                }
                else {
                    //forward email and pass to login function
                    login(strEmail, strPass);
                }
            }
        });



        //forgot password function
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                emailET.setError(null);
                if (email.matches(""))
                {
                    emailET.setError("Enter email first !!");
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(emailET.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //password reset email sent successfully to provided email
                                        Toast.makeText(SignInActivity.this, "Password reset email sent to " + emailET.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        //go to sign up screen
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void login(String strEmail, String strPass) {
        firebaseAuth.signInWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //login successful
                            //You can do your things here like intent or display toast or dialog
                            //Here i will simply display toast
                            Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email);
        passET = findViewById(R.id.pass);
        btnSignin = findViewById(R.id.btnSignin);
        forgotPass = findViewById(R.id.forgot_pass);
        signup = findViewById(R.id.signup);
    }


}
