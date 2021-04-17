package Connections;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1", 9876, "ghhwer");
            client.sendNewMessage("Mensagem de teste");
            client.sendNewMessage("Mensagem de teste 2");
            client.getMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
