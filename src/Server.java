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

public class Server {
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

    private static class getUsersHandler implements HttpHandler {
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

    private static class setUserHandler implements HttpHandler {
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

    private static class updateUserHandler implements HttpHandler {
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

    private static class getUserHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {

                String response;
                int responseCode;

                if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                    Headers headers = exch.getResponseHeaders();
                    headers.set("Content-Type", "application/json");

                    String queryString = exch.getRequestURI().getQuery();

                    if(queryString != null) {
                        if(isValidToken(exch)) {

                            Map<String, String> params = getParameters(queryString);
                            int stuNumber = Integer.parseInt(params.get("id"));
                            Student retrieved = StudentDAO.getStudent(stuNumber);

                            if (retrieved != null) {
                                responseCode = 200;
                                response = new Gson().toJson(retrieved);
                            } else {
                                responseCode = 400;
                                response = "Student does not exist";
                            }
                        } else {
                            responseCode = 400;
                            response = "Invalid API key";
                        }
                    } else {
                        responseCode = 400;
                        response = "Parameters not set";
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

    private static class getTokenHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {

                String response;
                int responseCode;

                if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                    String queryString = exch.getRequestURI().getQuery();

                    if(queryString != null) {
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
                        response = "Parameters not set";
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

    private static class deleteUserHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {

                String response;
                int responseCode;

                if(exch.getRequestMethod().equalsIgnoreCase("DELETE")) {
                    String queryString = exch.getRequestURI().getQuery();

                    if(queryString != null) {
                        if(isValidToken(exch)) {
                            Map<String, String> params = getParameters(queryString);
                            int stuNumber = Integer.parseInt(params.get("id"));
                            boolean deleted = StudentDAO.deleteStudent(stuNumber);

                            if (deleted) {
                                responseCode = 200;
                                response = "Student with ID: " + stuNumber + " deleted";
                            } else {
                                responseCode = 400;
                                response = "Student does not exist.";
                            }
                        } else {
                            responseCode = 400;
                            response = "Invalid API key";
                        }
                    } else {
                        responseCode = 400;
                        response = "Parameters not set";
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

    private static boolean isValidToken(HttpExchange exch) {
        String queryString = exch.getRequestURI().getQuery();

        if(queryString != null) {
            Map<String, String> params = getParameters(queryString);
            String apiKey = params.get("token");

            try {
                if(apiKey != null && StudentDAO.checkApiKey(apiKey)) {
                    return true;
                }
            } catch(SQLException e) {
                return false;
            }
        }
        return false;
    }

    private static void writeResponse(HttpExchange exch, int status, String response) throws Exception {
        exch.sendResponseHeaders(status, response.getBytes().length);
        OutputStream os = exch.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

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

