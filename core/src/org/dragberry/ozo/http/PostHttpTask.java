package org.dragberry.ozo.http;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Dragun_M on 3/3/2017.
 */

public abstract class PostHttpTask<P, R> extends HttpTask<P, R> {

    private static final String TAG = PostHttpTask.class.getName();

    private final P parameter;

    public PostHttpTask(P parameter, Class<R> resultClass, String urlTemplate, Object... urlParams) {
        super(resultClass, urlTemplate, urlParams);
        this.parameter = parameter;
    }

    @Override
    protected R doRequest() {
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug(TAG, "do POST request: " + url);
        }
        return getRestTemplate().postForObject(url, parameter, resultClass);
    }

    @Override
    public String toString() {
        return "POST HTTP Task: [URL=" + url + "][parameter=" + parameter + "]";
    }
}
