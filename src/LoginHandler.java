import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Handler to retrieve an API token.
 */
public class LoginHandler implements HttpHandler {
    public void handle(HttpExchange exch) {
        try {
            String response;
            int responseCode;

            Headers headers = exch.getResponseHeaders();
            headers.set("Content-Type", "text/html");

            if(exch.getRequestMethod().equalsIgnoreCase("GET")) {

                responseCode = 200;
                response = "<h1>Login</h1>" +
                        "<p>Enter your login details to retrieve your token.</p>" +
                        "<form action=\"login\" method=\"post\">" +
                        "<label>Username: </label><input type=\"text\" name=\"username\"><br>" +
                        "<label>Password: </label><input type=\"password\" name=\"password\"><br>" +
                        "<input type=\"submit\" value=\"Login\">" +
                        "</form>" +
                        "<p>If you don't have an account, register <a href=\"register\">here</a>.</p>";
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

                if (StudentDAO.checkLoginCredentials(username, password)) {
                    String token = StudentDAO.getApiKey(username);
                    responseCode = 200;
                    response = "<h1>" + username + "'s Account</h1>" +
                            "<p>Your access token is: " + token + "</p>" +
                            "<a href=\"/users?token=" + token + " \">View users</a>";
                } else {
                    responseCode = 400;
                    response = "<h1>Invalid Credentials</h1>" +
                            "<p>Please try again, <a href=\"login\">here</a>.</p>";
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