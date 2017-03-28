package org.dragberry.ozo.http;

import org.dragberry.ozo.common.CommonConstants;

import java.io.Serializable;

/**
 * Created by Dragun_M on 3/2/2017.
 */

public interface HttpClient {

    interface URL {

//      String ROOT = "http://10.0.2.2:8087/ozo-backend-war";
        String ROOT = "http://192.168.0.104:8080/ozo-backend-war/app/" + CommonConstants.APP_VERSION;


        String NEW_USER = "/user/new";

        String GET_ALL_RESULTS = "/results/user/{0}/levels";
        String NEW_RESULT = "/level/result/new";

        String NEW_AUDIT_EVENT = "/audit/events/new/";
    }


    boolean isConnected();

    <T, R>  void executeTask(HttpTask<T, R> httpTask);
}
