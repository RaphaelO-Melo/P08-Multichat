/******************************************************************************
 *                     Trabalho de Redes III - Multichat                      *
 *                        Centro Universitário SENAC                          *
 *                                                                            *
 *                  Técnologia em Jogos Digitais IV - 2019                    *
 ******************************************************************************/
package main_classes;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Raphael Melo
 */
public class Client extends javax.swing.JFrame{
    //Variável que irá guardar o nome de usuário do cliente
    private String clientUserID;
    
    //Variáveis da tela do cliente
    private JButton         sendButton;
    private JButton         closeButton;
    private JScrollPane     jScrollPane1;
    private JTextArea       jTextArea;
    private JTextField      jTextField;
    private BorderLayout    layout; 
    
    //Variável que e refere ao spcket do cliente
    private Socket clientSocket;
    private OutputStream output;
    
    //Construtor da estrutura do cliente
    public Client(){
        
        //Cria todos os elementos da tela
        jTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
	    
        //Configura área de texto do chat
        jTextArea.setColumns(20);
        jTextArea.setEditable(false);
        jTextArea.setRows(5);
        
        //Seta o painel de scroll para atuar na área de texto do chat
        jScrollPane1.setViewportView(jTextArea);

        //Configura o botão de enviar menssagem e atirbui um listenner para ele
        sendButton.setText("Enviar Mensagem");
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //Chama método para tratar mensagem para ser enviada
                sendMessage(jTextField.getText());
            }
        });

        //Configura o botão de fechar chat e atirbui um listenner para ele
        closeButton.setText("Desconectar da sala");
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //Chama método que fecha sala
            }
        });
			
        //Configura layout da janela
	layout = new BorderLayout( 5, 5 ); 
        getContentPane().setLayout(layout);
	add( jScrollPane1, BorderLayout.CENTER ); 
	add( jTextField, BorderLayout.SOUTH ); 
	add( sendButton, BorderLayout.EAST ); 
	add( closeButton, BorderLayout.NORTH ); 		
     }

    /**
     * Método que envia mensagem para o servidor
     * 
     * @param message : mensagem a ser enviada 
     */
    private void sendMessage(String message){
        
        PrintWriter pw = new PrintWriter(output, true);
        pw.println(message);
        
    }
    /**
     * Método que pergunta o nome de usuário ao cliente e o asssocia ao cliente
     * atual através da String de nome do cliente
     */
    private void askUserID() {
        String collectedID = JOptionPane.showInputDialog("Informe o seu nome:");
        this.clientUserID = collectedID;
    }
    
    /**
     * Método que configura o tamanho e a visibilidade da tela do cliente
     */
    private void configScreen() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setTitle(this.clientUserID);
        this.setVisible(true);
    }
    
    private void connectServer(){
        try {
            //Cria socket
            clientSocket = new Socket("localhost", 5555);
            //Obtem o output do cliente
            output = clientSocket.getOutputStream();
            //Manda primeira mensagem que é o nome do cliente
            PrintWriter pw = new PrintWriter(output, true);
            pw.println(this.clientUserID);
            TextListenerThread tL = new TextListenerThread(clientSocket.getInputStream(), this.jTextArea);
            tL.start();
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
    
    /**
     * MainClass chamada no início da execução do programa
     * @param args
     */
    public static void main(String[] args) {
        
        //Inicia a interface de um cliente
        Client client = new Client();
        //Pergunta ao cliente o seu nome de usuário
        client.askUserID();
        //Chama o método que configura a tela do cliente
        client.configScreen();
        //Chama método que cria socket e conecta com servidor
        client.connectServer();
        //Cria thread que escuta e modifica a caixa de texto dos usuários
        
    }
}
