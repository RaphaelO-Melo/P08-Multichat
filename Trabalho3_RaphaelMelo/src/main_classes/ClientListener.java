package main_classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * @author Raphael Melo
 */
public class ClientListener extends Thread {

    //Input que quando recebido será o do cliente
    private InputStream input;
    private ClientModel clientModel;
    
    //Booleana que mantém a thread rodando
    public boolean canGo = true;

    public ClientListener(ClientModel clientModel) {
        try {
            this.clientModel = clientModel;
            this.input = clientModel.getClientSocket().getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(ClientListener.class.getName());
        }
    }

    @Override
    public void run() {
        while (canGo) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(reader);
            try {
                String message = br.readLine();
                if(message == null){
                    canGo = false;
                    Server.sendMessageToAll(clientModel.getClientID() + " saiu do chat");
                    return;
                }
                if (!" ".equals(message)){
                    System.out.println("Mensagem recebida pela thread: " + message);
                    Server.sendMessageToAll(clientModel.getClientID() + ": " +message);
                }
                
                
            } catch (IOException ex) {
                Logger.getLogger(ClientListener.class.getName());
            }
        }
    }
    
    public void stopThread(){
        this.canGo = false;
    }
}
