package com.trecapps.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class WebSocketLoggingFilter implements GlobalFilter, Ordered {

   private static final Logger log = LoggerFactory.getLogger(WebSocketLoggingFilter.class);

   @Override
   public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
       String upgradeHeader = exchange.getRequest().getHeaders().getFirst("Upgrade");

       // Check if the connection is a WebSocket protocol upgrade request
       if ("websocket".equalsIgnoreCase(upgradeHeader)) {
           URI routedUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
           String path = exchange.getRequest().getPath().value();

           return chain.filter(exchange).then(Mono.fromRunnable(() -> {
               log.info("WebSocket handshake completed for Path: {} from {} with status: {}",
                       path, routedUri ,exchange.getResponse().getStatusCode());
           }));
       }

       return chain.filter(exchange);
   }

   @Override
   public int getOrder() {
       // Run early in the filter chain
       return Ordered.LOWEST_PRECEDENCE;
   }
}
