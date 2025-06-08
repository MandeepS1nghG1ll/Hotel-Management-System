package com.mandeep.api_gateway;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;
    
 // added this for rolebased
    private final Map<String, List<String>> endpointRoleMapping = Map.of(
    	    "/guests", List.of("ROLE_USER", "ROLE_ADMIN"),
    	    "/res", List.of("ROLE_ADMIN"),
    	    "/res/createReservation", List.of("ROLE_USER"),
    	    "/billing", List.of("ROLE_ADMIN")
    	);

//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//        	ServerHttpRequest request = null;
//            if (validator.isSecured.test(exchange.getRequest())) {
//                //header contains token or not
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing authorization header");
//                }
//
//                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    authHeader = authHeader.substring(7);
//                }
//                try {
////                    //REST call to AUTH service
////                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
//                    jwtUtil.validateToken(authHeader);
//                    
//                     request = exchange.getRequest()
//                    .mutate()
//                    .header("loggedInUser", jwtUtil.extractUsername(authHeader))
//                    .build();
//
//                } catch (Exception e) {
//                    System.out.println("invalid access...!");
//                    throw new RuntimeException("un authorized access to application");
//                }
//            }
//            return chain.filter(exchange.mutate().request(request).build());
//        });
//    }
//
//    public static class Config {
//
//    }
    
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                // 1. Check if Authorization header is present
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                String token = null;

                // 2. Extract token
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                } else {
                    throw new RuntimeException("Invalid authorization header format");
                }

                try {
                    // 3. Validate token
                    jwtUtil.validateToken(token);

                    // 4. Extract username and roles
                    String username = jwtUtil.extractUsername(token);
                    List<String> roles = jwtUtil.extractRoles(token);

                    // 5. Determine the target path
                    String path = request.getURI().getPath();
                    Optional<String> matchedPath = endpointRoleMapping.keySet().stream()
                            .filter(path::startsWith)
                            .findFirst();

                    if (matchedPath.isPresent()) {
                        List<String> allowedRoles = endpointRoleMapping.get(matchedPath.get());
                        boolean hasAccess = roles.stream().anyMatch(allowedRoles::contains);

                        if (!hasAccess) {
                            throw new RuntimeException("Forbidden: Insufficient permissions");
                        }
                    }

                    // 6. Mutate request to add username (optional)
                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", username)
                            .build();

                } catch (Exception e) {
                    System.out.println("Unauthorized access: " + e.getMessage());
                    throw new RuntimeException("Unauthorized access to application");
                }
            }

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {}
}   
    
    

