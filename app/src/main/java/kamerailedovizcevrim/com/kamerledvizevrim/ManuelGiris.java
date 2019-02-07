package kamerailedovizcevrim.com.kamerledvizevrim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ibrahim on 2.01.2019.
 */

public class ManuelGiris extends AppCompatActivity {

    String kaynakDoviz;
    String hedefDoviz;
    String kaynakDovizTam;
    String hedefDovizTam;
    Bundle bundle;
    MyAsyncTask apiCaller;
    String m_StrOran;
    Yardimci m_Yardimci = new Yardimci();
    Double m_Sonuc;
    private Spinner spinnerManuelKaynakDoviz;
    private Spinner spinnerManuelHedefDoviz;
    private ArrayAdapter<String> dataAdapterForKaynakDoviz;
    private ArrayAdapter<String> dataAdapterForHedefDoviz;
    private String[] dovizlerDeneme = m_Yardimci.Dovizler;
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

        spinnerManuelKaynakDoviz = findViewById(R.id.spinnerManuelKaynak);
        spinnerManuelHedefDoviz = findViewById(R.id.spinnerManuelHedef);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForKaynakDoviz = new ArrayAdapter<>(this, R.layout.spinnertext, dovizlerDeneme);
        dataAdapterForHedefDoviz = new ArrayAdapter<>(this, R.layout.spinnertext, dovizlerDeneme);

        dataAdapterForKaynakDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForHedefDoviz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerManuelKaynakDoviz.setAdapter(dataAdapterForKaynakDoviz);
        spinnerManuelHedefDoviz.setAdapter(dataAdapterForHedefDoviz);

        ArrayList<String> dovizDizi = new ArrayList<>(Arrays.asList(dovizlerDeneme));
        int hedefDovizPosition = dovizDizi.indexOf(hedefDovizTam);
        int kaynakDovizPosition = dovizDizi.indexOf(kaynakDovizTam);
        spinnerManuelHedefDoviz.setSelection(hedefDovizPosition);
        spinnerManuelKaynakDoviz.setSelection(kaynakDovizPosition);

        edtManuelHedefText = findViewById(R.id.edtManuelHedefText);
        edtManuelKaynakText = findViewById(R.id.edtManuelKaynakText);

        edtManuelHedefText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
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
               if(m_Durum && edtManuelHedefText.isFocused()) {
                   if (edtManuelHedefText.getText().toString() != null && edtManuelHedefText.getText().toString().length() > 0) {
                       m_Sonuc = Double.valueOf(edtManuelHedefText.getText().toString()) * (Math.round(Double.parseDouble(m_StrOran) * 100.0) / 100.0);
                       m_Sonuc = Math.round(m_Sonuc * 100.0) / 100.0;
                       edtManuelKaynakText.setText(m_Sonuc.toString());
                   } else
                       edtManuelKaynakText.setText("");
                   m_Durum = edtManuelHedefText.isFocused();
               }
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
                if(m_Durum && edtManuelKaynakText.isFocused()) {
                    if (edtManuelKaynakText.getText().toString() != null && edtManuelKaynakText.getText().toString().length() > 0) {
                        m_Sonuc = Double.valueOf(edtManuelKaynakText.getText().toString()) / (Math.round(Double.parseDouble(m_StrOran) * 100.0) / 100.0);
                        m_Sonuc = Math.round(m_Sonuc * 100.0) / 100.0;
                        edtManuelHedefText.setText(m_Sonuc.toString());
                    } else
                        edtManuelHedefText.setText("");
                    m_Durum = edtManuelKaynakText.isFocused();
                }
            }
        });

        AdView adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("5D4729103FAD02866CF82284B04568E5").build();
        adView.loadAd(adRequest); //adView i yüklüyoruz
    }

    private void getServisOran(String inKaynakDoviz,String inHedefDoviz){

            apiCaller = new MyAsyncTask(inKaynakDoviz, inHedefDoviz, ManuelGiris.this, "Yükleniyor...");

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
}

