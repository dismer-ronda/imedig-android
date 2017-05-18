package es.pryades.imedig.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import es.pryades.imedig.android.utils.Usuario;
import es.pryades.imedig.android.utils.Utils;
import lombok.Getter;
import lombok.Setter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MyBaseActivity implements PopupMenu.OnMenuItemClickListener
{
    private final static String TAG = LoginActivity.class.getSimpleName();

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    @Getter private EditText emailView;
    @Getter private EditText passwordView;
    @Getter private CheckBox checkRemember;

    private View mProgressView;
    private View mLoginFormView;

    @Setter @Getter private static Usuario usuario;
    @Setter @Getter private static boolean rememberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sessionPref = getPreferences(Context.MODE_PRIVATE);
        String email = sessionPref.getString( "user", null );
        String pwd = sessionPref.getString( "password", null );
        rememberLogin = sessionPref.getBoolean( "remember", false );

        setContentView(R.layout.activity_login);

        // Set up the login form.
        emailView = (EditText) findViewById(R.id.user);
        if ( rememberLogin )
            emailView.setText( email );

        passwordView = (EditText) findViewById(R.id.password);
        if ( rememberLogin )
            passwordView.setText( pwd );

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        checkRemember = (CheckBox) findViewById(R.id.checkbox_remember);
        checkRemember.setChecked( rememberLogin );

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onExit();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void onCheckboxClicked(View view) {
        rememberLogin = ((CheckBox) view).isChecked();
    }

    private void onExit()
    {
        finishAndRemoveTask();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if ( mAuthTask != null )
            return;

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if ( TextUtils.isEmpty( email ) )
        {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        }
        if ( TextUtils.isEmpty( password ) )
        {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }

        if ( cancel )
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true, mLoginFormView, mProgressView);

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {
        private final int ERROR_NONE = 0;
        private final int ERROR_INVALID_LOGIN = 1;
        private final int ERROR_INVALID_PROFILE = 2;
        private final int ERROR_HOSPITALS = 3;
        private final int ERROR_LOCATIONS = 4;

        private final String email;
        private final String password;
        private int errorCode;

        UserLoginTask(String email, String password)
        {
            this.email = email;
            this.password = password;
        }

        private void saveLoginCredentials()
        {
            SharedPreferences sessionPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sessionPref.edit();
            editor.putString( "user", email );
            editor.putString( "password", password );
            editor.putBoolean( "remember", rememberLogin );
            editor.apply();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( LoginActivity.this );
            String hospitalUrl = sharedPref.getString( SettingsFragment.SETTINGS_IMEDIG_URL, "http://192.168.1.253");
            Integer timeout = Integer.parseInt(sharedPref.getString(SettingsFragment.SETTINGS_IMEDIG_TIMEOUT, "10000"));

            usuario = Utils.login(hospitalUrl, email, password, timeout);
            if ( usuario == null ) {
                errorCode = ERROR_INVALID_LOGIN;
                return false;
            }

            saveLoginCredentials();

            return true;
        }

        @Override
        protected void onPostExecute( final Boolean success )
        {
            mAuthTask = null;
            showProgress(false, mLoginFormView, mProgressView);

            if ( success )
            {
                Intent intent = new Intent(LoginActivity.this, ListReportsActivity.class);
                startActivity(intent);
            }
            else
            {
                String errorMessage;
                switch ( errorCode )
                {
                    case ERROR_INVALID_LOGIN:
                        errorMessage = getString( R.string.error_incorrect_password );
                    break;

                    default:
                        errorMessage = getString( R.string.error_unknown );
                    break;
                }

                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle(getString( R.string.failure));
                alertDialog.setMessage( errorMessage );
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false, mLoginFormView, mProgressView);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_option:
                startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
                return true;
            default:
                return false;
        }
    }

    public void showPopup(View v) {
        startActivity(new Intent(LoginActivity.this, SettingsActivity.class));

        /*PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings_menu, popup.getMenu());
        popup.show();*/
    }
}

