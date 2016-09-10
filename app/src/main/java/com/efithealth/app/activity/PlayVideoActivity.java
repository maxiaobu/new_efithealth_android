package com.efithealth.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.efithealth.R;

/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class PlayVideoActivity extends Activity {

    private WebView wv_video;

    private String m_url = "file:///android_asset/video.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        wv_video = (WebView) findViewById(R.id.wv_video);


//        wv_video.loadUrl(m_url);
//
//        wv_video.setVerticalScrollbarOverlay(true);
//        wv_video.getSettings().setJavaScriptEnabled(true);
//        wv_video.addJavascriptInterface(new jsToAndroid(), "mobile");
//        wv_video.setWebChromeClient(new WebChromeClient());
//        wv_video.getSettings().setDefaultTextEncodingName("utf-8");
//
//        wv_video.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                wv_video.loadUrl(url);
//
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                //ҳ�������� ͨ��js�ص�����ʵ�ֻ�ȡҳ����Ϣ
//                view.loadUrl("javascript:window.mobile.loadPageData("
//                        + "document.title,$('title').attr('isback'),$('title').attr('btn'),$('title').attr('navbar'))");
//            }
//        });

    }

    // js���ý����
//    public class jsToAndroid {
//
//
//    }

}
