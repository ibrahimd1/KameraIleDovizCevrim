package kamerailedovizcevrim.com.kamerledvizevrim;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ibrahim on 2.01.2019.
 */

public class ManuelGiris extends AppCompatActivity {

    Button btnManuelGiris;
    EditText edtManuelaTutar;
    TextView txtManuelSonuc;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuel_giris);

        /*edtManuelaTutar = (EditText) findViewById(R.id.edtManuelTutar);
        txtManuelSonuc=(TextView)findViewById(R.id.txtManuelSonuc);

        btnManuelGiris = (Button) findViewById(R.id.butonManuelCevrim);
        btnManuelGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtManuelaTutar.getText() == null) {
                    txtManuelSonuc.setText("Tutar alanı boş geçilemez!");
                }
                else {

                }
            }
        });*/
    }

    /*private  class myAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }*/
}
