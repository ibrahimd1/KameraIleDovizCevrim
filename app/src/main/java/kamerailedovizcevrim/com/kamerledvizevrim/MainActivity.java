package kamerailedovizcevrim.com.kamerledvizevrim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Spinner içerisine koyacağımız verileri tanımlıyoruz.
    Yardimci yardimciSinif = new Yardimci();
    private String[] kaynakDovizEleman={"USD","EUR"};
    private String[] hedefDovizEleman={"TRY"};
    private String[] dovizlerDeneme = yardimciSinif.Dovizler;

    //Spinner'ları ve Adapter'lerini tanımlıyoruz.
    private Spinner spinnerKaynakDoviz;
    private Spinner spinnerHedefDoviz;
    private ArrayAdapter<String> dataAdapterForKaynakDoviz;
    private ArrayAdapter<String> dataAdapterForHedefDoviz;
    private Button btnCevirmAc;

    private String m_KaynakDoviz;
    private String m_HedefDoviz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerKaynakDoviz = (Spinner) findViewById(R.id.kaynakDoviz);
        spinnerHedefDoviz = (Spinner) findViewById(R.id.hedefDoviz);
        btnCevirmAc = (Button) findViewById(R.id.buttonCevrimAc);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForKaynakDoviz = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dovizlerDeneme);
        dataAdapterForHedefDoviz = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dovizlerDeneme);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHedefDoviz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_HedefDoviz = parent.getSelectedItem().toString().substring(0,3);
                Toast.makeText(MainActivity.this, "Hedef: "+m_HedefDoviz, Toast.LENGTH_SHORT).show();
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

    }
}
