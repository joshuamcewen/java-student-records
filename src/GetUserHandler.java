import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.Map;

/**
 * Handler to retrieve a specific user.
 */
public class GetUserHandler implements HttpHandler {
    /**
     * Return a specific student based on student number passed in id parameter. A valid GET request and student number
     * are required.
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
                String queryString = exch.getRequestURI().getQuery();

                if(Server.isValidToken(exch)) {
                    Map<String, String> params = Server.getParameters(queryString);
                    int stuNumber = Integer.parseInt(params.get("id"));
                    Student retrieved = StudentDAO.getStudent(stuNumber);

                    if (retrieved != null) {
                        responseCode = 200;
                        response = new Gson().toJson(retrieved);
                    } else {
                        responseCode = 400;
                        response = "Invalid student ID";
                    }
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