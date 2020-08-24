package org.techtown.gaproom.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.R;

public class AddRoomPop_Activity extends AppCompatActivity {
    private static int RESULT_OK = 2;
    private WebView webView;
    private Handler handler;
    private String result = null;
    private String result2 = null;
    Context context;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_popup);
        setActionBar();

        context = this;
        activity = this;
        webView = (WebView) findViewById(R.id.webView);
        initWebView();
        handler = new Handler();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.addJavascriptInterface(new AndroidBridge(), "GapRoom");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://192.168.43.145/daum_address_vh.html");

    }

    /*여의대방로 22길 24*/
    /*안드로이드 IP 192.168.43.145*/
    /*보스턴 커피 IP 172.30.1.5*/
    /*집 IP 192.168.0.9*/
    /*아주대 스터디 카페 IP 192.168.0.10*/
    private class AndroidBridge {
        @JavascriptInterface
        public void SetAddress(final String arg1, final String arg2, final String arg3){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    result = String.format("%s", arg1);
                    result2 = String.format("%s %s", arg2, arg3);
                    initWebView();
                    if(result != null){
                        Intent intent = new Intent();
                        intent.putExtra("result", result);
                        intent.putExtra("result2", result2);
                        setResult(RESULT_OK, intent);
                        initWebView();
                        finish();
                    }
                }
            });
        }
    }
    private void setActionBar(){
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }
}
