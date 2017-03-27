package org.dragberry.ozo.platform;

import org.dragberry.ozo.admob.AdsController;
import org.dragberry.ozo.admob.AdsControllerAdapter;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.HttpClientAdapter;

/**
 * Created by Dragun_M on 3/3/2017.
 */

public class PlatformAdapter implements Platform {

    private AdsController adsController = new AdsControllerAdapter();

    private HttpClient httpClient = new HttpClientAdapter();

    private User user = new UserImpl();

    @Override
    public User getUser() {
       return user;
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public AdsController getAdsController() {
        return adsController;
    }

    @Override
    public void openMarketUrl() {}
}
