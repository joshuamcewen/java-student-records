import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Handler to update an existing student.
 * @author Joshua McEwen (16012396)
 */
public class UpdateStudentHandler implements HttpHandler {
    /**
     * Update an existing student in the database when a valid PUT request is made with valid JSON for the student.
     * Implementation of {@link HttpHandler#handle}.
     * @param exch HttpExchange object to access request details/tailor response
     */
    public void handle(HttpExchange exch) {
        try {

            StringBuilder response = new StringBuilder();
            int responseCode;

            if(exch.getRequestMethod().equalsIgnoreCase("PUT")) {
                if(StudentDAO.isValidToken(exch)) {
                    StringBuilder request = new StringBuilder(); // New string, can be altered
                    BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                    String output;

                    while((output = reader.readLine()) != null) {
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

                        // update a student into the db using the values above.
                        Student updated_student = new Student(name, gender, dob, address, postcode, sNumber, cTitle, startDate, bursary, email);
                        StudentDAO.updateStudent(updated_student);

                        responseCode = 200;
                        response.append("Successfully updated " + updated_student.getName());
                    } catch(Exception e) {
                        responseCode = 400;
                        response.append("Unable to update student: " + e.getMessage());
                    }
                    // Close the reader and write the response to the browser.
                    reader.close();
                } else {
                    responseCode = 400;
                    response.append("Invalid API key");
                }
            } else {
                responseCode = 400;
                response.append("Invalid PUT request");
            }

            // Return a response to the client with values set above.
            Controller.writeResponse(exch, responseCode, response);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}