package com.ex.situationmanager;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class InnerEmploySituationSelectFragment extends Fragment implements View.OnClickListener {
    String TAG = "InnerEmploySituationSelectFragment";

    WebView situation_webview;
    private String rpt_id;
    private String acdnt_id;

    public InnerEmploySituationSelectFragment() {

    }

    public static boolean fragCheck = false;

    public String getRpt_id() {
        return rpt_id;
    }

    public void setRpt_id(String rpt_id) {
        this.rpt_id = rpt_id;
    }

    public String getAcdnt_id() {
        return acdnt_id;
    }

    public void setAcdnt_id(String acdnt_id) {
        this.acdnt_id = acdnt_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InnerEmployActivity.currentFragment = TAG;
        this.setAcdnt_id(getArguments().get("acdnt_id").toString());
        this.setRpt_id(getArguments().get("rpt_id").toString());

        View view = inflater.inflate(R.layout.inner_emploee_news, container, false);
        situation_webview = view.findViewById(R.id.situation_webview);
        ImageView btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        WebSettings webSettings = situation_webview.getSettings();

        webSettings.setJavaScriptEnabled(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        situation_webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView v, final SslErrorHandler handler, SslError er) {
                try {
                    String certificateString = "-----BEGIN CERTIFICATE-----\n" +
                            "MIIGczCCBVugAwIBAgIQCkXGPBtsAP24oUKfP+JruTANBgkqhkiG9w0BAQsFADBc\n" +
                            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
                            "d3cuZGlnaWNlcnQuY29tMRswGQYDVQQDExJUaGF3dGUgUlNBIENBIDIwMTgwHhcN\n" +
                            "MjExMjAxMDAwMDAwWhcNMjIxMjMwMjM1OTU5WjB6MQswCQYDVQQGEwJLUjEZMBcG\n" +
                            "A1UECBMQR3llb25nc2FuZ2J1ay1kbzEUMBIGA1UEBxMLR2ltY2hlb24tc2kxJTAj\n" +
                            "BgNVBAoTHEtvcmVhIEV4cHJlc3N3YXkgQ29ycG9yYXRpb24xEzARBgNVBAMMCiou\n" +
                            "ZXguY28ua3IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDmTJ7/+vgQ\n" +
                            "WgJO+Gf7CYlw9zDsScV2d+jsYbsIjf5X+hoNERlJjpIsUHo+LAvKE3lXEDlfvZ+e\n" +
                            "k0mYBkrvRsFyng7z2siAs6b6IFdBskTl4kaSa9epS4iPPJefY6fw8XbqWAxqN2/Z\n" +
                            "PeTv5/FT2o5Xki7JD6gAHfLwwczfF+IJ5y6pB+7QBgWHTlAzep7pWY+TuI7XInhr\n" +
                            "wSaFk38xdHg7KKHSRIPBFZJ8m1KzkU1M1/nWsFo4jqr6MEjB7NQ5qvUWDArgzXeS\n" +
                            "rURqDr7xHAXecz9Z9Fw79GX/d5l4scdmhGUu/7i9mX66pE95Hiv42jW75Tef0oJZ\n" +
                            "B/45MRffs49BAgMBAAGjggMRMIIDDTAfBgNVHSMEGDAWgBSjyF5lVOUweMEF6gcK\n" +
                            "alnMuf7eWjAdBgNVHQ4EFgQUFjvbBdz/p3nv1Q80e/e79+N5msowHwYDVR0RBBgw\n" +
                            "FoIKKi5leC5jby5rcoIIZXguY28ua3IwDgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQW\n" +
                            "MBQGCCsGAQUFBwMBBggrBgEFBQcDAjA6BgNVHR8EMzAxMC+gLaArhilodHRwOi8v\n" +
                            "Y2RwLnRoYXd0ZS5jb20vVGhhd3RlUlNBQ0EyMDE4LmNybDA+BgNVHSAENzA1MDMG\n" +
                            "BmeBDAECAjApMCcGCCsGAQUFBwIBFhtodHRwOi8vd3d3LmRpZ2ljZXJ0LmNvbS9D\n" +
                            "UFMwbwYIKwYBBQUHAQEEYzBhMCQGCCsGAQUFBzABhhhodHRwOi8vc3RhdHVzLnRo\n" +
                            "YXd0ZS5jb20wOQYIKwYBBQUHMAKGLWh0dHA6Ly9jYWNlcnRzLnRoYXd0ZS5jb20v\n" +
                            "VGhhd3RlUlNBQ0EyMDE4LmNydDAMBgNVHRMBAf8EAjAAMIIBfgYKKwYBBAHWeQIE\n" +
                            "AgSCAW4EggFqAWgAdQApeb7wnjk5IfBWc59jpXflvld9nGAK+PlNXSZcJV3HhAAA\n" +
                            "AX1zinDjAAAEAwBGMEQCIEbQTcwgYYSMKrcPF7TajkKYMca2RSbC6X6USgU/6Lo8\n" +
                            "AiA6E/fOgkW9CWqXrsiF0wgZdUq/vHfC3zPyo9tVfLdGKwB2AEHIyrHfIkZKEMah\n" +
                            "OglCh15OMYsbA+vrS8do8JBilgb2AAABfXOKcOsAAAQDAEcwRQIhAI6Vl0NhDdY9\n" +
                            "2VGstJIhOSIGZPk8tFKWyIGunnaFpxH/AiBOw9bgt+z6jmLeHYYQhj365+rJYTWJ\n" +
                            "rqugE0kTfhTVdQB3AN+lXqtogk8fbK3uuF9OPlrqzaISpGpejjsSwCBEXCpzAAAB\n" +
                            "fXOKcTEAAAQDAEgwRgIhAIZUP1DXclUZxI3Zx+dRyi4MTSw96kzAeTRv61AirR1Z\n" +
                            "AiEA9b5/+CW47kZgjccYHvf1JZleclwUmuOgf9boRe/Bn+kwDQYJKoZIhvcNAQEL\n" +
                            "BQADggEBAMkpM9vpP+rqK4FdT8q5Wul1/VPZIhYYcDNixmquLqhHUSx6iYY/sqh2\n" +
                            "t5dpIQfrkPiU/VkdbzKacF5Y/kJPTRVMYktxdF4UOZWmGBwDYOKsZcyzPf+TIgef\n" +
                            "KLU+MZQmDlcZp118NEfkyjhC3SA+LW33rxcp7kXMxYkph9Xi1FCjvVRcOTWtC5MF\n" +
                            "RWMk2KtcBc9cWfX0umTmoFqrTxH3MJkZM7Rskr60s3EDGZaTcLW3aL/hv92+SZc7\n" +
                            "gQnCYoKn0Nb1ysOUWZXOJ/BTLGOD69wDuGLxIlFVqCGZBoN2eTTQwwdtOpON7tzm\n" +
                            "h0yrP24SpIrLvhfwuAomS1AoHstK0eA=\n" +
                            "-----END CERTIFICATE-----";

                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream caInput = new ByteArrayInputStream(certificateString.getBytes());
                    Certificate ca = cf.generateCertificate(caInput);

                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    if (ca == null) {
                        Log.e("인증서 정보= ", "null");
                    }
                    keyStore.setCertificateEntry("ca", ca);

                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

                    for (TrustManager t : tmf.getTrustManagers()) {
                        if (t instanceof X509TrustManager) {
                            X509TrustManager trustManager = (X509TrustManager) t;
                            Bundle bundle = SslCertificate.saveState(er.getCertificate());
                            X509Certificate x509Certificate;
                            byte[] bytes = bundle.getByteArray("x509-certificate");

                            if (bytes == null) {
                                x509Certificate = null;

                            } else {
                                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                                Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(bytes));

                                System.out.println("인증서 정보= " + ((X509Certificate) cert).getSubjectDN());
                                x509Certificate = (X509Certificate) cert;
                            }
                            X509Certificate[] x509Certificates = new X509Certificate[1];
                            x509Certificates[0] = x509Certificate;

                            trustManager.checkServerTrusted(x509Certificates, "ECDH_RSA");
                        }
                    }
                    Log.d(TAG, "Certificate from " + er.getUrl() + " is trusted.");
                    handler.proceed();

                } catch (Exception e) {
                    Log.d(TAG, "Failed to access " + er.getUrl() + ". Error: " + er.getPrimaryError());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    String message = "SSL Certificate error.\n";
                    switch (er.getPrimaryError()) {
                        case SslError.SSL_NOTYETVALID:
                            message = "이 사이트의 보안 인증서가 아직 유효하지 않습니다.\n";
                            break;
                        case SslError.SSL_EXPIRED:
                            message = "이 사이트의  보안 인증서가 만료되었습니다.\n";
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = "이 사이트의 보안 인증서 ID가 일치하지 않습니다.\n";
                            break;
                        case SslError.SSL_UNTRUSTED:
                            message = "이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n";
                            break;
                        case SslError.SSL_DATE_INVALID:
                            message = "이 사이트의 보안 인증서 날짜가 잘못되었습니다.\n";
                            break;
                        case SslError.SSL_INVALID:
                            message = "이 사이트의 보안 인증서에 대한 문제가 발생하였습니다.\n";
                            break;
                    }
                    message += "계속하시겠습니까?";

                    builder.setMessage(message);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            //2021.06 사고속보조회 ssl적용완
         /*   @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("이 사이트의 보안 인증서는 신뢰하는 보안 인증서가 아닙니다. 계속하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }*/




            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }*/
        });

        //추후 사고의 아이디와 rptId는 목록에서 가져와서 보내줘야 함.
        Log.i("acid", this.getAcdnt_id());
        Log.i("acid", this.getRpt_id());
        Log.i("acid", "https://oneclickapp.ex.co.kr:9443/selectAcdntReport.do?acdntId= " + this.getAcdnt_id() + "&rptId=" + this.getRpt_id());

        //2021.09 사고속보조회 임시방편
        situation_webview.loadUrl("https://oneclickapp.ex.co.kr:9443/selectAcdntReport.do?acdntId=" + this.getAcdnt_id() + "&rptId=" + this.getRpt_id()); //*2021운영
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            situation_webview.destroy();
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("웹뷰 ", "onDestroy");
        Log.println(Log.ASSERT, "", "웹뷰:" + "onDestroy");
    }
}
