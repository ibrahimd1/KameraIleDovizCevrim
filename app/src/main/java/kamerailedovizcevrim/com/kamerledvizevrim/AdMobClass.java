package kamerailedovizcevrim.com.kamerledvizevrim;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Ibrahim on 3.02.2019.
 */

public class AdMobClass {

    private static final String reklamID = "ca-app-pub-4739427604654377/4486122267";
    public static InterstitialAd getInstance(Context context) {
        InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(reklamID);
        return mInterstitialAd;
    }
    public static AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice("5D4729103FAD02866CF82284B04568E5")
                .build();
    }
}
