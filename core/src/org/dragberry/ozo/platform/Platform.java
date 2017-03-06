package org.dragberry.ozo.platform;

import org.dragberry.ozo.admob.AdsController;
import org.dragberry.ozo.http.HttpClient;

/**
 * Created by Dragun_M on 3/3/2017.
 */

public interface Platform {

    User getUser();

    HttpClient getHttpClient();

    AdsController getAdsController();
}
