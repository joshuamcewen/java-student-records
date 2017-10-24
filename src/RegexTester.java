public class RegexTester {
    public static void main(String[] args) {
        if("@m.com".matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            System.out.println("match");
        } else {
            System.out.println("no match");
        }
    }
}
