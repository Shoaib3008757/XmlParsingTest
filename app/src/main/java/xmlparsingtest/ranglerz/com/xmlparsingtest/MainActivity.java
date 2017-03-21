package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Answer;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Question;

public class MainActivity extends AppCompatActivity {





    private static final String TAG = "MAIN ACTIVITY";

   // private WebView webView ;




   // static DBHelperClass dbHelperClass ;
    public static  File DB_PATH = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // dbHelperClass = new DBHelperClass(this);



        // get Database File Name..
      //  DB_PATH = this.getDatabasePath(dbHelperClass.getDatabaseName());
        Log.d(TAG, "Db Path is " + DB_PATH.getAbsolutePath());


        //   webView  = (WebView) findViewById(R.id.webView1);

        /////////////////////////////////
        /// User Registration Segment ///
        /////////////////////////////////

        /********************************/
        /* This is used for showing the website in to webview */
        /********************************/

      //  startWebView("http://abel-app.ranglerz.com");

    }




    /********************************/
        /* This method is called when user press Learn By Chapter Button
        * */
    /********************************/

    public void learnByChapter (View v)
    {
        Intent i = new Intent(MainActivity.this,LearnByChapter.class);
        startActivity(i);
    }




    /********************************/
        /* This method is called when user press take Test Button
        * */
    /********************************/

    public void takeTest (View v)
    {
        Intent i = new Intent(MainActivity.this,RandomTest.class);
        startActivity(i);
    }



    /********************************/
        /* This method is called when user press take Category Test
        * */
    /********************************/

    public void takeCategoryTest (View v)
    {
        Intent i = new Intent(MainActivity.this,CategoryTest.class);
        startActivity(i);
    }

    /********************************/
        /* This method is called when user press View Progress Button
        * */
    /********************************/

    public void viewProgress (View v)
    {
//        Intent i = new Intent(MainActivity.this,CategoryTest.class);
//        startActivity(i);



    }



    public void ShowUserProgressDialog(String totaltests , String totalCorrectAnswers , String totalWrongAnswers , String totalPassTest){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(" General Statistics ");
        builder.setCancelable(false);

        int totalFailTest = Integer.parseInt(totaltests) - Integer.parseInt(totalPassTest);
            builder.setMessage(" Progress \n" +
                    " Total Number of Tests " + totaltests +
                    " \n Total Correct Answers  " + totalCorrectAnswers +
                    " \n Total Wrong Answers  " + totalWrongAnswers +
                    " \n Passed Tests " + totalPassTest +
                    " \n Failed Tests  " + totalFailTest +
                    " \n ");



        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        builder.create().show();
    }






    /********************************/
        /* This method will load the url into the WebView
        *
        * */
    /********************************/




//    private void startWebView(String url) {
//
//        //Create new webview Client to show progress dialog
//        //When opening a url or click on link
//
//        webView.setWebViewClient(new WebViewClient() {
//
//
//            //If you will not use this method url links are opeen in new brower not in webview
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            //Show loader on url load
//            public void onLoadResource (WebView view, String url) {
//
//
//            }
//            public void onPageFinished(WebView view, String url) {
//
//            }
//
//        });
//
//        // Javascript inabled on webview
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        // Other webview options
//        /*
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webView.setScrollbarFadingEnabled(false);
//        webView.getSettings().setBuiltInZoomControls(true);
//        */
//
//        /*
//         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
//         webview.loadData(summary, "text/html", null);
//         */
//
//        //Load url in webview
//        webView.loadUrl(url);
//
//
//    }



    @Override
    public void onBackPressed() {
//        if(webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
    }




}
