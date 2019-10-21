package main_classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 * @author Raphael Melo
 */
public class TextListenerThread extends Thread{
    private InputStream input;
    private JTextArea textArea;
    public boolean canGo = true;
    
    public TextListenerThread(InputStream input, JTextArea textArea){
        this.input = input;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        while (canGo) {
            try {
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);
                String text = br.readLine();
                textArea.append(text + "\n");
                
            } catch (IOException ex) {
                Logger.getLogger(TextListenerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void stopThread(){
        this.canGo = false;
    }
}
