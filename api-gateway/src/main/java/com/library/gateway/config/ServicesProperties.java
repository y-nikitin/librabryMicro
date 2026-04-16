package com.library.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "services")
public class ServicesProperties {

    private ServiceEntry book = new ServiceEntry();

    private ServiceEntry borrowing = new ServiceEntry();

    private ServiceEntry notification = new ServiceEntry();

    @Getter
    @Setter
    public static class ServiceEntry {
        private String baseUrl;
    }
}
