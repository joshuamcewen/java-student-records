import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.Map;

/**
 * Handler to delete an existing student.
 */
public class DeleteUserHandler implements HttpHandler {
    /**
     * Delete an existing student in the database based on a student number passed in id parameter. A valid DELETE request
     * and student number are required.
     * Implementation of {@link HttpHandler#handle}.
     * @param exch HttpExchange object to access request details/tailor response
     */
    public void handle(HttpExchange exch) {
        try {

            String response;
            int responseCode;

            if(exch.getRequestMethod().equalsIgnoreCase("DELETE")) {
                String queryString = exch.getRequestURI().getQuery();

                if(Server.isValidToken(exch)) {
                    Map<String, String> params = Server.getParameters(queryString);
                    int stuNumber = Integer.parseInt(params.get("id"));
                    boolean deleted = StudentDAO.deleteStudent(stuNumber);

                    if (deleted) {
                        responseCode = 200;
                        response = "Student with ID: " + stuNumber + " deleted";
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
                response = "Invalid DELETE request";
            }
            Server.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}