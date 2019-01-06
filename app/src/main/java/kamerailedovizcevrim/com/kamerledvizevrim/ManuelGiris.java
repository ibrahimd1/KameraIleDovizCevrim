package kamerailedovizcevrim.com.kamerledvizevrim;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ibrahim on 2.01.2019.
 */

public class ManuelGiris extends AppCompatActivity {

    Button btnManuelCevir;
    EditText edtManuelaTutar;
    TextView txtManuelSonuc;
    String kaynakDoviz;
    String hedefDoviz;
    Bundle bundle;
    MyAsyncTask apiCaller;
    Long m_Oran;
    Yardimci m_Yardimci = new Yardimci();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuel_giris);

        bundle = getIntent().getExtras();
        if (bundle!=null){
            hedefDoviz = bundle.getString("HedefDoviz");
            kaynakDoviz = bundle.getString("KaynakDoviz");
        }
        edtManuelaTutar = (EditText) findViewById(R.id.edtManuelTutar);
        txtManuelSonuc=(TextView)findViewById(R.id.txtManuelSonuc);

        btnManuelCevir = (Button) findViewById(R.id.btnManuelCevir);
        btnManuelCevir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtManuelaTutar.getText() == null || edtManuelaTutar.getText().length() == 0) {
                    txtManuelSonuc.setText("Tutar alanı boş geçilemez!");
                } else {
                    apiCaller = new MyAsyncTask(kaynakDoviz, hedefDoviz, ManuelGiris.this, m_Oran, "Yükleniyor...");
                    apiCaller.execute();
                    m_Yardimci.Mesaj("Oran: " + m_Oran, ManuelGiris.this);
                }
            }
        });
    }
}
