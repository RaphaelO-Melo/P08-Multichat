package main_classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Raphael Melo
 */
public class Server {
    //Socket do servidor
    private ServerSocket serverSocket;
    private static ArrayList<ClientModel> clients;
    
    //Constutor da classe
    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
    
    //Método que inicializa e bota servidor para receber e tratar clientes
    private void startServer() {
        while (true) {
            System.out.println("Esperando clientes se conectarem...");
            try {
                //Aceita o cliente
                Socket client = serverSocket.accept();
                //Obtem inputStream dele
                InputStream input = client.getInputStream();
                //Lê a primeira mensagem que é o nome do cliente
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);
                String clientID = br.readLine();
                //Cria o modelo do cliente em que o servidor guardará o nome 
                //associado com o socket
                ClientModel clientModel = new ClientModel(clientID, client);
                //Adiciona o cliente na lista de modelos de clientes
                clients.add(clientModel);
                //Inicia thread que vai cuidar de escutar o cliente
                ClientListener threadClient = new ClientListener(clientModel);
                threadClient.start();
                
                //Sinaliza que cliente foi conectado
                System.out.println(clientID + " conectado");
                
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName());
            }
        }
    }
    
    public static void main(String[] args) {
        //Cria servidor
        Server server = new Server(5555);
        //Inicializa o mesmo
        server.startServer();
    }
    
    public static void sendMessageToAll(String message){
        System.out.println("Mensagem que será passada para todo mundo: " + message);
        
        for(ClientModel c : clients){
            try {
                PrintWriter pw = new PrintWriter(c.getClientSocket().getOutputStream(), true);
                pw.println(message);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
}
