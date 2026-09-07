package com.trecapps.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    if(exchange.getResponse().getStatusCode().equals(HttpStatusCode.valueOf(404))){
                        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
                        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
                        log.info("404 from routed id: " + route.getId()
                                + ", uri:" + routeUri);
                    }
                }));
    }
}
