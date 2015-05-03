package it.bestapp.paganino.utility.drive;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import it.bestapp.paganino.adapter.bustapaga.Busta;


public abstract class BaseDriveActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private GoogleApiClient mGoogleApiClient;
    private DriveId idFolder;
    private boolean active;
    private Activity _this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = this;
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (!active) return;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }


    public void pushFile(final File f, final Busta bP){
        if (idFolder == null) return;
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            showMessage("Error while trying to create new file contents");
                            return;
                        }
                        DriveContents driveContents = result.getDriveContents();
                        OutputStream os = driveContents.getOutputStream();
                        try {
                            InputStream dbInputStream = new FileInputStream(f);
                            byte[] buffer = new byte[1024];
                            int length;
                            int counter = 0;
                            while((length = dbInputStream.read(buffer)) > 0) {
                                ++counter;
                                os.write(buffer, 0, length);
                            }
                            dbInputStream.close();
                            os.flush();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(bP.getAnno()+"_"+bP.getMese(_this)+".pdf")
                                .setMimeType("application/pdf")
                                .setStarred(true).build();

                        DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), idFolder);
                        folder.createFile(mGoogleApiClient, changeSet, driveContents);
                    }
                });
    }


    @Override
    protected void onPause() {
        if (active) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        }
        super.onPause();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TRASHED, false))
                .addFilter(Filters.eq(SearchableField.TITLE, "Paganino"))
                .build();

        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {

                if (!metadataBufferResult.getStatus().isSuccess()) {
                    showMessage("Problem while retrieving results");
                    return;
                }
                for ( Metadata mBuffer  : metadataBufferResult.getMetadataBuffer() ) {
                    if (mBuffer.getMimeType().equalsIgnoreCase("application/vnd.google-apps.folder")){
                        idFolder = mBuffer.getDriveId();
                        return;
                    }
                }

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("Paganino").build();
                Drive.DriveApi.getRootFolder(getGoogleApiClient())
                        .createFolder(getGoogleApiClient(), changeSet)
                        .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                            @Override
                            public void onResult(DriveFolder.DriveFolderResult result) {
                                if (!result.getStatus().isSuccess()) {
                                    showMessage("Error while trying to create the folder");
                                    return;
                                }
                                idFolder = result.getDriveFolder().getDriveId();
                            }
                        });
            }

        });
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {

        }
    }


    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onConnectionSuspended(int i) {}


    protected void driveActive(boolean b){
        active = b;
    }

}
