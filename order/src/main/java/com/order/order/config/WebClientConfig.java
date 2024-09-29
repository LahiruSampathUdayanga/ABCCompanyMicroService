package com.order.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
    @Bean
    public WebClient inventryWebClient(){
        return webClientBuilder().baseUrl("http://inventory/api/v1").build();
    }
    @Bean
    public WebClient productWebClient(){
        return webClientBuilder().baseUrl("http://product/api/v1").build();
    }


//    before eureka server

//    @Bean
//    public WebClient webClient(){
//        return WebClient.builder().build();
//    }
//    @Bean
//    public WebClient inventryWebClient(){
//        return WebClient.builder().baseUrl("http://localhost:60614/api/v1").build();
//    }
//    @Bean
//    public WebClient productWebClient(){
//        return WebClient.builder().baseUrl("http://localhost:60649/api/v1").build();
//    }

}
