package org.dragberry.ozo.http;

import com.badlogic.gdx.Gdx;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * Created by Dragun_M on 3/2/2017.
 */
public abstract class HttpTask<P, R> {

    private static final String TAG = HttpTask.class.getName();

//    protected static final String URL_ROOT = "http://10.0.2.2:8087/ozo-backend-war";
    protected static final String URL_ROOT = "http://192.168.0.104:8080/ozo-backend-war";

    protected final String url;
    protected final Class<R> resultClass;

    public HttpTask(Class<R> resultClass, String urlTemplate, Object... urlParams) {
        this.url = MessageFormat.format(URL_ROOT + urlTemplate, urlParams);
        this.resultClass = resultClass;
    }

    public final R execute() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            return doRequest(restTemplate);
        } catch (Exception exc) {
            Gdx.app.error(TAG, toString() + " was completed with errors:", exc);
        }
        return null;
    }

    protected abstract R doRequest(RestTemplate restTemplate);

    public abstract void onComplete(R result);

    public void onFail() {
        // on fail
    }


}
