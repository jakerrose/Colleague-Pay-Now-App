package pl.paynowapp.samples.security.callme.saml;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final ProxyProperties proxyProperties;

    @Autowired
    public RestTemplateConfig(ProxyProperties proxyProperties) {
        this.proxyProperties = proxyProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpClient httpClient;

        if (proxyProperties.isEnabled()) {
            HttpHost proxy = new HttpHost(proxyProperties.getHost(), proxyProperties.getPort());
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

            httpClient = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}



