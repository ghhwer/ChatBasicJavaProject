package Connections;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static Connections.Utils.*;

public class Server {
    ArrayList<JSONObject> Users = new ArrayList<JSONObject>();
    ArrayList<JSONObject> Messages = new ArrayList<JSONObject>();

    //static ServerSocket variable
    private ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;

    public JSONObject newUser(JSONObject request){
        JSONObject response = new JSONObject();
        for(JSONObject user: Users){
            if(user.get("username") == request.get("username")) {
                response.put("message", "User already exists");
                response.put("status", false);
                return response;
            }
        }
        JSONObject newUser = new JSONObject();
        String token = randomToken();
        newUser.put("username", request.get("username"));
        newUser.put("token", token);
        response.put("token", token);
        response.put("status", true);
        Users.add(newUser);
        System.out.println("New user logged in: "+request.get("username"));
        return response;
    }
    public Boolean autenticate(JSONObject request){
        if(!request.has("token"))
            return false;
        for(JSONObject user: Users)
            if(user.get("token").equals(request.get("token")))
                return true;
        return false;
    }
    public String getUserFromToken(String token){
        for(JSONObject user: Users)
            if(user.get("token").equals(token))
                return (String) user.get("username");
        return "";
    }
    public JSONObject getMessages(JSONObject request){
        JSONObject response = new JSONObject();
        response.put("status", true);

        JSONArray messages = new JSONArray();
        for (JSONObject message: Messages) {
            messages.put(message);

        }

        response.put("messages", messages);
        return response;
    }
    public boolean newMessage(JSONObject request){
        JSONObject msg = new JSONObject();
        msg.put(
                "username",
                getUserFromToken(
                        (String) request.get("token")
                )
        );
        msg.put(
                "message",
                request.get("message")
        );
        Messages.add(msg);
        return true;
    }

    public JSONObject processCommand(JSONObject request){
        String command = (String) request.get("command");
        Boolean isAuth = autenticate(request);
        JSONObject response = new JSONObject();
        if(command.contains("authMe")){
            response = newUser(request);
        }
        else if(command.contains("newMessage") && isAuth){
            Boolean status = newMessage(request);
            response.put("status", status);
        }
        else if(command.contains("getMessages") && isAuth){
            response = getMessages(request);
        }
        else{
            response.put("status", false);
            response.put("message", "invalid command");
        }
        return response;
    }

    public void sendResponse(JSONObject response, ObjectOutputStream oos) throws IOException {
        String encodedResponse = jsonToB64String(response);
        oos.writeObject(encodedResponse);
    }
    public JSONObject getRequest(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String message = (String) ois.readObject();
        JSONObject request = parseB64Json(message);
        System.out.println("Received Request: " + request);
        return  request;
    }

    public Server() throws IOException, ClassNotFoundException, IOException {
        server = new ServerSocket(port);
        while(true){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // Pega dados de entrada
            JSONObject request = getRequest(ois);
            // Responde de acordo
            JSONObject response = processCommand(request);
            sendResponse(response, oos);

            ois.close();
            oos.close();
            socket.close();

            if(request.get("command").equals("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }

}
