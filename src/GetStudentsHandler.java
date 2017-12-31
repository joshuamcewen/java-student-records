import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler to retrieve all users.
 * @author Joshua McEwen (16012396)
 */
public class GetStudentsHandler implements HttpHandler {
    /**
     * Return all students from the database in JSON format when a valid GET request is made.
     * Implementation of {@link HttpHandler#handle}.
     * @param exch HttpExchange object to access request details/tailor response
     */
    public void handle(HttpExchange exch) {
        try {

            StringBuilder response = new StringBuilder();
            int responseCode;

            // If accessed through a GET request, continue to API token validation.
            if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                Headers headers = exch.getResponseHeaders();
                headers.set("Content-Type", "application/json");

                // If a valid API token is present, append all students as a JSON array to the response.
                if(UserDAO.isValidToken(exch)) {
                    responseCode = 200;
                    response.append(new Gson().toJson(StudentDAO.getAllStudents()));
                } else {
                    responseCode = 400;
                    response.append("Invalid API key");
                }
            } else {
                responseCode = 400;
                response.append("Invalid GET request");
            }

            // Return a response to the client with values set above.
            Controller.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}