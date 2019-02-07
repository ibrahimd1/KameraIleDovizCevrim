package kamerailedovizcevrim.com.kamerledvizevrim;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
 * Created by Ibrahim on 6.01.2019.
 */

public  class MyAsyncTask extends AsyncTask<Void,Void,String> {

    String m_KaynakDoviz;
    String m_HedefDoviz;
    Context m_Context;
    String modalMesaj;
    ProgressDialog dialog;
    String m_Mesaj;
    String oran;

    public MyAsyncTask(String kaynakDoviz, String hedefDoviz, Context context, String mesaj){
        m_KaynakDoviz = kaynakDoviz;
        m_HedefDoviz = hedefDoviz;
        m_Context = context;
        m_Mesaj = mesaj;
        this.modalMesaj = m_Mesaj;
        this.dialog = new ProgressDialog(m_Context);
    }

    JSONObject jsonObject = null;
    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder link = new StringBuilder();
        link.append("http://free.currencyconverterapi.com/api/v5/convert?q=");
        link.append(m_KaynakDoviz);
        link.append("_");
        link.append(m_HedefDoviz);
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
                oran = jsonObject.getJSONObject(m_KaynakDoviz + "_" + m_HedefDoviz).getString("val").toString();
                instream.close();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return oran;
    }

    public static String convertStreamToString(InputStream is) {

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
}