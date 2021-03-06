package ru.codeninja.proxyapp.header;

import ru.codeninja.proxyapp.connection.ProxyConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created: 23.01.15 12:01
 *
 * @author Vitaliy Mayorov
 */
public class ResponseHeadersManager {
    static final String CSP_POLICY = "default-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data:;";
    static String[] acceptedHeaders = {"content-type", "expires", "last-modified"};
    static final String SET_COOKIE_HTTP_HEADER = "Set-Cookie";

    public void sendHeaders(HttpServletResponse response, ProxyConnection headerSource) throws IOException {
        response.setStatus(headerSource.conn.getResponseCode());

        for (String headerName : acceptedHeaders) {
            String value = headerSource.conn.getHeaderField(headerName);
            if (value != null) {
                response.setHeader(headerName, value);
            }
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Content-Security-Policy", CSP_POLICY);
        response.setHeader("X-Content-Security-Policy", CSP_POLICY);

        String redirectLocation = headerSource.conn.getHeaderField("location");
        if (redirectLocation != null) {
            Redirect.location(headerSource.getCurrentUrl(), response)
                    .to(redirectLocation);
        }

        if (headerSource.isCookiesOn) {
            receiveCookies(response, headerSource);
        }
    }

    private void receiveCookies(HttpServletResponse response, ProxyConnection cookiesSource) {
        Map<String, List<String>> proxyResponseHeaders = cookiesSource.conn.getHeaderFields();
        List<String> cookies = proxyResponseHeaders.get(SET_COOKIE_HTTP_HEADER.toLowerCase());
        if (cookies != null) {
            for (String cookie : cookies) {
                response.addHeader(SET_COOKIE_HTTP_HEADER, Cookies.neutralize(cookiesSource.getCurrentUrl(), cookie));
            }
        }

    }
}
