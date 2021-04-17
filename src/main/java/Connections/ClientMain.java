package Connections;

import Models.Message;

import java.io.IOException;
import java.util.ArrayList;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1", 9876, "ghhwer");
            client.sendNewMessage("Mensagem de teste");
            client.sendNewMessage("Mensagem de teste 2");
            ArrayList<Message> msgs = client.getMessages();
            for (Message msg:msgs) {
                System.out.println("Message From: "+msg.getUsername());
                System.out.println(msg.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
