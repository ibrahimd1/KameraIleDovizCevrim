package kamerailedovizcevrim.com.kamerledvizevrim;

import android.app.Activity;
import android.os.Bundle;
import  android.content.Intent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Ibrahim on 11.11.2018.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ImageView img = findViewById(R.id.imageSplashScreen);
        img.setVisibility(View.VISIBLE);


        Thread timerThread=new Thread(){
          public void run(){
              try{
                  sleep(2000);
              }catch(InterruptedException e){
                  e.printStackTrace();
              }finally{
                  Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                  startActivity(intent);
              }
          }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
