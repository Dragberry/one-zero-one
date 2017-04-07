package org.dragberry.ozo.admob;

/**
 * Created by maksim on 15.02.17.
 */

public interface AdsController {

    void showBannerAd();

    void showBannerAdNew();

    void hideBannerAd();

    boolean isBannerShown();

    void showInterstitialAd(Runnable then);
}
