package org.dragberry.ozo.admob;

/**
 * Created by maksim on 15.02.17.
 */
public class AdsControllerAdapter implements AdsController {

    @Override
    public void showBannerAd() {}

    @Override
    public void showBannerAdNew() {}

    @Override
    public void hideBannerAd() {}

    @Override
    public boolean isBannerShown() {
        return false;
    }

    @Override
    public void showInterstitialAd(Runnable then) {}
}
