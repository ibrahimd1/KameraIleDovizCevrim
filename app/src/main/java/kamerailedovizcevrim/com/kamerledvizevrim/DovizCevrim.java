package kamerailedovizcevrim.com.kamerledvizevrim;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
    MyAsyncTask apiCaller;
    String m_StrOran;

    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            onBackPressed();
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

        fiyat = 0d;
        getServisOran(kaynakDoviz, hedefDoviz);

        btnApi= findViewById(R.id.btnApi);
        btnApi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View arg0) {

                Double sonuc = fiyat * (Math.round(Double.parseDouble(m_StrOran) * 100.0) / 100.0);
                sonuc = Math.round(sonuc * 100.0) / 100.0;
                mTextView.setText(fiyat.toString() + " " + kaynakDoviz + " : " + sonuc.toString() + " " + hedefDoviz);
            }
        });
        kamerayiBaslat();

        AdView adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest); //adView i yüklüyoruz
    }

    private void kamerayiBaslat(){
        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);
        startCameraSource();
    }

    private void getServisOran(String inKaynakDoviz,String inHedefDoviz){

        apiCaller = new MyAsyncTask(inKaynakDoviz, inHedefDoviz, DovizCevrim.this, "Yükleniyor...");

        try {
            m_StrOran = apiCaller.execute().get();
            if(m_StrOran==null ) {
                new AlertDialog.Builder(this)
                        .setTitle("Servis Hata!")
                        .setMessage("Servis şu anda yanıt vermiyor.Lütfen daha sonra tekrar deneyiniz.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                                System.exit(0);
                            }
                        }).create().show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    if (isNumeric(item.getValue().replace(',', '.'))) {
                                        fiyat = Double.parseDouble(item.getValue().replace(".","").replace(',', '.'));
                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
