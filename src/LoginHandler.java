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
            StringBuilder response = new StringBuilder();
            int responseCode;

            Headers headers = exch.getResponseHeaders();
            headers.set("Content-Type", "text/html");

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

            } else if(exch.getRequestMethod().equalsIgnoreCase("POST")) {
                StringBuilder request = new StringBuilder(); // New string, can be altered
                BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                String output;

                while ((output = reader.readLine()) != null) {
                    request.append(output); // Append request line by line to StringBuilder.
                }

                Map<String, String> params = Controller.getParameters(request.toString());
                String username = params.get("username");
                String password = params.get("password");

                if (StudentDAO.checkLoginCredentials(username, password)) {
                    String token = StudentDAO.getApiKey(username);

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
                    responseCode = 400;
                    response.append("<h1>Invalid Credentials</h1>");
                    response.append("<p>Please try again, <a href=\"login\">here</a>.</p>");
                }
            } else {
                responseCode = 400;
                response.append("Invalid GET/POST request");
            }

            Controller.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}