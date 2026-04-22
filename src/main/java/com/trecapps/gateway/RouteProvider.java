package com.trecapps.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class RouteProvider {


    @Autowired
    GatewayMap gatewayMap;

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Bean
    public RouteLocator tcTestRoutes(RouteLocatorBuilder builder){
        AtomicReference<RouteLocatorBuilder.Builder> ret = new AtomicReference<>(builder.routes());

        gatewayMap.getBackend().forEach((String route, String target) -> {
            ret.set(ret.get().route((PredicateSpec ps) ->
                    ps.path(String.format("/%s/**",route))
                            .filters((GatewayFilterSpec filter) ->
                                    filter.stripPrefix(1)
                                            .removeResponseHeader("WWW-Authenticate"))
                            .uri(target)));
        });

        Map<String, String> frontends = gatewayMap.getFrontends();

        if (frontends != null){
            frontends.forEach((String route, String target) -> {

                String uPath = capitalize(route);

                List<String> paths = new ArrayList<>();

                String[] pieces = target.split("\\|", 2);

                for(String subPath: pieces[0].split(";")){
                    paths.add(String.format("/%s/%s", route, subPath).replace("//", "/"));
                    paths.add(String.format("/%s/%s", uPath, subPath).replace("//", "/"));
                }

                ret.set(
                        ret.get().route((PredicateSpec ps) ->
                                ps.path(String.format("/%s/**", route))
                                        .filters((GatewayFilterSpec filter) -> {

                                            for(String prefix: paths){
                                                filter = filter.rewritePath(prefix, String.format("/%s", route));
                                            }

                                            return filter.stripPrefix(1);
                                        })
                                        .uri(pieces[1]))
                );

                ret.set(
                        ret.get().route((PredicateSpec ps) ->
                                ps.path(String.format("/%s/**", uPath))
                                        .filters((GatewayFilterSpec filter) -> {

                                            for(String prefix: paths){
                                                filter = filter.rewritePath(prefix, String.format("/%s", route));
                                            }

                                            return filter.stripPrefix(1);
                                        })
                                        .uri(pieces[1]))
                );

            });
        }

        return ret.get().build();
    }
}
