import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
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
                if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                    Headers headers = exch.getResponseHeaders();
                    headers.set("Content-Type", "application/json");

                    String response = new Gson().toJson(StudentDAO.getAllStudents());
                    writeResponse(exch, 200, response);
                } else {
                    writeResponse(exch, 400, "Invalid GET request");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class setUserHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {
                if(exch.getRequestMethod().equalsIgnoreCase("POST")) {
                    // DO SOMETHING WITH THE POST DATA
                    StringBuilder response = new StringBuilder(); // New string, can be altered
                    BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                    String output;

                    while((output = reader.readLine()) != null) {
                        response.append(output); // Append request line by line to StringBuilder.
                    }

                    // Parse the JSON and create an instance JsonObject from it.
                    JsonObject student = new Gson().fromJson(response.toString(), JsonObject.class);

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
                        writeResponse(exch, 200, "Successfully added student");
                    } catch(Exception e) {
                        writeResponse(exch, 200, "Unable to add student");
                    }
                    // Close the reader and write the response to the browser.
                    reader.close();
                } else {
                    writeResponse(exch, 400, "Invalid POST request");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class updateUserHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {
                if(exch.getRequestMethod().equalsIgnoreCase("PUT")) {
                    // DO SOMETHING WITH THE POST DATA
                    StringBuilder response = new StringBuilder(); // New string, can be altered
                    BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody())); // Read the req
                    String output;

                    while((output = reader.readLine()) != null) {
                        response.append(output); // Append request line by line to StringBuilder.
                    }

                    // Parse the JSON and create an instance JsonObject from it.
                    JsonObject student = new Gson().fromJson(response.toString(), JsonObject.class);

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
                        writeResponse(exch, 200, "Successfully updated " + updated_student.getName());
                    } catch(Exception e) {
                        writeResponse(exch, 400, "Unable to update student.");
                    }
                    // Close the reader and write the response to the browser.
                    reader.close();
                } else {
                    writeResponse(exch, 400, "Invalid PUT request");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class getUserHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {
                if(exch.getRequestMethod().equalsIgnoreCase("GET")) {
                    Headers headers = exch.getResponseHeaders();
                    headers.set("Content-Type", "application/json");

                    String queryString = exch.getRequestURI().getQuery();

                    if(queryString != null) {

                        Map<String, String> params = getParameters(queryString);
                        int stuNumber = Integer.parseInt(params.get("id"));
                        Student retrieved = StudentDAO.getStudent(stuNumber);

                        if(retrieved != null) {
                            String response = new Gson().toJson(retrieved);
                            writeResponse(exch, 200, response);
                        } else {
                            writeResponse(exch, 400, "Student does not exist");
                        }
                    } else {
                        writeResponse(exch, 400, "No parameters supplied.");
                    }
                } else {
                    writeResponse(exch, 400, "Invalid GET request");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class deleteUserHandler implements HttpHandler {
        public void handle(HttpExchange exch) {
            try {
                if(exch.getRequestMethod().equalsIgnoreCase("DELETE")) {
                    String queryString = exch.getRequestURI().getQuery();

                    if(queryString != null) {

                        Map<String, String> params = getParameters(queryString);
                        int stuNumber = Integer.parseInt(params.get("id"));
                        boolean deleted = StudentDAO.deleteStudent(stuNumber);

                        if(deleted) {
                            writeResponse(exch, 200, "Student with ID: " + stuNumber + " deleted");
                        } else {
                            writeResponse(exch, 400, "Student does not exist");
                        }
                    } else {
                        writeResponse(exch, 400, "No parameters supplied.");
                    }
                } else {
                    writeResponse(exch, 400, "Invalid DELETE request");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void writeResponse(HttpExchange exch, int status, String response) throws Exception {
        exch.sendResponseHeaders(status, response.getBytes().length);
        OutputStream os = exch.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Map<String, String> getParameters(String queryString) {
        Map<String, String> params = new HashMap<String, String>();

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

