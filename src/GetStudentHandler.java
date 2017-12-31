import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.Map;

/**
 * Handler to retrieve a specific user.
 * @author Joshua McEwen (16012396)
 */
public class GetStudentHandler implements HttpHandler {
    /**
     * Return a specific student based on student number passed in id parameter. A valid GET request and student number
     * are required.
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
                String queryString = exch.getRequestURI().getQuery();

                if(UserDAO.isValidToken(exch)) {

                    // Retrieve GET data through the request by mapping key pair values for the student's ID.
                    Map<String, String> params = Controller.getParameters(queryString);
                    int stuNumber = Integer.parseInt(params.get("id"));
                    Student retrieved = StudentDAO.getStudent(stuNumber);

                    // If a valid student record is retrieved, append the student as a JSON array to the re
                    if (retrieved != null) {
                        responseCode = 200;
                        response.append(new Gson().toJson(retrieved));
                    } else {
                        responseCode = 400;
                        response.append("Invalid student ID");
                    }
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