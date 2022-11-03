package pl.szinton.querky.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Profile("!test")
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Value("${RABBITMQ_HOSTNAME}")
    private String rabbitHostname;

    @Value("${RABBITMQ_PORT}")
    private int rabbitPort;

    @Value("${RABBITMQ_USER}")
    private String rabbitUser;

    @Value("${RABBITMQ_PASSWORD}")
    private String rabbitPassword;

//    private final ConnectionListener connectionListener;

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
        registry.addEndpoint("/querky").withSockJS();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        String address = rabbitHostname + ":" + rabbitPort;
        connectionFactory.setAddresses(address);
        connectionFactory.setUsername(rabbitUser);
        connectionFactory.setPassword(rabbitPassword);
//        connectionFactory.setConnectionListeners(List.of(connectionListener));
        return connectionFactory;
    }
}
