package org.dragberry.ozo.http;

import com.badlogic.gdx.Gdx;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Dragun_M on 3/3/2017.
 */

public abstract class GetHttpTask<R> extends HttpTask<Void, R> {

    private static final String TAG = HttpTask.class.getName();

    public GetHttpTask(Class<R> resultClass, String urlTemplate, Object... urlParams) {
        super(resultClass, urlTemplate, urlParams);
    }

    @Override
    protected R doRequest(RestTemplate restTemplate) {
        Gdx.app.debug(TAG, "do GET request: " + url);
        return restTemplate.getForObject(url, resultClass);
    }
}