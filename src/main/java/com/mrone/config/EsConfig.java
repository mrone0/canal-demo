package com.mrone.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Mr.One
 * @program canal-demo
 * @description
 * @create 2024-06-20 21:07
 **/
@Configuration
public class EsConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() throws IOException {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost",9200,"http")));
    }
}
