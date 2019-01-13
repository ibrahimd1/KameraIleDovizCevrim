package kamerailedovizcevrim.com.kamerledvizevrim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
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
    String kaynakDovizTam;
    String hedefDovizTam;
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
    EditText edtManuelKaynakText;
    EditText edtManuelHedefText;
    boolean m_Durum = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuel_giris);

        bundle = getIntent().getExtras();
        if (bundle!=null){
            hedefDoviz = bundle.getString("HedefDoviz");
            kaynakDoviz = bundle.getString("KaynakDoviz");
            hedefDovizTam = bundle.getString("HedefDovizTam");
            kaynakDovizTam = bundle.getString("KaynakDovizTam");
        }

        //getServisOran(kaynakDoviz, hedefDoviz);

        m_Yardimci.Mesaj("Oran:" + m_Oran, ManuelGiris.this);

        spinnerManuelKaynakDoviz = (Spinner) findViewById(R.id.spinnerManuelKaynak);
        spinnerManuelHedefDoviz = (Spinner) findViewById(R.id.spinnerManuelHedef);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForKaynakDoviz = new ArrayAdapter<String>(this, R.layout.spinnertext, dovizlerDeneme);
        dataAdapterForHedefDoviz = new ArrayAdapter<String>(this, R.layout.spinnertext, dovizlerDeneme);

        dataAdapterForKaynakDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForHedefDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerManuelKaynakDoviz.setAdapter(dataAdapterForKaynakDoviz);
        spinnerManuelHedefDoviz.setAdapter(dataAdapterForHedefDoviz);

        ArrayList<String> dovizDizi = new ArrayList<String>(Arrays.asList(dovizlerDeneme));
        int hedefDovizPosition = dovizDizi.indexOf(hedefDovizTam);
        int kaynakDovizPosition = dovizDizi.indexOf(kaynakDovizTam);
        spinnerManuelHedefDoviz.setSelection(hedefDovizPosition);
        spinnerManuelKaynakDoviz.setSelection(kaynakDovizPosition);

        edtManuelHedefText = (EditText) findViewById(R.id.edtManuelHedefText);
        edtManuelKaynakText = (EditText) findViewById(R.id.edtManuelKaynakText);

        edtManuelHedefText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        /*btnManuelCevir.setOnClickListener(new View.OnClickListener() {
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
                    Double sonuc = Double.valueOf(edtManuelaTutar.getText().toString()) * (Math.round(Double.parseDouble(m_StrOran) * 100.0) / 100.0);

                    strSonucMesaj = new StringBuilder();
                    strSonucMesaj.append(edtManuelaTutar.getText());
                    strSonucMesaj.append(" ");
                    strSonucMesaj.append(kaynakDoviz);
                    strSonucMesaj.append(" = ");
                    strSonucMesaj.append(sonuc.toString());
                    strSonucMesaj.append(" ");
                    strSonucMesaj.append(hedefDoviz);

                    txtManuelSonuc.setText(strSonucMesaj.toString());
                    m_Yardimci.Mesaj("Hedef: " + hedefDoviz + " Kaynak: " + kaynakDoviz + " Oran: " + m_StrOran, ManuelGiris.this);
                }
            }
        });*/

        spinnerManuelKaynakDoviz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtManuelKaynakText.setText("");
                edtManuelHedefText.setText("");
                getServisOran(spinnerManuelKaynakDoviz.getSelectedItem().toString().substring(0,3),spinnerManuelHedefDoviz.getSelectedItem().toString().substring(0,3));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerManuelHedefDoviz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtManuelKaynakText.setText("");
                edtManuelHedefText.setText("");
                getServisOran(spinnerManuelKaynakDoviz.getSelectedItem().toString().substring(0,3),spinnerManuelHedefDoviz.getSelectedItem().toString().substring(0,3));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtManuelHedefText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtManuelHedefText.getText().toString() != null && edtManuelHedefText.getText().toString().length() > 0) {
                    m_Sonuc = Double.valueOf(edtManuelHedefText.getText().toString()) * (Math.round(Double.parseDouble(m_StrOran) * 100.0) / 100.0);
                    edtManuelKaynakText.setText(m_Sonuc.toString());
                }
                else
                    edtManuelKaynakText.setText("");
            }
        });

        edtManuelKaynakText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (m_Durum) {
                /*m_Sonuc = Double.valueOf(edtManuelKaynakText.getText().toString()) / (Math.round(Double.parseDouble(m_StrOran) * 100.0) / 100.0);
                edtManuelHedefText.setText(m_Sonuc.toString());
                    Log.i("BILGI", "edtManuelKaynakText " + edtManuelKaynakText.getText().toString());
                    edtManuelHedefText.setText("5");
                    m_Durum = false;
                }*/
            }
        });
    }

    private void getServisOran(String inKaynakDoviz,String inHedefDoviz){

            apiCaller = new MyAsyncTask(inKaynakDoviz, inHedefDoviz, ManuelGiris.this, "Yükleniyor...");

            try {
                m_StrOran = apiCaller.execute().get();
                m_Oran = Double.parseDouble(m_StrOran);
                Log.i("OranDegisti", inKaynakDoviz + " " + inHedefDoviz + " " + m_Oran);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
    }

    private Double sonucHesapla(String tutar, String oran) {
        return Double.valueOf(tutar) * (Math.round(Double.parseDouble(oran) * 100.0) / 100.0);
    }
}

