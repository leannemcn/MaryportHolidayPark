package com.example.b00682737.maryportholidaypark.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00682737.maryportholidaypark.BookingApplication;
import com.example.b00682737.maryportholidaypark.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {

    Context mContext;
    BookingApplication mMyApp;
    protected ProgressDialog mProgress;

    protected Bitmap mBitmap;

    SharedPreferences mSettings;


    public static final String GLOBAL_SETTING = "cscs";

    protected static final int REQUEST_REGISTER = 100;
    protected static final int REQUEST_LOCATION = 200;
    protected static final int REQUEST_CAMERA = 300;
    protected static final int REQUEST_ALBUM = 400;

    // Permission Requests

    protected static final int PERMISSION_REQUEST_CODE_PHOTO = 101;
    protected static final String[] PERMISSION_REQUEST_PHOTO_STRING = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    protected static final int PERMISSION_REQUEST_CODE_GALLERY = 102;
    protected static final String[] PERMISSION_REQUEST_GALLERY_STRING = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    protected static final int PERMISSION_REQUEST_CODE_LOCATION = 103;
    protected static final String[] PERMISSION_REQUEST_LOCATION_STRING = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};



    protected String TAG = "AppCommon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mMyApp = (BookingApplication) getApplication();

        mSettings = getPreferences(this);

        mProgress = new ProgressDialog(mContext, R.style.DialogTheme);
        mProgress.setMessage(getString(R.string.loading));
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(GLOBAL_SETTING, Context.MODE_PRIVATE);
    }


    public void showProgressDialog() {
        if (mProgress.isShowing())
            return;

        mProgress.show();
        mProgress.setContentView(R.layout.dialog_loading);
    }

    public void hideProgressDialog() {
        if (mProgress.isShowing())
            mProgress.dismiss();
    }

    public void showToastMessage(String msg) {
        if(!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }


    public void showAlert(String message) {
        showAlert(message, null);
    }

    public void showAlert(String message, final View.OnClickListener clickListener) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);

        final AlertDialog errorDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView tvAlert = (TextView) dialogView.findViewById(R.id.tvAlert);
        tvAlert.setText(message);
        dialogView.findViewById(R.id.btnCloseAlert).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                errorDlg.dismiss();
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        });

        errorDlg.show();
        errorDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    protected void shareApp() {
        try {
            // Uri imageUri = Uri.fromFile(imageFile);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body));
            // shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("text/plain");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String chooserTitle = getResources().getString(R.string.share_title);
            startActivity(Intent.createChooser(shareIntent, chooserTitle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //List and Signout options at the top of the home page. Possibility of being used as navigation.
    protected void Signout() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Confirm logout");
        alertDialogBuilder.setMessage("Would you like to logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                // Firebase Logout
                if (firebaseAuth != null) {
                    firebaseAuth.signOut();
                }

                finish();
                startActivity(new Intent(mContext, Login.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


   //the profile photo for users
    protected String strCameraOutputFilePath;

    // image crop parameter
    protected static final String TYPE_IMAGE = "image/*";

    protected static final int CARAVAN_IMAGE_ASPECT_X = 3;
    protected static final int CARAVAN_IMAGE_ASPECT_Y = 2;
    protected static final int CARAVAN_IMAGE_OUTPUT_X = 750;
    protected static final int CARAVAN_IMAGE_OUTPUT_Y = 500;

    final static String IMG_FILE_PREFIX = "fg";
    final static String CAMERA_FILE_PREFIX = "CAMERA";
    final static String JPEG_FILE_SUFFIX = ".jpg";

    public File makeCameraOutputFile() {

        String extStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = "DCIM"/*getString(R.string.app_name)*/;
        String appFolder = extStoragePath + File.separator + folderName;
        String cameraFolder = appFolder + File.separator + "NationalSales";

        File mediaStorageDir = new File(cameraFolder);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraOutputFileManager", "Required media storage does not exist");
                return null;
            }
        }

        String strFileName = String.format("%s%d", CAMERA_FILE_PREFIX, System.currentTimeMillis());

        File cameraImage = null;
        try {
            cameraImage = File.createTempFile(strFileName, // prefix
                    JPEG_FILE_SUFFIX, // suffix
                    mediaStorageDir // directory
            );
            cameraImage.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cameraImage;
    }

    public void getCameraImage() {
        if (checkPermissions(mContext, PERMISSION_REQUEST_PHOTO_STRING, false, PERMISSION_REQUEST_CODE_PHOTO)) {
            startCameraActivity();
        }
    }

    protected void showImageSource() {
        final Dialog customDlg = new Dialog(this);
        customDlg.setContentView(R.layout.dialog_custom_photo);
        customDlg.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDlg.dismiss();
            }
        });

        customDlg.findViewById(R.id.choosephoto).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDlg.dismiss();
                if (checkPermissions(mContext, PERMISSION_REQUEST_GALLERY_STRING, false, PERMISSION_REQUEST_CODE_GALLERY)) {
                    startGalleryActivity();
                }
            }
        });
        customDlg.findViewById(R.id.takephoto).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDlg.dismiss();

                if (checkPermissions(mContext, PERMISSION_REQUEST_PHOTO_STRING, false, PERMISSION_REQUEST_CODE_PHOTO)) {
                    startCameraActivity();
                }
            }
        });

        customDlg.show();
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    protected void startGalleryActivity() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_ALBUM);
    }

    protected void startCameraActivity() {
        File cameraOutputFile = makeCameraOutputFile();
        if (cameraOutputFile == null) {
            return;
        }

        strCameraOutputFilePath = cameraOutputFile.getAbsolutePath();

        Uri imageUri = getFileUri(cameraOutputFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri/*Uri.fromFile(cameraOutputFile)*/);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    protected Uri getFileUri(File file) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Uri fileUri = Uri.fromFile(file);
            return fileUri;
        } else {
            Uri fileUri = FileProvider.getUriForFile(
                    getApplicationContext(),
                    "com.example.b00641907.mcnicholl_caravans.provider", // (use your app signature + ".provider" )
                    file);
            return fileUri;
        }
    }


    public File makeTempFile(String suffix) {

        if (TextUtils.isEmpty(suffix))
            suffix = JPEG_FILE_SUFFIX;

        String extStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = "DCIM"/*getString(R.string.app_name)*/;
        String appFolder = extStoragePath + File.separator + folderName;
        String tmpPicFolder = appFolder + File.separator + "NcNicholl"/*"tmpPic"*/;
        File mediaStorageDir = new File(tmpPicFolder);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Temp File Manager", "Required media storage does not exist");
                return null;
            }
        }

        String strFileName = String.format("%s%d", IMG_FILE_PREFIX, System.currentTimeMillis());

        File tempImage = null;
        try {
            tempImage = File.createTempFile(strFileName, // prefix
                    suffix, // suffix
                    mediaStorageDir // directory
            );

            tempImage.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    public static boolean checkPermissions(Context context, String[] permissions, boolean showHintMessage, int requestCode) {

        if (permissions == null || permissions.length == 0)
            return true;

        boolean allPermissionSetted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionSetted = false;
                break;
            }
        }

        if (allPermissionSetted)
            return true;

        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                shouldShowRequestPermissionRationale = true;
                break;
            }
        }

        if (showHintMessage && shouldShowRequestPermissionRationale) {
            // waiting for the user's response! Allow user to try again
            String strPermissionHint = context.getString(R.string.request_permission_hint);
            Toast.makeText(context, strPermissionHint, Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check All Permission was granted
        boolean bAllGranted = true;
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                bAllGranted = false;
                break;
            }
        }

        if (bAllGranted) {
            if (requestCode == PERMISSION_REQUEST_CODE_PHOTO) {
                startCameraActivity();
            } else if (requestCode == PERMISSION_REQUEST_CODE_GALLERY) {
                startGalleryActivity();
            }
        } else {
            showAlert("Need permissions to use function.");
        }
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    protected boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(mContext, requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    protected void createAlarm(Intent intent, int requestCode, long timeInMillis) {
        deleteAlarm(intent, requestCode);

        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
    }

    protected void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(mContext, requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
            Log.d("OskarSchindler", "PI Cancelled " + doesPendingIntentExist(i, requestCode));
        }
    }

}

