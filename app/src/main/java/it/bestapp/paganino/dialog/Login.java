package it.bestapp.paganino.dialog;



import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import com.afollestad.materialdialogs.MaterialDialog;

import it.bestapp.paganino.R;
import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.setting.*;
import it.bestapp.paganino.utility.thread.ThreadHome;


public class Login {


    private MaterialDialog dialog;
    private Activity act;
    private Fragment frag;
    private HRConnect conn;



    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SettingsManager settings;
    private SingletonParametersBridge singleton;



    public Login(Fragment f, HRConnect c){
        this.act  = f.getActivity();
        this.frag = f;
        this.conn = c;

        MaterialDialog.Builder dBuilder =
                new MaterialDialog.Builder(act)
                                    .title("Login")
                                    .customView(R.layout.dia_login, true)
                                    .positiveText("Submit")
                                    .negativeText("Cancel")
                                    .cancelable(false)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            if (settings.isRicordami()) {
                                                settings.setUser(mEmailView.getText().toString());
                                                settings.setPaswd(mPasswordView.getText().toString());
                                            }
                                            (new ThreadHome((PageDownloadedInterface) act, conn)).execute();
                                        }
                                        @Override
                                        public void onNegative(MaterialDialog dialog) {
                                            ((Lista) frag).stopRefresh();
                                            dialog.dismiss();
                                        }
                                    });

        dialog = dBuilder.build();
        View view = dialog.getView();

        singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings");

        // Set up the login form.
        mEmailView = (EditText) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        final CheckBox cbRicordamiView = (CheckBox) view.findViewById(R.id.cbRicordami);

        if(settings.isRicordami()) {
            //mail
            mEmailView.setText(settings.getUser());
            //passwd
            mPasswordView.setText(settings.getPaswd());
            //ricordami
            cbRicordamiView.setChecked(settings.isRicordami());
        }
        cbRicordamiView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setRicordami(isChecked);
                settings.setUser(mEmailView.getText().toString());
                settings.setPaswd(mPasswordView.getText().toString());
            }
        });

        //login
/*        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);*/







    }





    public void show(){
        dialog.show();
    }




/*

    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
            connection = new Connect(email, password, this);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;//email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;//password.length() > 4;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void loginStatus(Boolean stato) {
        showProgress(false);
        if (stato){
            singleton.addParameter("connessione", connection);
            dismiss();
        }else
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
    }



*/



}



