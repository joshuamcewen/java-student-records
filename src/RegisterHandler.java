import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Handler to register a user.
 * @author Joshua McEwen (16012396)
 */
public class RegisterHandler implements HttpHandler {
    public void handle(HttpExchange exch) {
        try {
            StringBuilder response = new StringBuilder();
            int responseCode;

            Headers headers = exch.getResponseHeaders();
            headers.set("Content-Type", "text/html");

            // If accessed through a GET request, respond with a signup form.
            if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                responseCode = 200;
                response.append("<h1>Register</h1>");
                response.append("<p>Enter your details for registration.</p>");
                response.append("<form action=\"register\" method=\"post\">");
                response.append("<label>Username: </label><input type=\"text\" name=\"username\"><br>");
                response.append("<label>Password: </label><input type=\"password\" name=\"password\"><br>");
                response.append("<input type=\"submit\" value=\"Register\">");
                response.append("</form>");
                response.append("<p>If you already have an account, login <a href=\"login\">here</a>.</p>");

            // If accessed through a POST request, process POST data with account creation logic.
            } else if(exch.getRequestMethod().equalsIgnoreCase("POST")) {
                StringBuilder request = new StringBuilder(); // New string, can be altered
                BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                String output;

                while ((output = reader.readLine()) != null) {
                    request.append(output); // Append request line by line to StringBuilder.
                }

                // Retrieve POST data through the request by mapping key pair values for username and password.
                Map<String, String> params = Controller.getParameters(request.toString());
                String username = params.get("username");
                String password = params.get("password");

                // If the user is successfully created, add an appropriate message to the response.
                if (StudentDAO.insertUser(username, password)) {
                    responseCode = 200;
                    response.append("<h1>Account created</h1>");
                    response.append("Please visit the <a href=\"login\">Login</a> page.");
                } else {
                    responseCode = 400;
                    response.append("<h1>Username taken</h1>");
                    response.append("Please register with a different username. <a href=\"register\">Register</a> here.");
                }
            } else {
                responseCode = 400;
                response.append("Invalid GET/POST request");
            }

            // Return a response to the client with values set above.
            Controller.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}