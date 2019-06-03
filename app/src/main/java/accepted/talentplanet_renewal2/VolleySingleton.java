package accepted.talentplanet_renewal2;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by kwonhong on 2017-12-03.
 */

public class VolleySingleton {
    private static  VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleySingleton(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruBitmapCache(mCtx);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        HurlStack hurlStack = new HurlStack(){
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException{
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try{
                    httpsURLConnection.setSSLSocketFactory(newSslSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                }catch (Exception e){
                    e.printStackTrace();
                }

                return httpsURLConnection;
            }
        };

        if(mRequestQueue == null){

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), hurlStack);
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public ImageLoader getmImageLoader(){
        return mImageLoader;
    }



    private SSLSocketFactory newSslSocketFactory(){
        try{
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(mCtx.getApplicationContext().getResources().openRawResource(R.raw.client), "accepted9091".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");

            TrustManager[] trustManagers = tmf.getTrustManagers();
            context.init(null, trustManagers, null);
            SSLSocketFactory sf = context.getSocketFactory();

            return sf;
        }catch (Exception e){
            throw new AssertionError(e);
        }
    }

    private HostnameVerifier getHostnameVerifier(){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return true;
            }
        };

        return hostnameVerifier;
    }

    private SSLSocketFactory sslSocketFactory(){
        try{
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream in = mCtx.getApplicationContext().getResources().openRawResource(R.raw.accepted);
            Certificate ca;
            try{
                ca = cf.generateCertificate(in);
                Log.d("ca","ca=" + ((X509Certificate) ca).getSubjectDN());
            }finally {
                in.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL url = new URL("https://221.162.94.43:8443/Accepted");
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            InputStream in2 = urlConnection.getInputStream();

            return context.getSocketFactory();

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(CertificateException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(KeyStoreException e){
            e.printStackTrace();
        }catch (KeyManagementException e){
            e.printStackTrace();
        }

        return null;
    }
}
