package kamerailedovizcevrim.com.kamerledvizevrim;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Ibrahim on 26.11.2018.
 */

public class DovizCevrim extends AppCompatActivity implements View.OnClickListener {
    SurfaceView mCameraView;
    TextView mTextView;
    CameraSource mCameraSource;
    Button btnApi;
    Double fiyat;
    Bundle bundle;
    String hedefDoviz;
    String kaynakDoviz;

    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;

    @Override
    public void onClick(View v) {
        new myAsyncTask("Yükleniyor").execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Log.i(TAG, "Geriye basıldı");
            setContentView(R.layout.doviz_cevrim);
            kamerayiBaslat();
            Mesaj("Geriiiii");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doviz_cevrim);

        bundle = getIntent().getExtras();
        if (bundle!=null){
            hedefDoviz = bundle.getString("HedefDoviz");
            kaynakDoviz = bundle.getString("KaynakDoviz");
        }

        fiyat = Double.valueOf(0);
        List<String> dovizler = getDovizBilgi();
        String asd = "";
        for (String s : dovizler)
        {
            asd += s + "\t";
        }
        Mesaj(asd);

        Log.i("TCMB",asd);

        btnApi=(Button)findViewById(R.id.btnApi);
        btnApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new myAsyncTask("Yükleniyor").execute();
            }
        });
        kamerayiBaslat();
    }

    private void kamerayiBaslat(){
        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);
        startCameraSource();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(DovizCevrim.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    if (isNumeric(item.getValue().replace(',', '.'))) {
                                        //stringBuilder.append(item.getValue());
                                        //stringBuilder.append("\n");
                                        fiyat = Double.parseDouble(item.getValue().replace(',', '.'));
                                        break;
                                    }
                                }
                                //mTextView.setText(stringBuilder.toString());
                                //stop();
                            }
                        });
                    }
                }
            });
        }
    }

    public void stop(){

        if(mCameraSource != null){
            //this.autoFocusEngine.stop();
            mCameraSource.stop();
            //mCameraSource = null;
        }
        Log.d(TAG, "CameraEngine Stopped");
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        String modalMesaj;
        ProgressDialog dialog;

        String productName = "";
        String categoryName = "";
        String unitPrice = "";

        JSONObject jsonObject = null;

        public myAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(DovizCevrim.this);
        }

        protected void onPreExecute() {
            /*dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //String url = "https://www.doviz.com/api/v1/currencies/EUR/latest";
            StringBuilder link = new StringBuilder();
            link.append("http://free.currencyconverterapi.com/api/v5/convert?q=");
            link.append(kaynakDoviz);
            link.append("_");
            link.append(hedefDoviz);
            link.append("&compact=y");

            String url = link.toString();

            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(url);

            HttpResponse response;
            try {
                response = httpclient.execute(httpget);

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);

                    jsonObject = new JSONObject(result);

                    instream.close();
                }

            } catch (ClientProtocolException e) {
                Mesaj(e.getMessage());
            } catch (IOException e) {
                Mesaj(e.getMessage());
            } catch (JSONException e) {
                Mesaj(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing())
                dialog.dismiss();

            try {

                String oran = jsonObject.getJSONObject(kaynakDoviz + "_" + hedefDoviz).getString("val").toString();

                Double sonuc = Double.valueOf(fiyat) * Math.round(Double.parseDouble(oran));
                mTextView.setText(fiyat.toString() + " " + kaynakDoviz + " : " + sonuc.toString() + " " + hedefDoviz);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void Mesaj(String s) {

        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private List<String> getDovizBilgi() {
        //uygulama yanıt vermiyor hatasının önüne geçmek için
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String dobiz_url = "http://www.tcmb.gov.tr/kurlar/today.xml"; //merkez bankasının xml dosyasını çekeceğiz

        List<String> doviz_list = new ArrayList<>(); //oluşturduğum döviz listesi
        HttpURLConnection baglanti = null; //web servise ulaşmak için bağlantı oluşturdum. başlangıç değeri null

        try {
            URL url = new URL(dobiz_url);

            baglanti=(HttpURLConnection) url.openConnection(); //bağlantıyı açıyoruz

            int baglanti_durumu=baglanti.getResponseCode();//bağlantı başarılı mı kontrol ediyoz. integer bir değer döner

            if(baglanti_durumu==HttpURLConnection.HTTP_OK){ //HTTP_NOTFOUND da bağlantı başarısız mı diye kontrol eder.

                BufferedInputStream stream=new BufferedInputStream(baglanti.getInputStream());
                DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance(); //kendi sınıf içerisinde nesne örneklemesi yapar newinstance();
                DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();

                Document document =documentBuilder.parse(stream); // document değişkenim var, parse ettim, //iki farklı paket var import tan

                NodeList dovizNodeList = document.getElementsByTagName("Currency");//liste tagname le ulaştık. id le ulaşmak gibi id si yok bunun

                for(int i=0; i<dovizNodeList.getLength(); i++ ) { //bütün etiketlere ulaşmak için for. kaç tane etiket varsa length

                    Element element = (Element) dovizNodeList.item(i); //3farklı paket var

                    NodeList nodeListBirim = element.getElementsByTagName("Unit");
                    NodeList nodeListParaBirimi = element.getElementsByTagName("Isim");
                    NodeList nodeListAlis = element.getElementsByTagName("ForexBuying");
                    NodeList nodeListSatis = element.getElementsByTagName("ForexSelling");

                    String birim = nodeListBirim.item(0).getFirstChild().getNodeValue();//ilk eleman, ilk çocuk,ilk değer
                    String parabirimi = nodeListParaBirimi.item(0).getFirstChild().getNodeValue();
                    String alis = nodeListAlis.item(0).getFirstChild().getNodeValue();
                    String satis = nodeListSatis.item(0).getFirstChild().getNodeValue();
                    if (element.getAttribute("Kod").equals("USD") || element.getAttribute("Kod").equals("IQD") || element.getAttribute("Kod").equals("EUR")) {
                        doviz_list.add(birim + " " + parabirimi + " Alış: " + alis + " Satış: " + satis);
                    }
                }
            }
        }catch (Exception e){
            Log.e("Xml parse hatası",e.getLocalizedMessage().toString()); //hata mesajı

        }
        finally { //hata olsun olmasın çalışacak. internet bağlantısı varsa
            if(baglanti !=null){
                baglanti.disconnect();
            }
        }
        return doviz_list;
    }
}
