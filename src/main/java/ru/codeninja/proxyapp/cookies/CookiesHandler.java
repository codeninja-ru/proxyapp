package ru.codeninja.proxyapp.cookies;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;

/**
 * Created by vital on 14.02.15.
 */
public interface CookiesHandler {
    String COOKIES_ON_PARAM = "__cookies";

    void sendCookies(HttpServletRequest request, HttpURLConnection connection);
    void receiveCookies(HttpServletResponse response, HttpURLConnection cookiesSource);
}
