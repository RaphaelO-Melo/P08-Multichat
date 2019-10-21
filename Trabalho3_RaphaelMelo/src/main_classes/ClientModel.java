package main_classes;

import java.net.Socket;

/**
 * @author Raphael Melo
 */
public class ClientModel {

    private String clientID;
    private Socket clientSocket;

    public ClientModel(String clientID, Socket clientSocket) {
        //Associa os valores recebidos com os atributos
        this.clientID = clientID;
        this.clientSocket = clientSocket;
    }

    public String getClientID() {
        return clientID;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
