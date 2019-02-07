package kamerailedovizcevrim.com.kamerledvizevrim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    //Spinner içerisine koyacağımız verileri tanımlıyoruz.
    Yardimci yardimciSinif = new Yardimci();
    private String[] dovizlerDeneme = yardimciSinif.Dovizler;

    //Spinner'ları ve Adapter'lerini tanımlıyoruz.
    private Spinner spinnerKaynakDoviz;
    private Spinner spinnerHedefDoviz;
    private ArrayAdapter<String> dataAdapterForKaynakDoviz;
    private ArrayAdapter<String> dataAdapterForHedefDoviz;
    private Button btnCevirmAc;
    private Button btnManuelGiris;

    private String m_KaynakDoviz;
    private String m_HedefDoviz;
    private String m_KaynakDovizTam;
    private String m_HedefDovizTam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerKaynakDoviz = findViewById(R.id.kaynakDoviz);
        spinnerHedefDoviz = findViewById(R.id.hedefDoviz);
        btnCevirmAc = findViewById(R.id.buttonCevrimAc);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForKaynakDoviz = new ArrayAdapter<>(this, R.layout.spinner_text_main, dovizlerDeneme);
        dataAdapterForHedefDoviz = new ArrayAdapter<>(this, R.layout.spinner_text_main, dovizlerDeneme);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForKaynakDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForHedefDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerKaynakDoviz.setAdapter(dataAdapterForKaynakDoviz);
        spinnerHedefDoviz.setAdapter(dataAdapterForHedefDoviz);

        spinnerKaynakDoviz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_KaynakDoviz = parent.getSelectedItem().toString().substring(0,3);
                m_KaynakDovizTam = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHedefDoviz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_HedefDoviz = parent.getSelectedItem().toString().substring(0,3);
                m_HedefDovizTam = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCevirmAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DovizCevrim.class);
                intent.putExtra("KaynakDoviz",m_KaynakDoviz);
                intent.putExtra("HedefDoviz", m_HedefDoviz);
                startActivity(intent);
            }
        });

        btnManuelGiris = findViewById(R.id.butonManuelCevrim);
        btnManuelGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManuelGiris.class);
                intent.putExtra("KaynakDoviz",m_KaynakDoviz);
                intent.putExtra("HedefDoviz", m_HedefDoviz);
                intent.putExtra("KaynakDovizTam",m_KaynakDovizTam);
                intent.putExtra("HedefDovizTam", m_HedefDovizTam);
                startActivity(intent);
            }
        });

        AdView adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("5D4729103FAD02866CF82284B04568E5").build();
        adView.loadAd(adRequest); //adView i yüklüyoruz

    }
}
