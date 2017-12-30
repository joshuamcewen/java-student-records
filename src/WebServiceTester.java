import org.omg.CORBA_2_3.portable.OutputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Entry point for testing {@link Controller} methods.
 * @author Joshua McEwen (16012396)
 */

public class WebServiceTester {

    private static String token = "rLgmNbAVKfGwMfWkAz";

    /**
     * Retrieves the contents of server routes created in {@link Controller} for demonstration of the
     * handlers performing CRUD operations on the database.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Students JSON > " + getStudents());
        System.out.println("Student with ID 16012495 JSON > " + getStudent(16012495));
        System.out.println("Post new student JSON > " + postStudent());
        System.out.println("Update this student JSON > " + updateStudent());
        System.out.println("Delete this student JSON > " + deleteStudent(15093243));
    }

    /**
     * Retrieves the output of http://localhost:8005/students with API token passed. Output is
     * returned as a StringBuffer for display.
     * @return StringBuffer containing response
     */
    private static StringBuffer getStudents() {
        StringBuffer response = new StringBuffer();

        try {
            // Make a connection to the URL and read the contents.
            URL url = new URL("http://localhost:8005/students?token=" + token);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String output;

            // While there is output, append to the response StringBuffer.
            while((output = reader.readLine()) != null) {
                response.append(output);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    /**
     * Retrieves the output of http://localhost:8005/students with API token passed. Output is
     * returned as a StringBuffer for display.
     * @param stuNumber Student's student ID number
     * @return StringBuffer containing response
     */
    private static StringBuffer getStudent(int stuNumber) {
        StringBuffer response = new StringBuffer();

        try {
            // Make a connection to the URL and read the contents.
            URL url = new URL("http://localhost:8005/student?id=" + stuNumber + "&token=" + token);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String output;

            // While there is output, append to the response StringBuffer.
            while((output = reader.readLine()) != null) {
                response.append(output);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    /**
     * Retrieves the output of http://localhost:8005/student/create with API token and a new student JSON object passed. Output is
     * returned as a StringBuffer for display.
     * @return StringBuffer containing response
     */
    private static StringBuffer postStudent() {
        StringBuffer response = new StringBuffer();

        try {
            // Make a connection to the URL. HttpURLConnection is required, as additional parameters for POST needed.
            URL url = new URL("http://localhost:8005/student/create?token=" + token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set connection parameters to accept JSON, input and POST method.
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            // Define student JSON as string before writing to connection.
            String student = "{'studentNumber':15093243,'courseTitle':'Mathematics','startDate':'03-09-2015','bursary':550.0,'email':'tbirch@mmumail.com','name':'Tom Birch','gender':'M','dob':'19-01-1988','address':'Southbank','postcode':'PR9 8GT'}";
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(student);
            out.flush();

            // Read connection as normal.
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;

            // While there is output, append to the response StringBuffer.
            while((output = reader.readLine()) != null) {
                response.append(output);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    /**
     * Retrieves the output of http://localhost:8005/student/update with API token and an updated existing student JSON object passed. Output is
     * returned as a StringBuffer for display.
     * @return StringBuffer containing response
     */
    private static StringBuffer updateStudent() {
        StringBuffer response = new StringBuffer();

        try {
            // Make a connection to the URL. HttpURLConnection is required, as additional parameters for PUT needed.
            URL url = new URL("http://localhost:8005/student/update?token=" + token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set connection parameters to accept JSON, input and PUT method.
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setRequestMethod("PUT");

            // Define student JSON as string before writing to connection.
            String student = "{'studentNumber':15093243,'courseTitle':'Applied Mathematics','startDate':'03-09-2015','bursary':1550.0,'email':'tbirch@mmumail.com','name':'Tom Birch','gender':'M','dob':'19-01-1988','address':'Southbank','postcode':'PR9 8GT'}";
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(student);
            out.flush();

            // Read connection as normal.
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;

            // While there is output, append to the response StringBuffer.
            while((output = reader.readLine()) != null) {
                response.append(output);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }


    /**
     * Retrieves the output of http://localhost:8005/student/delete with API token and a student number passed. Output is
     * returned as a StringBuffer for display.
     * @param stuNumber Student's student ID number
     * @return StringBuffer containing response
     */
    private static StringBuffer deleteStudent(int stuNumber) {
        StringBuffer response = new StringBuffer();

        try {
            // Make a connection to the URL. HttpURLConnection is required, as additional parameters for DELETE needed.
            URL url = new URL("http://localhost:8005/student/delete?id=" + stuNumber + "&token=" + token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set connection parameters to use DELETE method.
            con.setRequestMethod("DELETE");

            // Read connection as normal.
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;

            // While there is output, append to the response StringBuffer.
            while((output = reader.readLine()) != null) {
                response.append(output);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }
}
