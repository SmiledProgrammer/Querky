package pl.szinton.querky.websocket;

import pl.szinton.querky.message.EventMessage;

import java.security.Principal;

public interface IOutputController {

    void broadcastTableMessage(int tableNumber, EventMessage broadcastMsg);
    void sendDirectMessage(Principal principal, EventMessage directMsg);
}
