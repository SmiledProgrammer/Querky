package pl.szinton.querky.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Profile("!test")
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Value("${RABBITMQ_HOSTNAME}")
    private String rabbitHostname;

    @Value("${RABBITMQ_PORT}")
    private int rabbitPort;

    @Value("${RABBITMQ_USER}")
    private String rabbitUser;

    @Value("${RABBITMQ_PASSWORD}")
    private String rabbitPassword;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .setApplicationDestinationPrefixes("/app")
                .setUserDestinationPrefix("/user")
                .enableStompBrokerRelay("/topic", "/queue", "/user")
                .setRelayHost(rabbitHostname)
                .setRelayPort(rabbitPort)
                .setSystemLogin(rabbitUser)
                .setSystemPasscode(rabbitPassword)
                .setClientLogin(rabbitUser)
                .setClientPasscode(rabbitPassword);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
