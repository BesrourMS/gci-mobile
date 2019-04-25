package com.gci.gestioncapteursincendie;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    WebViewClient webViewClient;
    String webView_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //storing webView defined in activity_main.xml inside a WebView instance webView
        webView = (WebView) findViewById(R.id.webview);

        //enabling js in the WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url){
                System.out.println("Webview URL OUTPUTT= "+ webView.getUrl());
                //restrict evaluating on non logged in users
                if(!webView.getUrl().equals("http://gestioncapteursincendie.herokuapp.com/") && !webView.getUrl().equals("http://gestioncapteursincendie.herokuapp.com/login")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript("(function() { return document.getElementById('etb').value; })();",
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String html) {
                                        System.out.println("HTML OUTPUTT= "+html);
                                        try {

                                           OneSignal.sendTag("etab", html.substring(1,html.length()-1));

                                            System.out.println("marche");
                                        }
                                        catch (Exception e){
                                            System.out.println("error: "+e);
                                        }

                                        OneSignal.getTags(new OneSignal.GetTagsHandler() {
                                            @Override
                                            public void tagsAvailable(JSONObject tags) {
                                                System.out.println("debug0123= "+tags.toString());
                                            }
                                        });
                                    }
                                });
                    }
                }
            }

        });

        //defining the website the webView loads when the app is launched
        webView.loadUrl("http://gestioncapteursincendie.herokuapp.com");
        webView_url = webView.getUrl();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                      if(task.isSuccessful()){
                          String token = task.getResult().getToken();
                          System.out.println("token: "+token);
                        }
                        else
                      {
                          System.out.println("Token Not Generated");
                      }
                    }
                });

        }
    /*private class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            if(!webView_url.equals("http://gestioncapteursincendie.herokuapp.com/login") && !webView_url.equals("http://gestioncapteursincendie.herokuapp.com/")){
            Document doc = null;
            Element x = null;
            try {
                doc = Jsoup.connect(webView_url).get();
                System.out.println("SOoutput Webview url 0 = " +webView_url);

                x = doc.getElementById("loggedin");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("SOoutput Webview url= " +webView_url);
            System.out.println("SOoutput loggedin= " + x);
            System.out.println("SOoutput x val "+ x.val());
            return x.val();
            }
            return "NOTHING anymore";
        }


        @Override
        protected void onPostExecute(String result) {

            System.out.println("RESULT: "+result);
        }
    }*/

}



