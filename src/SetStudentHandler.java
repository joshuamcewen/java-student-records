import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Handler to create a new user.
 * @author Joshua McEwen (16012396)
 */
public class SetStudentHandler implements HttpHandler {
    /**
     * Insert a student into the database when a valid POST request is made with valid JSON for the student.
     * Implementation of {@link HttpHandler#handle}.
     * @param exch HttpExchange object to access request details/tailor response
     */
    public void handle(HttpExchange exch) {
        try {

            StringBuilder response = new StringBuilder();
            int responseCode;

            // If accessed through a post request, continue to API token validation.
            if(exch.getRequestMethod().equalsIgnoreCase("POST")) {
                if(StudentDAO.isValidToken(exch)) {
                    StringBuilder request = new StringBuilder(); // New string, can be altered
                    BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                    String output;

                    while ((output = reader.readLine()) != null) {
                        request.append(output); // Append request line by line to StringBuilder.
                    }

                    // Parse the JSON and create an instance JsonObject from it.
                    JsonObject student = new Gson().fromJson(request.toString(), JsonObject.class);

                    // Access the JSON keys and their values
                    try {
                        String name = student.get("name").getAsString();
                        String gender = student.get("gender").getAsString();
                        String dob = student.get("dob").getAsString();
                        String address = student.get("address").getAsString();
                        String postcode = student.get("postcode").getAsString();
                        int sNumber = student.get("studentNumber").getAsInt();
                        String cTitle = student.get("courseTitle").getAsString();
                        String startDate = student.get("startDate").getAsString();
                        float bursary = student.get("bursary").getAsFloat();
                        String email = student.get("email").getAsString();

                        // Insert a student into the db using the values above.

                        StudentDAO.insertStudent(new Student(name, gender, dob, address, postcode, sNumber, cTitle, startDate, bursary, email));

                        responseCode = 200;
                        response.append("Successfully added student");
                    } catch (Exception e) {
                        responseCode = 400;
                        response.append("Unable to add student: " + e.getMessage());
                        Controller.writeResponse(exch, responseCode, response);
                    }
                    // Close the reader and write the response to the browser.
                    reader.close();
                } else {
                    responseCode = 400;
                    response.append("Invalid API key");
                }
            } else {
                responseCode = 400;
                response.append("Invalid POST request");
            }

            // Return a response to the client with values set above.
            Controller.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}