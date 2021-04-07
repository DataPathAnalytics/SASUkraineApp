package com.datapath.sasu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class CookieInterceptor implements ClientHttpRequestInterceptor {

    private String cookie;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (cookie != null) {
            request.getHeaders().add(HttpHeaders.COOKIE, cookie);
        }
        ClientHttpResponse response = execution.execute(request, body);

        String lastCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (cookie == null) {
            cookie = lastCookie;
            log.info("initial cookie [{}]", cookie);
        } else if (lastCookie!= null && !cookie.equalsIgnoreCase(lastCookie)) {
            log.info("Cookie has been changed. Before [{}], after [{}]", cookie, lastCookie);
            cookie = lastCookie;
        }


        return response;
    }

}
