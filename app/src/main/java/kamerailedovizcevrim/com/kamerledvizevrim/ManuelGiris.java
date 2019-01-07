package kamerailedovizcevrim.com.kamerledvizevrim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Long m_Oran;
    String asd;
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
                    try {
                        asd = apiCaller.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    m_Yardimci.Mesaj("Hedef: " + hedefDoviz + " Kaynak: " + kaynakDoviz + " Oran: " + asd, ManuelGiris.this);
                }
            }
        });
    }
}
