package main_classes;

//Imports
import java.net.Socket;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;

/**
 * @author Raphael Melo
 * 
 * Class Server: Nesta classe é criado o servidor que vai ser o responsável
 *               por gerenciar a comunicação entre todos os clientes
 */
public class Server {
    //Socket do servidor
    private ServerSocket serverSocket;
    //Arraylist de cliente
    public static ArrayList<ClientModel> clients;
   
    //Constutor da classe
    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

    /**
     * Método que inicializa, cria os modelos de cliente e bota servidor para 
     * receber e tratar clientes 
     */
    private void startServer() {
        //Servidor após iniciado sempre espera clientes
        while (true) {
            System.out.println("Esperando clientes se conectarem...");
            try {
                //Limita o chat à 15 usuários
                if (clients != null && clients.size() < 15) {
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
                    ClientModel cModel = new ClientModel(clientID, client);
                    //Adiciona o cliente na lista de modelos de clientes
                    clients.add(cModel);
                    //Inicia thread que vai cuidar de escutar o cliente
                    ClientListener threadClient = new ClientListener(cModel);
                    threadClient.start();
                    //Sinaliza que cliente foi conectado
                    sendMessageToAll(cModel.getClientID(), " conectado...");
                }else{
                    System.out.println("Servidor cheio");
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName());
            }
        }
    }
    
    /**
     * Método principal chamado na inicialização do servidor
     * 
     * @param args 
     */
    public static void main(String[] args) {
        //Cria servidor
        Server server = new Server(5555);
        //Inicializa o mesmo
        server.startServer();
    }
    
    /**
     * Método que manda uma mensagem para todos os clientes
     *
     * @param message : mensagem passada
     */
    public static void sendMessageToAll(String clientID, String msg) {
        //Se começar com @ quer mandar mensagem privada
        if (msg.charAt(0) == '@') {
            sendPrivateMessage(clientID, msg);
        } else {
            //Se não, manda a mensagem para todo mundo
            System.out.println(clientID + " mandando a mensagem: " + msg);
            for (ClientModel c : clients) {
                try {
                    PrintWriter pw = new PrintWriter(c.getClientSocket()
                                                      .getOutputStream(), true);
                    pw.println(clientID + ": " + msg);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName());
                }
            }
        }
    }
    
    /**
     * Método que manda uma mensagem privada para um usuário específico 
     * 
     * @param senderUser : Usuário remetente
     * @param msg : mensagem que vai ser passada
     */
    public static void sendPrivateMessage(String senderUser, String msg) {
        //Divide a marcação da mensagem privada
        String[] firstArray = msg.split("@");
        //Divide o nome do ussuário e a mensagem
        String[] secondArray = firstArray[1].split(" ");
        //Guarda o nome do usuário remetente
        String userName = secondArray[0];
        //Em um loop reagrupa a mensagem que foi separada
        String message = "";
        for (int i = 0; i < secondArray.length; i++) {
            message = message + " " + secondArray[i];
        }

        //Anda pela lista de clientes e se acha o usuário destinado, manda a
        //mensagem para ele e para o próprio remetente
        for (ClientModel c : clients) {
            if (c.getClientID().equals(userName) 
                  || senderUser.equals(c.getClientID())) {                
                try {
                    PrintWriter pw = new PrintWriter(c.getClientSocket()
                                                      .getOutputStream(), true);
                    pw.println(senderUser + ": " + msg);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName());
                }
            }
        }
    }
}
