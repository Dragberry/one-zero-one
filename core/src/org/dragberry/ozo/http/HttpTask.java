package org.dragberry.ozo.http;

import com.badlogic.gdx.Gdx;

import org.dragberry.ozo.game.DirectedGame;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * Created by Dragun_M on 3/2/2017.
 */
public abstract class HttpTask<P, R> {

    public static class Result<T> {
        public final T result;
        public final HttpStatus status;

        public Result(T result) {
            this(result, null);
        }

        public Result(HttpStatus status) {
            this(null, status);
        }

        public Result(T result, HttpStatus status) {
            this.status = status;
            this.result = result;
        }
    }

    private static final String TAG = HttpTask.class.getName();

    public static RestTemplate restTemplate;

    protected final String url;
    protected final Class<R> resultClass;

    public HttpTask(Class<R> resultClass, String urlTemplate, Object... urlParams) {
        this.url = MessageFormat.format(HttpClient.URL.ROOT + urlTemplate, urlParams);
        this.resultClass = resultClass;
    }



    public final Result<R> execute() {
        try {
            return new Result<R>(doRequest());
        } catch (HttpClientErrorException exc) {
            if (HttpStatus.GONE.equals(exc.getStatusCode())) {
                DirectedGame.game.wrongAppVersion = true;
                Gdx.app.error(TAG, "Application version is wrong!");
            } else {
                Gdx.app.error(TAG, toString() + " was completed with errors:", exc);
                return new Result<R>(exc.getStatusCode());
            }
        } catch (Exception exc) {
            Gdx.app.error(TAG, toString() + " was completed with errors:", exc);
        }
        return null;
    }

    protected abstract R doRequest();

    public abstract void onComplete(R result);

    public void onFail(HttpStatus status) {
        // on fail
    }

    protected static RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return restTemplate;
    }

}
