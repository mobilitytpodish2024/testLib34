package com.tpodisha.gcmaqa.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.JsonObject;
import com.tpodisha.gcmaqa.BuildConfig;
import com.tpodisha.gcmaqa.CommonMethods.CommonMethod;
import com.tpodisha.gcmaqa.R;
import com.tpodisha.gcmaqa.webservice.ApiInterface;
import com.tpodisha.gcmaqa.webservice.RetrofitClientInstance;
import com.tpodisha.modal.ModalResponseVersion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.delight.android.webview.AdvancedWebView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity implements AdvancedWebView.Listener {
    AdvancedWebView mWebView;
    AdvancedWebView printWeb;
    Context mContext;
    Button savePdfBtn;
    private ProgressDialog progressdialog;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    private String FILE_NAME = "";
    private String MIME_TYPE = "application/vnd.android.package-archive";

    private String docName="";
    private String BASE_URL="https://portal.tpcentralodisha.com:8099/Tender/Apk/";

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    private ProgressDialog pDialogInstall;
    private String gradleVersion="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        pDialogInstall=new ProgressDialog(MainActivity.this);


        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        checkPermissions();


        initView();

        /*myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                System.out.println("hello");
                return true;
            }
        });
        myWebView.loadUrl("https://gcmsqa.tpodisha.com/");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        myWebView.setClickable(true);
        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String billNo="";
                DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url));
                *//*request.setMimeType(mimetype);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie",cookies);
                request.addRequestHeader("User-Agent",userAgent);
                request.setDescription("Downloading file....");
                request.setTitle(URLUtil.guessFileName(url,contentDisposition,mimetype));*//*
                request.allowScanningByMediaScanner();
                *//*request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(),"Downloading File",Toast.LENGTH_SHORT).show();*//*
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                try{
                    billNo=url.split("=")[1];
                }catch (Exception e){
                    e.printStackTrace();
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Bill_"+billNo+".pdf");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
            }
        });*/
    }

    @SuppressLint("NewApi")
    private void initView() {
// Initializing the Button
        savePdfBtn = (Button) findViewById(R.id.savePdfBtn);
        mWebView = findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setClickable(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.setMixedContentAllowed(false);


        progressdialog = new ProgressDialog(mContext);
        //web_view.setMixedContentAllowed(false);
        WebSettings settings = mWebView.getSettings();
        settings.setTextZoom(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }



        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
       // mWebView.loadUrl("https://gcmsqa.tpodisha.com/");

        mWebView.loadUrl("https://gcms.tpodisha.com/");


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                Log.d("permission","permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,1);
            }


        }

        savePdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (printWeb != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Calling createWebPrintJob()
                        PrintTheWebPage(printWeb);
                    } else {
                        // Showing Toast message to user
                        Toast.makeText(MainActivity.this, "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Showing Toast message to user
                    Toast.makeText(MainActivity.this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    PrintJob printJob;

    // a boolean to check the status of printing
    boolean printBtnPressed = false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView webView) {

        // set printBtnPressed true
        printBtnPressed = true;

        // Creating  PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        // setting the name of job
        String jobName = getString(R.string.app_name) + " webpage" + webView.getUrl();

        // Creating  PrintDocumentAdapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
        progressdialog.dismiss();
        printWeb = mWebView;
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        progressdialog.dismiss();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        try {

            System.out.println("url=="+url);
            System.out.println("url=="+suggestedFilename);



            if ((url.contains("Home/DownloadBill"))){
                String docName=suggestedFilename.split("Bill_")[1];
                url="https://portal.tpcentralodisha.com:8071/ConsumerBillInfo_2021/PdfBillGeneratorFrontController?documentno="+docName.split(".pdf")[0];
            }


            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(url));



            request.setMimeType(mimeType);
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                    mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                            url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Downloading File",
                    Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            savePdfBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        callVersionCheckAPI();

        mWebView.onResume();
        if (printJob != null && printBtnPressed) {
            if (printJob.isCompleted()) {
                // Showing Toast Message
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
                savePdfBtn.setVisibility(View.GONE);
            } else if (printJob.isStarted()) {
                // Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show();

            } else if (printJob.isBlocked()) {
                // Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();

            } else if (printJob.isCancelled()) {
                // Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show();

            } else if (printJob.isFailed()) {
                // Showing Toast Message
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show();

            } else if (printJob.isQueued()) {
                // Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();

            }
            // set printBtnPressed false
            printBtnPressed = false;
        }
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }*/

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = REQUIRED_SDK_PERMISSIONS;
        }
        return p;
    }


    public void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissi
        for (final String permission : permissions()) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int REQUEST_CODE_FILE_UPLOAD = 5902;
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted

                        // finish();
                        if (permissionStatus.getBoolean(REQUIRED_SDK_PERMISSIONS[0], true)) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This app needs storage permission.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sentToSettings = true;
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                            builder.show();
                        }
                        return;
                    }
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(WRITE_EXTERNAL_STORAGE, true);
                editor.commit();

                // pickPicture(REQUEST_CODE_FILE_UPLOAD);
                break;
        }
    }

    public void Update(final String apkurl) {
        new AsyncTask<Void, String, String>() {
            String result="";
            @Override
            protected String doInBackground(Void... params) {
                try {
                    try {
                        // 001300265466
                        // String docPath = "https://www.tpnodl.com/tpn/app_debug.apk";
                        String docPath = apkurl;
                        System.out.println("sdf=="+docPath);

                        FILE_NAME=docPath.substring(docPath.lastIndexOf("/")+1);

                        String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+FILE_NAME;
                        File myFile = new File(fileName);
                        if (myFile.exists())
                            myFile.delete();
                        final DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(docPath));
                        request.setMimeType(MIME_TYPE);
                        String cookies = CookieManager.getInstance().getCookie(docPath);

                        request.setDescription("Downloading file...");
                        request.setTitle(URLUtil.guessFileName(docPath, docName,
                                MIME_TYPE));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                        docPath, docName, MIME_TYPE));


                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                        BroadcastReceiver receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                unregisterReceiver(this);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialogInstall.dismiss();;
                                    }
                                });

                              /*  Intent intents =new  Intent(Intent.ACTION_DELETE);
                                intents.setData(Uri.parse("package:"+"com.aa.cesuecollection"));
                                startActivity(intents);*/
                              /*  Uri packageURI = Uri.parse("package:"+"com.aa.cesuecollection");
                                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                                startActivity(uninstallIntent);*/

                                //  deleteCache(CollectionDashBoard.this);

                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+FILE_NAME);
                                Intent intenat = new Intent(Intent.ACTION_VIEW);



                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if(!getPackageManager().canRequestPackageInstalls()){
                                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                                                .setData(Uri.parse(String.format("package:%s", getPackageName()))), 1);
                                    }
                                    else{
                                        installAPK();
                                    }

                                }
                                else{

                                }

                          /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    Uri downloaded_apk = FileProvider.getUriForFile(CollectionDashBoard.this, CollectionDashBoard.this.getApplicationContext().getPackageName() + ".provider", file);
                                    intenat.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                                    List<ResolveInfo> resInfoList = CollectionDashBoard.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        CollectionDashBoard.this.grantUriPermission(CollectionDashBoard.this.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }

                                    intenat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(intenat);
                                } else {
                                    intenat.setAction(Intent.ACTION_VIEW);
                                    intenat.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intenat.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                    intenat.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                    intenat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intenat);


                                }*/

                            }
                        };
                        registerReceiver(receiver, new IntentFilter(
                                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        dm.enqueue(request);
                        // Toast.makeText(CollectionDashBoard.this, "Download started", Toast.LENGTH_SHORT).show();


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception e) {
                    result="Update error! "+ e.getMessage();
                    e.printStackTrace();

                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                pDialogInstall.setTitle("Downloading APK Please wait..");
                pDialogInstall.show();
            }

            protected void onPostExecute(String result) {

                //  Toast.makeText(getApplicationContext(), result,
                //  Toast.LENGTH_LONG).show();
            };

        }.execute();

    }
    void installAPK(){

        String PATH =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+FILE_NAME;
        // Environment.getExternalStorageDirectory() + "/" + "apkname.apk";

        File file = new File(PATH);
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(getApplicationContext(), new File(PATH)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
            }
        }else{
            Toast.makeText(getApplicationContext(),"installing",Toast.LENGTH_LONG).show();
        }
    }
    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mWebView.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode==RESULT_OK){
            installAPK();
        }
        if (requestCode == 1 && resultCode==RESULT_CANCELED){
            finish();
        }
    }

    private void callVersionCheckAPI() {
        if (CommonMethod.isNetworkAvailable(MainActivity.this)) {

            ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            JsonObject object = new JsonObject();

            object.addProperty("MODULE_ID", "GPCOL");

            Call<ModalResponseVersion> call = service.checkVersion( object);
            call.enqueue(new Callback<ModalResponseVersion>() {
                @Override
                public void onResponse(Call<ModalResponseVersion> call, Response<ModalResponseVersion> response) {

                    if (response.code()==200){
                        ModalResponseVersion modalResponseVersion=response.body();

                        assert modalResponseVersion != null;
                        if (modalResponseVersion.getResponse().getResponseCode().equalsIgnoreCase("200")){

                            if (CommonMethod.getVersionName(MainActivity.this).equalsIgnoreCase(modalResponseVersion.getResponse().getApiresponse().getSoftwareVersionSap())){

                            }else {
                                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(MainActivity.this);
                                adb.setMessage("You are using older version of application, Please Update the application.")
                                        .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                                if (modalResponseVersion.getResponse().getApiresponse().getUrlname()!=null && !modalResponseVersion.getResponse().getApiresponse().getUrlname().equalsIgnoreCase("")){
                                                    Update(modalResponseVersion.getResponse().getApiresponse().getUrlname());
                                                }
                                            }
                                        }).create().show();
                            }

                        }
                    }else {

                    }

                }
                @Override
                public void onFailure(Call<ModalResponseVersion> call, Throwable t) {

                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

            //--------------------------------

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("You are not connected to internet");
            alertDialogBuilder.setMessage("Please connect to internet.")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
            // internetStatus.setText("Internet Disconnected.");
            //  internetStatus.setTextColor(Color.parseColor("#ff0000"));
        }
    }

}
