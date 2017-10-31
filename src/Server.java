import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a HTTP Server and routes with corresponding handlers for CRUD operations on the database.
 * @author Joshua McEwen (16012396)
 */

public class Server {
    /**
     * Entry point for starting the server. Creates the HttpServer object on port 8005 and sets routes/handlers that
     * execute when routes are accessed.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8005), 0);

            // Route handling
            server.createContext("/users", new GetUsersHandler());
            server.createContext("/user/new", new SetUserHandler());
            server.createContext("/user/update", new UpdateUserHandler());
            server.createContext("/user", new GetUserHandler());
            server.createContext("/user/delete", new DeleteUserHandler());
            server.createContext("/login", new LoginHandler());
            server.createContext("/register", new RegisterHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port 8005.");
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks the API token supplied as a URL parameter and returns a boolean based on the outcome of that check.
     * @param exch HttpExchange object to access request details
     * @return true/false depending on whether the token exists or not.
     */
    public static boolean isValidToken(HttpExchange exch) {
        String queryString = exch.getRequestURI().getQuery();

        if(queryString != null) {
            Map<String, String> params = getParameters(queryString);
            String apiKey = params.get("token");

            if(apiKey != null && StudentDAO.checkApiKey(apiKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the headers for the response and writes the body to displayed to users when routes are accessed.
     * @param exch HttpExchange object to modify and send response
     * @param status A HTTP response code based on result of request
     * @param response Textual response displayed to the user
     * @throws IOException When response is invalid
     */
    public static void writeResponse(HttpExchange exch, int status, String response) throws IOException {
        exch.sendResponseHeaders(status, response.getBytes().length);
        OutputStream os = exch.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Sets the headers for the response and writes the body to displayed to users when routes are accessed.
     * @param queryString Query string from URL, gained from HttpExchange object.
     * @return A map of the URL parameters (name and values)
     */
    public static Map<String, String> getParameters(String queryString) {
        Map<String, String> params = new HashMap<>();

        for(String param : queryString.split("&")) { // Split the keys and their values
            String pair[] = param.split("="); // Separate the key and value

            if(pair.length > 1) { // If a key and a value exists, add the pair to the map.
                params.put(pair[0], pair[1]);
            } else {
                params.put(pair[0], "Undefined"); // If only a key exists, set the value as undefined.
            }
        }

        return params; // Return the map for access
    }

}

