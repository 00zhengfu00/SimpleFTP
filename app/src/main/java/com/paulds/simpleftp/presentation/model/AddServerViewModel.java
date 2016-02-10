package com.paulds.simpleftp.presentation.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.view.View;

import com.paulds.simpleftp.data.entities.FtpServer;
import com.paulds.simpleftp.presentation.AndroidApplication;
import com.paulds.simpleftp.presentation.activities.ListServerActivity;

/**
 * Model for the FTP server creation view.
 *
 * @author Paul-DS
 */
public class AddServerViewModel extends FormViewModel {
    /**
     * The activity context.
     */
    private Activity context;

    /**
     * The server name.
     */
    public FieldViewModel<String> name;

    /**
     * The server host.
     */
    public FieldViewModel<String> host;

    /**
     * The server port.
     */
    public FieldViewModel<String> port;

    /**
     * A value indicating whether the connection must be anonymous.
     */
    public FieldViewModel<Boolean> isAnonymous;

    /**
     * The login used to connect the server.
     */
    public FieldViewModel<String> login;

    /**
     * The password used to connect the server.
     */
    public FieldViewModel<String> password;

    /**
     * Default constructor.
     * @param context The context of the current activity.
     */
    public AddServerViewModel(Activity context) {
        this.context = context;
        this.name = new FieldViewModel<String>(){
            @Override
            public void onValidate() {
                this.setError(this.get() == null || this.get().isEmpty()
                                ? "Please fill a name for this server."
                                : null);
            }
        };

        this.host = new FieldViewModel<String>(){
            @Override
            public void onValidate() {
                this.setError(this.get() == null || this.get().isEmpty()
                        ? "Please fill the server host."
                        : null);
            }
        };

        this.port = new FieldViewModel<String>("21"){
            @Override
            public void onValidate() {
                this.setError(!TextUtils.isDigitsOnly(this.get()) ? "Incorrect port." : null);
            }
        };

        this.isAnonymous = new FieldViewModel<Boolean>();

        this.login = new FieldViewModel<String>(){
            @Override
            public void onValidate() {
                this.setError((isAnonymous.get() != null && isAnonymous.get().booleanValue())  && (this.get() == null || this.get().isEmpty())
                        ? "Please fill the login for FTP connection."
                        : null);
            }
        };

        this.password = new FieldViewModel<String>(){
            @Override
            public void onValidate() {
                this.setError((isAnonymous.get() != null && isAnonymous.get().booleanValue()) && (this.get() == null || this.get().isEmpty())
                        ? "Please fill the password for FTP connection."
                        : null);
            }
        };
    }

    /**
     * Create the server.
     * @param view The current view
     */
    public void createServer(View view) {
        if(this.validate())
        {
            FtpServer server = new FtpServer();

            server.setName(this.name.get());
            server.setHost(this.host.get());
            server.setPort(TextUtils.isDigitsOnly(this.port.get()) ? Integer.parseInt(this.port.get()) : 21);
            server.setIsAnonymous(this.isAnonymous.get());
            server.setLogin(this.login.get());
            server.setPassword(this.password.get());

            AndroidApplication.getRepository().getServerRepository().addServer(server);

            this.context.finish();
        }
    }
}