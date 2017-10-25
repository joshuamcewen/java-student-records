import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Entry point for testing {@link Server} methods.
 * @author Joshua McEwen (16012396)
 */

public class WebServiceTester {
    /**
     * Retrieves the contents of server routes created in {@link Server} for demonstration of the
     * handlers performing CRUD operations on the database.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Students JSON > " + getStudents());
    }

    private static StringBuffer getStudents() {
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL("http://localhost:8005/users");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String output;

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
