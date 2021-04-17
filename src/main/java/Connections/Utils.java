package Connections;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Base64;
import java.util.Random;

public class Utils {

    public static JSONObject parseB64Json(String message){
        // Decode Base 64
        message = new String(
                Base64.getDecoder().decode(message)
        );
        // Decode JSON
        JSONTokener tokener = new JSONTokener(message);
        JSONObject root = new JSONObject(tokener);
        return root;
    }

    public static String jsonToB64String(JSONObject obj){
        String json_string = obj.toString();
        String b64 = Base64.getEncoder().encodeToString(json_string.getBytes());
        return b64;
    }

    public static String randomToken() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 7;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();
        return randomString;
    }
}
