package sa_b_2.coms309.dungeonadventure.ui.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.network.HttpParse;

import static sa_b_2.coms309.dungeonadventure.user.User.logout;

/**
 * A Change username screen
 * 99% of this was generated by android studio
 */
public class ChangeUsernameActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Nullable
    private UserChangeUsernameTask mAuthTask = null;

    // UI references.
    private EditText mNewUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmUsernameView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        // Set up the login form.
        mNewUsernameView = (EditText) findViewById(R.id.username);

        mConfirmUsernameView = (EditText) findViewById(R.id.confirm_username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.change_username || id == EditorInfo.IME_NULL) {
                    attemptChangeUsername();
                    return true;
                }
                return false;
            }
        });

        Button mChangeUsernameButton = (Button) findViewById(R.id.change_username_button);
        mChangeUsernameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangeUsername();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptChangeUsername() {
        if (mAuthTask != null)
            return;

        // Reset errors.
        mNewUsernameView.setError(null);
        mConfirmUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String newUsername = mNewUsernameView.getText().toString();
        String confirmUsername = mConfirmUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!newUsername.equals(confirmUsername)) {
            mConfirmUsernameView.setError(getString(R.string.error_passwords_do_not_match));
            focusView = mConfirmUsernameView;
            cancel = true;
        }

        if (!isUsernameValid(newUsername)) {
            mNewUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mNewUsernameView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserChangeUsernameTask(newUsername, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(@NonNull String username) {
        return (username.length()) <= 20 && (username.length() >= 6) && !username.contains(" ") && !username.contains(",");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserChangeUsernameTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNewUsername;
        private final String mPassword;
        private int response = -1;

        UserChangeUsernameTask(String username, String password) {
            mNewUsername = username;
            mPassword = password;
        }

        @NonNull
        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", Constants.currentUser.getUsername());
            map.put("newUsername", mNewUsername);
            map.put("password", mPassword);

            String loginURL = "http://proj-309-sa-b-2.cs.iastate.edu/ChangeUsername.php";
            String finalResult = HttpParse.postRequest(map, loginURL);

            if (finalResult.equals("Something Went Wrong")) {
                response = 2;
                return false;
            }
            if (finalResult.equals("Not connected to the internet"))
                return false;

            response = Integer.parseInt(finalResult);

            return response == 0;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                logout();

                finish();
            } else
                switch (response) {
                    case -1:
                        mNewUsernameView.setError(getString(R.string.error_internet_connection));
                        mNewUsernameView.requestFocus();
                        break;
                    case 0:
                        mNewUsernameView.setError(getString(R.string.error_bad_code));
                        mNewUsernameView.requestFocus();
                        break;
                    case 1:
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                        break;
                    case 2:
                    case 3:
                        mNewUsernameView.setError(getString(R.string.error_server_error));
                        mNewUsernameView.requestFocus();
                        break;
                    case 4:
                        mNewUsernameView.setError(getString(R.string.error_username_taken));
                        mNewUsernameView.requestFocus();
                        break;
                }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

