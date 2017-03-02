package org.dragberry.ozo.http;

/**
 * Created by maksim on 02.03.17.
 */

public class HttpClientAdapter implements HttpClient {
    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public <T, R> void executeTask(HttpTask<T, R> httpTask) {

    }
}
