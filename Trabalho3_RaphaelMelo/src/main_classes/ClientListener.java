package main_classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.logging.Logger;
import java.io.InputStreamReader;

/**
 * @author Raphael Melo
 * 
 * Class ClientListener : classe responsável por ser a thread que irá escutar 
 *                        as mensagens que um cliente quer enviar para os outros
 */
public class ClientListener extends Thread {
    //Input que quando recebido será o do cliente
    private InputStream input;
    //Modelo criado do cliente
    private ClientModel clientModel;
    //Booleana que mantém a thread rodando
    public boolean canGo = true;

    //Construtor da classe
    public ClientListener(ClientModel clientModel) {
        try {
            this.clientModel = clientModel;
            this.input = clientModel.getClientSocket().getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(ClientListener.class.getName());
        }
    }

    /**
     * Método chamado na execução da thread, ele que ficará em loop ouvindo o
     * que o cliente quer mandar
     */
    @Override
    public void run() {
        while (canGo) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(reader);
            try {
                String msg = br.readLine();
                if(msg == null){
                    canGo = false;
                    Server.sendMessageToAll(clientModel.getClientID(),
                                            " saiu do chat");
                    return;
                }
                if (!" ".equals(msg)){
                    System.out.println("Mensagem recebida pela thread: " + msg);
                    Server.sendMessageToAll(clientModel.getClientID(), msg);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ClientListener.class.getName());
            }
        }
    }
}
