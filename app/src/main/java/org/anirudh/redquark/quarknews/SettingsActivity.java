package org.anirudh.redquark.quarknews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.anirudh.redquark.quarknews.application.QuarkNewsApplication;
import org.anirudh.redquark.quarknews.receiver.ConnectivityReceiver;

public class SettingsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private Button changeEmail;
    private Button changePassword;
    private Button sendEmail;
    private Button remove;

    private EditText oldEmail, newEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        Button btnChangeEmail = findViewById(R.id.change_email_button);
        Button btnChangePassword = findViewById(R.id.change_password_button);
        Button btnSendResetEmail = findViewById(R.id.sending_pass_reset_button);
        Button btnRemoveUser = findViewById(R.id.remove_user_button);
        changeEmail = findViewById(R.id.changeEmail);
        changePassword = findViewById(R.id.changePass);
        sendEmail = findViewById(R.id.send);
        remove = findViewById(R.id.remove);
        Button signOut = findViewById(R.id.sign_out);
        Button back = findViewById(R.id.btn_back);

        oldEmail = findViewById(R.id.old_email);
        newEmail = findViewById(R.id.new_email);
        password = findViewById(R.id.password);
        newPassword = findViewById(R.id.newPassword);

        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    oldEmail.setVisibility(View.GONE);
                    newEmail.setVisibility(View.VISIBLE);
                    password.setVisibility(View.GONE);
                    newPassword.setVisibility(View.GONE);
                    changeEmail.setVisibility(View.VISIBLE);
                    changePassword.setVisibility(View.GONE);
                    sendEmail.setVisibility(View.GONE);
                    remove.setVisibility(View.GONE);
                }
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (user != null && !newEmail.getText().toString().trim().equals("")) {
                        user.updateEmail(newEmail.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    } else if (newEmail.getText().toString().trim().equals("")) {
                        newEmail.setError("Enter email");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    oldEmail.setVisibility(View.GONE);
                    newEmail.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                    newPassword.setVisibility(View.VISIBLE);
                    changeEmail.setVisibility(View.GONE);
                    changePassword.setVisibility(View.VISIBLE);
                    sendEmail.setVisibility(View.GONE);
                    remove.setVisibility(View.GONE);
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (user != null && !newPassword.getText().toString().trim().equals("")) {
                        if (newPassword.getText().toString().trim().length() < 6) {
                            newPassword.setError("Password too short, enter minimum 6 characters");
                            progressBar.setVisibility(View.GONE);
                        } else {
                            user.updatePassword(newPassword.getText().toString().trim())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                                signOut();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(SettingsActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                    } else if (newPassword.getText().toString().trim().equals("")) {
                        newPassword.setError("Enter password");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    oldEmail.setVisibility(View.VISIBLE);
                    newEmail.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                    newPassword.setVisibility(View.GONE);
                    changeEmail.setVisibility(View.GONE);
                    changePassword.setVisibility(View.GONE);
                    sendEmail.setVisibility(View.VISIBLE);
                    remove.setVisibility(View.GONE);
                }
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (!oldEmail.getText().toString().trim().equals("")) {
                        auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    } else {
                        oldEmail.setError("Enter email");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (user != null) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SettingsActivity.this, SignupActivity.class));
                                            finish();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection())
                    signOut();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (!isConnected) {
            message = "Sorry! Not connected to internet. Please check your internet connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        QuarkNewsApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}