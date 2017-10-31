import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler to retrieve all users.
 */
public class GetUsersHandler implements HttpHandler {
    /**
     * Return all students from the database in JSON format when a valid GET request is made.
     * Implementation of {@link HttpHandler#handle}.
     * @param exch HttpExchange object to access request details/tailor response
     */
    public void handle(HttpExchange exch) {
        try {

            String response;
            int responseCode;

            if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                Headers headers = exch.getResponseHeaders();
                headers.set("Content-Type", "application/json");

                if(Server.isValidToken(exch)) {
                    responseCode = 200;
                    response = new Gson().toJson(StudentDAO.getAllStudents());
                } else {
                    responseCode = 400;
                    response = "Invalid API key";
                }
            } else {
                responseCode = 400;
                response = "Invalid GET request";
            }
            Server.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}