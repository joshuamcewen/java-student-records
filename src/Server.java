import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a HTTP Server and routes with corresponding handlers for CRUD operations on the database.
 * @author Joshua McEwen (16012396)
 */

public class Server {
    /**
     * Entry point for starting the server. Creates the HttpServer object on port 8005 and sets routes/handlers that
     * execute when routes are accessed.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8005), 0);

            // Route handling
            server.createContext("/users", new getUsersHandler());
            server.createContext("/user/new", new setUserHandler());
            server.createContext("/user/update", new updateUserHandler());
            server.createContext("/user", new getUserHandler());
            server.createContext("/user/delete", new deleteUserHandler());
            server.createContext("/access", new getTokenHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port 8005.");
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handler to retrieve all users.
     */
    private static class getUsersHandler implements HttpHandler {
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

                    if(isValidToken(exch)) {
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
                writeResponse(exch, responseCode, response);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handler to create a new user.
     */
    private static class setUserHandler implements HttpHandler {
        /**
         * Insert a student into the database when a valid POST request is made with valid JSON for the student.
         * Implementation of {@link HttpHandler#handle}.
         * @param exch HttpExchange object to access request details/tailor response
         */
        public void handle(HttpExchange exch) {
            try {

                String response;
                int responseCode;

                if(exch.getRequestMethod().equalsIgnoreCase("POST")) {
                    if(isValidToken(exch)) {
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
                            response = "Successfully added student";
                        } catch (Exception e) {
                            responseCode = 400;
                            response = "Unable to add student: " + e.getMessage();
                            writeResponse(exch, responseCode, response);
                        }
                        // Close the reader and write the response to the browser.
                        reader.close();
                    } else {
                        responseCode = 400;
                        response = "Invalid API key";
                    }
                } else {
                    responseCode = 400;
                    response = "Invalid POST request";
                }
                writeResponse(exch, responseCode, response);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handler to update an existing student.
     */
    private static class updateUserHandler implements HttpHandler {
        /**
         * Update an existing student in the database when a valid PUT request is made with valid JSON for the student.
         * Implementation of {@link HttpHandler#handle}.
         * @param exch HttpExchange object to access request details/tailor response
         */
        public void handle(HttpExchange exch) {
            try {

                String response;
                int responseCode;

                if(exch.getRequestMethod().equalsIgnoreCase("PUT")) {
                    if(isValidToken(exch)) {
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
                            response = "Successfully updated " + updated_student.getName();
                        } catch(Exception e) {
                            responseCode = 400;
                            response = "Unable to update student: " + e.getMessage();
                        }
                        // Close the reader and write the response to the browser.
                        reader.close();
                    } else {
                        responseCode = 400;
                        response = "Invalid API key";
                    }
                } else {
                    responseCode = 400;
                    response = "Invalid PUT request";
                }
                writeResponse(exch, responseCode, response);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handler to retrieve a specific user.
     */
    private static class getUserHandler implements HttpHandler {
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

                    if(isValidToken(exch)) {
                        Map<String, String> params = getParameters(queryString);
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
                writeResponse(exch, responseCode, response);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handler to delete an existing student.
     */
    private static class deleteUserHandler implements HttpHandler {
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

                    if(isValidToken(exch)) {
                        Map<String, String> params = getParameters(queryString);
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
                writeResponse(exch, responseCode, response);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handler to retrieve an API token.
     */
    private static class getTokenHandler implements HttpHandler {
        /**
         * Retrieve an API token from the users table from the values supplied by username and password parameters.
         * A valid GET request and credentials are required.
         * Implementation of {@link HttpHandler#handle}.
         * @param exch HttpExchange object to access request details/tailor response
         */
        public void handle(HttpExchange exch) {
            try {

                String response;
                int responseCode;

                if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                    String queryString = exch.getRequestURI().getQuery();

                    Map<String, String> params = getParameters(queryString);
                    String username = params.get("username");
                    String password = params.get("password");

                    if (StudentDAO.checkLoginCredentials(username, password)) {
                        String token = StudentDAO.getApiKey(username);
                        responseCode = 200;
                        response = "Access token for " + username + ": " + token;
                    } else {
                        responseCode = 400;
                        response = "Invalid credentials";
                    }
                } else {
                    responseCode = 400;
                    response = "Invalid GET request";
                }
                writeResponse(exch, responseCode, response);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Checks the API token supplied as a URL parameter and returns a boolean based on the outcome of that check.
     * @param exch HttpExchange object to access request details
     * @return true/false depending on whether the token exists or not.
     */
    private static boolean isValidToken(HttpExchange exch) {
        String queryString = exch.getRequestURI().getQuery();

        if(queryString != null) {
            Map<String, String> params = getParameters(queryString);
            String apiKey = params.get("token");

            if(apiKey != null && StudentDAO.checkApiKey(apiKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the headers for the response and writes the body to displayed to users when routes are accessed.
     * @param exch HttpExchange object to modify and send response
     * @param status A HTTP response code based on result of request
     * @param response Textual response displayed to the user
     * @throws IOException When response is invalid
     */
    private static void writeResponse(HttpExchange exch, int status, String response) throws IOException {
        exch.sendResponseHeaders(status, response.getBytes().length);
        OutputStream os = exch.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Sets the headers for the response and writes the body to displayed to users when routes are accessed.
     * @param queryString Query string from URL, gained from HttpExchange object.
     * @return A map of the URL parameters (name and values)
     */
    private static Map<String, String> getParameters(String queryString) {
        Map<String, String> params = new HashMap<>();

        for(String param : queryString.split("&")) { // Split the keys and their values
            String pair[] = param.split("="); // Separate the key and value

            if(pair.length > 1) { // If a key and a value exists, add the pair to the map.
                params.put(pair[0], pair[1]);
            } else {
                params.put(pair[0], "Undefined"); // If only a key exists, set the value as undefined.
            }
        }

        return params; // Return the map for access
    }

}

