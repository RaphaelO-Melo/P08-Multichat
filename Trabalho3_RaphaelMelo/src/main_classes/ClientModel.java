package main_classes;

//Imports
import java.net.Socket;

/**
 * @author Raphael Melo
 * 
 * Class ClientModel: Classe que guarda o socket do cliente associado ao nome
 */
public class ClientModel {
    //Nome/ID do cliente
    private String clientID;
    //Socket do cliente
    private Socket clientSocket;

    //Construtor da classe
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
