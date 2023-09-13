package com.epix.hawkadmin.config;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.elasticsearch.client.RestClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Configuration
public class ElasticsearchConfig {

    @Value("${spring.data.elasticsearch.username}")
    private String username;

    @Value("${spring.data.elasticsearch.password}")
    private String password;

    @Bean
    public RestClient restClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        SSLContextBuilder builder = SSLContextBuilder.create();
        builder.loadTrustMaterial(new TrustAllStrategy()); // trusting all certificates
        final SSLContext sslContext = builder.build();

        return RestClient.builder(new HttpHost("83.202.224.116", 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)) // disabling hostname verification
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}




/*@Configuration
public class ElasticsearchConfig {
  @Bean

    public ElasticsearchClient client() {
        try {
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true)
                    .build();

            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials("elastic", "H-AoZ9LfpWfVmM7*z3b-"));

            RestClient restClient = RestClient.builder(
                            new HttpHost("83.202.224.116", 9200, "https"))
                    .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                            httpAsyncClientBuilder.setSSLContext(sslContext)
                                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE))
                    .build();

            // Create the transport with a Jackson mapper
            ElasticsearchTransport transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper());

            // And create the API client
            return new ElasticsearchClient(transport);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to create ElasticsearchClient bean", e);
        }

    }
}*/

/*@Configuration
public class ElasticsearchConfig
{


    // Create the low-level client
    RestClient restClient = RestClient.builder(
            new HttpHost("83.202.224.116", 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);



    }*/



