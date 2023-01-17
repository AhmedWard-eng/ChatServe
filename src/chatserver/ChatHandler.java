/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AhmedWard
 */
public class ChatHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    Socket socket;
    static Vector<ChatHandler> clientsVector = new Vector<>();
    

    public ChatHandler(Socket s) {
        socket = s;
        try {
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            ChatHandler.clientsVector.add(this);
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String str = dis.readLine();
                sendMessageToAll(str);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMessageToAll(String msg) {
        for (ChatHandler ch : clientsVector) {
            if(ch.socket.isConnected()){
                ch.ps.println(msg);
            }else{  
                clientsVector.remove(ch);
            }
           
        }
    }

}
