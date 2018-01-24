package org.anirudh.redquark.quarknews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        onNewIntent(getIntent());

        try {
            Intent intent = getIntent();
            String postUrl = intent.getStringExtra("loadUrl");

            webView.setWebViewClient(new MyWebClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(postUrl);
            webView.setHorizontalScrollBarEnabled(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class MyWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    protected void onNewIntent(Intent intent){
        String action = intent.getAction();
        String postUrl = intent.getDataString();

        try{
            webView.setWebViewClient(new MyWebClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(postUrl);
            webView.setHorizontalScrollBarEnabled(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(WebActivity.this, HomeActivity.class);
        startActivity(i);
    }
}
