package budget;

public class Main {
    public static void main(String[] args) {
        //MVC pattern -- Budget class is model
        Controller controller = new Controller();
        controller.run();
    }
}
