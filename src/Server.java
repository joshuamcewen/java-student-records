import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8005), 0);
            server.createContext("/users", new getUsersHandler());
            server.setExecutor(null);
            server.start();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static class getUsersHandler implements HttpHandler {
        public void handle(HttpExchange t) {
            try {
                byte[] response = new Gson().toJson(StudentDAO.getAllStudents()).getBytes();

                Headers headers = t.getResponseHeaders();
                headers.set("Content-Type", "application/json");

                t.sendResponseHeaders(200, response.length);
                OutputStream os = t.getResponseBody();
                os.write(response);
                os.close();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

