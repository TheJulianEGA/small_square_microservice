package small_square_microservice.small_square.infrastructure.configuration.feign;

import feign.Client;
import feign.Logger;
import feign.RequestInterceptor;
import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import small_square_microservice.small_square.infrastructure.http.feign.interceptor.JwtRequestInterceptor;

@Configuration
public class FeignConfiguration {

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new JwtRequestInterceptor();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}