package com.trecapps.gateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "trecapps.routes")
@Getter
@Setter
public class GatewayMap {
    private Map<String, String> backend;
    private Map<String, String> frontends;
}
