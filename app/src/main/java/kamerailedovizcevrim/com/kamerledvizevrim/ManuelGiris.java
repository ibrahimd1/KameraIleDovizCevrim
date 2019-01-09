package kamerailedovizcevrim.com.kamerledvizevrim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

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
    Double m_Oran;
    String m_StrOran;
    Yardimci m_Yardimci = new Yardimci();
    StringBuilder strSonucMesaj;
    Double m_Sonuc;
    Yardimci yardimciSinif = new Yardimci();
    private Spinner spinnerManuelKaynakDoviz;
    private Spinner spinnerManuelHedefDoviz;
    private ArrayAdapter<String> dataAdapterForKaynakDoviz;
    private ArrayAdapter<String> dataAdapterForHedefDoviz;
    private String[] dovizlerDeneme = yardimciSinif.Dovizler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuel_giris);

        bundle = getIntent().getExtras();
        if (bundle!=null){
            hedefDoviz = bundle.getString("HedefDoviz");
            kaynakDoviz = bundle.getString("KaynakDoviz");
        }

        spinnerManuelKaynakDoviz = (Spinner) findViewById(R.id.spinnerManuelKaynak);
        spinnerManuelHedefDoviz = (Spinner) findViewById(R.id.spinnerManuelHedef);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForKaynakDoviz = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dovizlerDeneme);
        dataAdapterForHedefDoviz = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dovizlerDeneme);

        dataAdapterForKaynakDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForHedefDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerManuelKaynakDoviz.setAdapter(dataAdapterForKaynakDoviz);
        spinnerManuelHedefDoviz.setAdapter(dataAdapterForHedefDoviz);

        edtManuelaTutar = (EditText) findViewById(R.id.edtManuelTutar);
        txtManuelSonuc = (TextView) findViewById(R.id.txtManuelSonuc);

        btnManuelCevir = (Button) findViewById(R.id.btnManuelCevir);
        btnManuelCevir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtManuelaTutar.getText() == null || edtManuelaTutar.getText().length() == 0) {
                    txtManuelSonuc.setText("Tutar alanı boş geçilemez!");
                }
                else {
                    if (m_Oran == null || m_Oran == 0) {

                        apiCaller = new MyAsyncTask(kaynakDoviz, hedefDoviz, ManuelGiris.this, "Yükleniyor...");

                        try {
                            m_StrOran = apiCaller.execute().get();
                            m_Oran = Double.parseDouble(m_StrOran);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    Double sonuc = Double.valueOf(edtManuelaTutar.getText().toString()) * Math.round(Double.parseDouble(m_StrOran));
                    m_Sonuc = sonuc;
                    strSonucMesaj = new StringBuilder();
                    strSonucMesaj.append(edtManuelaTutar.getText());
                    strSonucMesaj.append(" ");
                    strSonucMesaj.append(kaynakDoviz);
                    strSonucMesaj.append(" = ");
                    strSonucMesaj.append(m_Sonuc.toString());
                    strSonucMesaj.append(" ");
                    strSonucMesaj.append(hedefDoviz);

                    txtManuelSonuc.setText(strSonucMesaj.toString());
                    m_Yardimci.Mesaj("Hedef: " + hedefDoviz + " Kaynak: " + kaynakDoviz + " Oran: " + m_StrOran, ManuelGiris.this);
                }
            }
        });
    }
}
