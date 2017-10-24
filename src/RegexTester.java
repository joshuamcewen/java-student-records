public class RegexTester {
    public static void main(String[] args) {
        if("Joshua".matches("^[A-Za-z\\s]{1,6}+$")) {
            System.out.println("match");
        } else {
            System.out.println("no match");
        }
    }
}
