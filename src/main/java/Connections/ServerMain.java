package Connections;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Server server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
