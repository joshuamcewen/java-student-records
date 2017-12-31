import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Handler to handle login requests.
 * @author Joshua McEwen (16012396)
 */
public class LoginHandler implements HttpHandler {
    /**
     * Returns HTML form for login requests. Handles login logic and if successful,
     * provides an interface to view API token and links to other routes.
     * Implementation of {@link HttpHandler#handle}.
     * @param exch HttpExchange object to access request details/tailor response
     */
    public void handle(HttpExchange exch) {
        try {
            StringBuilder response = new StringBuilder();
            int responseCode;

            Headers headers = exch.getResponseHeaders();
            headers.set("Content-Type", "text/html");

            // If accessed through a GET request, respond with a login form.
            if(exch.getRequestMethod().equalsIgnoreCase("GET")) {

                responseCode = 200;
                response.append("<h1>Login</h1>");
                response.append("<p>Enter your login details to retrieve your token.</p>");
                response.append("<form action=\"login\" method=\"post\">");
                response.append("<label>Username: </label><input type=\"text\" name=\"username\"><br>");
                response.append("<label>Password: </label><input type=\"password\" name=\"password\"><br>");
                response.append("<input type=\"submit\" value=\"Login\">");
                response.append("</form>");
                response.append("<p>If you don't have an account, register <a href=\"register\">here</a>.</p>");

                // If accessed through a POST request, process POST data with account login logic.
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

                // If the user's details are correct, retrieve their API key and provide a GUI for them to manage their account.
                if (UserDAO.checkLoginCredentials(username, password)) {
                    String token = UserDAO.getApiKey(username);

                    responseCode = 200;

                    response.append("<h1>" + username + "'s Account</h1>");
                    response.append("<nav>");
                    response.append("<a href=\"/students?token=" + token + " \">View students</a> ");
                    response.append("<a href=\"/login\">Logout</a>");
                    response.append("</nav>");
                    response.append("<p>Your access token is: <b>" + token + "</b></p>");
                    response.append("<ul>");
                    response.append("<li>To <b>view</b> a specific student use GET: /<b>student</b>?token=" + token + "&id=<b>16012495</b></li>");
                    response.append("<li>To <b>create</b> a student use POST: /<b>student/create</b>?token=" + token + "</li>");
                    response.append("<li>To <b>update</b> a specific student use PUT: /<b>student/update</b>?token=" + token + "</li>");
                    response.append("<li>To <b>delete</b> a specific student use DELETE: /<b>student/delete</b>?token=" + token + "&id=<b>16012495</b></li>");
                    response.append("</ul>");
                } else {
                    // If a user's details are incorrect, append an appropriate message to the response.
                    responseCode = 400;
                    response.append("<h1>Invalid Credentials</h1>");
                    response.append("<p>Please try again, <a href=\"login\">here</a>.</p>");
                }
            } else {
                // If neither a GET or POST request is made, return a 400 bad request header.
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