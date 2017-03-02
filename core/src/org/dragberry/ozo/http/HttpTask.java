package org.dragberry.ozo.http;

import org.dragberry.ozo.common.levelresult.AllLevelResults;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Created by Dragun_M on 3/2/2017.
 */
public abstract class HttpTask<P, R> {

    private final String url;
    private final Class<R> resultClass;

    private P parameter;

    public HttpTask(Class<R> resultClass, String urlTemplate, Object... urlParams) {
        this.url = MessageFormat.format(urlTemplate, urlParams);
        this.resultClass = resultClass;
    }

    public final R execute() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(url, resultClass);
    }

    public abstract void onComplete(R result);

    public P getParameter() {
        return parameter;
    }

    public void setParameter(P parameter) {
        this.parameter = parameter;
    }

    public Class<R> getResultClass() {
        return resultClass;
    }

    public String getUrl() {
        return url;
    }

}
