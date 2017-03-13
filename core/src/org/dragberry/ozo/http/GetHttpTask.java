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
    protected R doRequest() {
        Gdx.app.debug(TAG, "do GET request: " + url);
        return getRestTemplate().getForObject(url, resultClass);
    }

    @Override
    public String toString() {
        return "GET HTTP Task: [URL=" + url + "]";
    }
}
