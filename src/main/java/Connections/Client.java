package Connections;

import Models.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;

import static Connections.Utils.jsonToB64String;
import static Connections.Utils.parseB64Json;

public class Client {
    private Socket socket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    InetAddress host;
    int port;

    String username;
    String token;

    public Client(String Host, int port, String username) throws IOException, ClassNotFoundException{
        this.host = InetAddress.getByName(Host);
        this.username = username;
        this.port= port;

        JSONObject authMeRequest = new JSONObject();
        authMeRequest.put("command", "authMe");
        authMeRequest.put("username", this.username);

        JSONObject response = sendAndWaitResponse(authMeRequest);

        if((boolean) response.get("status")){
            this.token = (String) response.get("token");
            System.out.println("Client logged in with token: "+this.token);
        }
    }

    private JSONObject sendAndWaitResponse(JSONObject request) throws IOException, ClassNotFoundException {
        socket = new Socket(this.host.getHostName(), port);
        oos = new ObjectOutputStream(socket.getOutputStream());

        String encodedMessage = jsonToB64String(request);
        oos.writeObject(encodedMessage);

        ois = new ObjectInputStream(socket.getInputStream());
        String encodedResponse = (String) ois.readObject();
        JSONObject response = parseB64Json(encodedResponse);

        ois.close();
        oos.close();

        return response;
    }
    public boolean sendNewMessage(String message) throws IOException, ClassNotFoundException {
        JSONObject authMeRequest = new JSONObject();
        authMeRequest.put("command", "newMessage");
        authMeRequest.put("token", this.token);
        authMeRequest.put("message", message);

        JSONObject response = sendAndWaitResponse(authMeRequest);
        return response.getBoolean("status");
    }
    public ArrayList<Message> getMessages() throws IOException, ClassNotFoundException {
        JSONObject authMeRequest = new JSONObject();
        authMeRequest.put("command", "getMessages");
        authMeRequest.put("token", this.token);
        JSONObject response = sendAndWaitResponse(authMeRequest);
        ArrayList<Message> messages = new ArrayList<Message>();


        if(response.getBoolean("status")){
            JSONArray responseMessages = response.getJSONArray("messages");
            for (int i = 0; i < responseMessages.length(); i++) {
                JSONObject msgJson = responseMessages.getJSONObject(i);
                messages.add(
                        new Message(
                                msgJson.getString("username"),
                                msgJson.getString("message")
                        )
                );
            }
        }
        System.out.println(response);
        return messages;
    }
}
