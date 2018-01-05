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

public class Controller {
    /**
     * Entry point for starting the server. Creates the HttpServer object on port 8005 and sets routes/handlers that
     * execute when routes are accessed.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8005), 0);

            // Route handling
            server.createContext("/students", new GetStudentsHandler());
            server.createContext("/student/create", new SetStudentHandler());
            server.createContext("/student/update", new UpdateStudentHandler());
            server.createContext("/student", new GetStudentHandler());
            server.createContext("/student/delete", new DeleteStudentHandler());
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
     * Sets the headers for the response and writes the body to displayed to users when routes are accessed.
     * @param exch HttpExchange object to modify and send response
     * @param status A HTTP response code based on result of request
     * @param response StringBuilder object containing the response
     * @throws IOException When response is invalid
     */
    public static void writeResponse(HttpExchange exch, int status, StringBuilder response) throws IOException {
        exch.sendResponseHeaders(status, response.toString().getBytes().length);
        OutputStream os = exch.getResponseBody();
        os.write(response.toString().getBytes());
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

