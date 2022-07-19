package graph_encryption.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class is the util class to send request to url.
 */
public class HttpUtils {

    /**
     * This method post request to given url with given params.
     *
     * @param url destination url
     * @param params given params
     * @param body body data
     * @param contentType content type of the data
     * @return response from the given url
     */
    public static String postRequest(String url, Map<String, String> params, String body, ContentType contentType) {
        try {
            PoolingHttpClientConnectionManager cManager = getConnectionManager();
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(cManager).build();

            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry param : params.entrySet()) {
                    uriBuilder.addParameter((String)param.getKey(), (String)param.getValue());
                }
            }

            HttpPost post = new HttpPost(uriBuilder.build());
            post.setConfig(createRequestConfig());
            post.addHeader("Authorization", "Bearer WINSgo6m4k3ATVxrour1J7VHS2Er5r5ec3EvAs0RpyudqOGPZgZXD_5oAryZqxqlBBwF0mZZG3V8Ds2ukQTpv8LFky8P6hXEcw9OB0nH6N5er2UyWOQg0QuvCoaoYXYx");

            if (StringUtils.isEmpty(body) == false) {
                if (contentType == null) {
                    post.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
                } else {
                    post.addHeader("Content-Type", contentType.toString());
                }
                HttpEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
                post.setEntity(entity);
            }

            try {
                String result = null;
                HttpResponse response = client.execute(post);
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println(response.toString());
                if (statusCode == 200) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
                    }

                    return result;
                } else {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
                    }
                    throw new RuntimeException();
                }
            } catch (ClientProtocolException ex) {
                throw new RuntimeException();
            } catch (IOException ex) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    /**
     * This method send get request to the given url.
     *
     * @param url destination url
     * @param params given params
     * @return response from the url
     */
    public static String getRequest(String url, Map<String, String> params) {
        try {
            PoolingHttpClientConnectionManager cManager = getConnectionManager();
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(cManager).build();

            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry param : params.entrySet()) {
                    uriBuilder.addParameter((String)param.getKey(), (String)param.getValue());
                }
            }

            HttpGet get = new HttpGet(uriBuilder.build());
            get.setConfig(createRequestConfig());
            get.addHeader("Authorization", "Bearer WINSgo6m4k3ATVxrour1J7VHS2Er5r5ec3EvAs0RpyudqOGPZgZXD_5oAryZqxqlBBwF0mZZG3V8Ds2ukQTpv8LFky8P6hXEcw9OB0nH6N5er2UyWOQg0QuvCoaoYXYx");

            String result = null;
            HttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
                return result;
            } else {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
                }
                return result;
            }
        } catch (ClientProtocolException ex) {
            throw new RuntimeException();
        } catch (IOException ex) {
            throw new RuntimeException();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    private static PoolingHttpClientConnectionManager getConnectionManager() {
        PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
        cManager.setValidateAfterInactivity(4000);
        cManager.setDefaultSocketConfig(
                SocketConfig.custom().setSoTimeout(4000).build());
        cManager.setValidateAfterInactivity(2000);
        return cManager;
    }

    private static RequestConfig createRequestConfig() {
        RequestConfig config = RequestConfig.custom().setSocketTimeout(4000)
                .setConnectTimeout(2000).setConnectionRequestTimeout(2000)
                .build();
        return config;
    }
}
