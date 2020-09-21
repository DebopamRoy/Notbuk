package project.mapobed.notbuk.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import project.mapobed.notbuk.Home.HomeActivity;
import project.mapobed.notbuk.R;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private TextInputLayout register_name, register_email, register_password;
    private Button register_submit;
    private ImageView register_facebook, register_google;
    private String name, email, password;
    private FirebaseAuth mAuth;
    private ConstraintLayout constraintLayout;
    private TextView register_login, register_skip;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null)
            startHomeActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //declaration
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        register_name = (TextInputLayout) findViewById(R.id.register_name);
        register_email = (TextInputLayout) findViewById(R.id.register_email);
        register_password = (TextInputLayout) findViewById(R.id.register_password);
        register_submit = (Button) findViewById(R.id.register_submit);
        register_facebook = (ImageView) findViewById(R.id.register_facebook);
        register_google = (ImageView) findViewById(R.id.register_google);
        register_login = (TextView) findViewById(R.id.register_login);
        register_skip = (TextView) findViewById(R.id.register_skip);
        constraintLayout = (ConstraintLayout) findViewById(R.id.register_constrainLayout);

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        register_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomeActivity();
            }
        });

        register_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());

        register_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("email", "public_profile"));
                mCallbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        snackbarShow(getString(R.string.please_try_again));
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        snackbarShow(exception);
                    }
                });

            }
        });

        register_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = register_name.getEditText().getText().toString().trim();
                email = register_email.getEditText().getText().toString().trim();
                password = register_password.getEditText().getText().toString().trim();

                if (name.isEmpty() || name.length() == 0) {
                    register_name.setError(getString(R.string.name_empty_error));
                    register_name.requestFocus();
                    return;
                }
                register_name.setErrorEnabled(false);

                if (email.isEmpty() || email.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    register_email.setError(getString(R.string.email_empty_error));
                    register_email.requestFocus();
                    return;
                }
                register_email.setErrorEnabled(false);

                if (password.isEmpty() || email.length() == 0 || password.length() < 6 || password.length() > 22) {
                    register_password.setError(getString(R.string.password_empty_error));
                    register_password.requestFocus();
                    return;
                }
                register_password.setErrorEnabled(false);


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification();
                            snackbarShow(getString(R.string.email_verification_link_sent));
                            Toast.makeText(RegisterActivity.this, "" + getString(R.string.email_verification_link_sent), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class).putExtra("userEmail", email));
                            finish();
                        } else {
                            snackbarShow(task.getException());
                            Log.d("ERROR", task.getException() + "");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERROR", e + "");
                        snackbarShow(e);
                    }
                });
            }
        });
    }


    private void handleFacebookAccessToken(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startHomeActivity();
                } else {
                    snackbarShow(task.getException());
                }
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void startHomeActivity() {
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
        finish();
    }

    private void snackbarShow(Object text) {
        if (text.toString().equals("com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
            text = getString(R.string.check_internet);
        if (text.toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."))
            text = getString(R.string.already_email);
        Snackbar snackbar = Snackbar.make(constraintLayout, text.toString(), Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
        snackbar.setTextColor(getResources().getColor(R.color.secondColor));
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("ERROR", "Google sign in failed" + e);
                snackbarShow(e);
            }
        } else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startHomeActivity();
                } else {
                    snackbarShow(task.getException());
                }
            }
        });
    }
}