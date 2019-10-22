package main_classes;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;

/**
 * @author Raphael Melo
 * 
 * Class TextListenerThread : classe responsável por ouvir as mensagens que o
 *                            cliente está recebendo do servidor
 */
public class TextListenerThread extends Thread{
    //InputStream do cliente
    private InputStream input;
    //Área de texto da interface do usuário
    private JTextArea textArea;
    //Booleana que permite que a thread rode
    public boolean canGo = true;
    
    //Construtor da classe
    public TextListenerThread(InputStream input, JTextArea textArea){
        this.input = input;
        this.textArea = textArea;
    }

    /**
     * Método chamado na execução da thread, ele que ficará em loop ouvindo o
     * que o servidor mandou para o cliente
     */
    @Override
    public void run() {
        while (canGo) {
            try {
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);
                String text = br.readLine();    
                //Adiciona o texto recebido na tela do cliente
                textArea.append(text + "\n");
                
            } catch (IOException ex) {
                Logger.getLogger(TextListenerThread.class.getName());
            }
        }
    }
    
    /**
     * Método que encerra loop da thread, chamada pelo cliente quando ele tenta
     * se desconectar do servidor
     */
    public void stopThread(){
        this.canGo = false;
    }
}
