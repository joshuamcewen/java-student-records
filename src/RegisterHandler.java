import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Handler to retrieve an API token.
 */
public class RegisterHandler implements HttpHandler {
    public void handle(HttpExchange exch) {
        try {
            String response;
            int responseCode;

            Headers headers = exch.getResponseHeaders();
            headers.set("Content-Type", "text/html");

            if(exch.getRequestMethod().equalsIgnoreCase("GET")) {

                responseCode = 200;
                response = "<h1>Register</h1>" +
                        "<p>Enter your details for registration.</p>" +
                        "<form action=\"register\" method=\"post\">" +
                        "<label>Username: </label><input type=\"text\" name=\"username\"><br>" +
                        "<label>Password: </label><input type=\"password\" name=\"password\"><br>" +
                        "<input type=\"submit\" value=\"Register\">" +
                        "</form>" +
                        "<p>If you already have an account, login <a href=\"login\">here</a>.</p>";

            } else if(exch.getRequestMethod().equalsIgnoreCase("POST")) {
                StringBuilder request = new StringBuilder(); // New string, can be altered
                BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                String output;

                while ((output = reader.readLine()) != null) {
                    request.append(output); // Append request line by line to StringBuilder.
                }

                Map<String, String> params = Server.getParameters(request.toString());
                String username = params.get("username");
                String password = params.get("password");

                if (StudentDAO.insertUser(username, password)) {
                    responseCode = 200;
                    response = "<h1>Account created</h1>" +
                            "Please visit the <a href=\"login\">Login</a> page.";
                } else {
                    responseCode = 400;
                    response = "<h1>Username taken</h1>" +
                            "Please register with a different username. <a href=\"register\">Register</a> here.";
                }
            } else {
                responseCode = 400;
                response = "Invalid GET/POST request";
            }

            Server.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}