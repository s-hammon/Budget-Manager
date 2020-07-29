package budget;

import java.util.List;
import java.util.Map;

public class UserView {
    //handles all menu displays
    public void menu() {
        System.out.print("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit\n");
    }

    public void message(String message){ System.out.println(message); }

    public void messageWithSum(String message, double sum) { System.out.printf("%s $%.2f\n", message, sum); }

    public void emptyLine() { System.out.println(); }

    public void list(List<?> list) {
        list.forEach(System.out::println);
    }

    public void sortMenu() {
        System.out.println();
        System.out.format("How do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back\n");
    }

    public void typeMenu() {
        System.out.println();
        System.out.format("Choose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n");
    }

    public void categoryName(Category category) {
        String name = category.name();
        System.out.printf("\n%s%s: \n", name.substring(0, 1), name.substring(1).toLowerCase());
    }

    public void categories() {
        categories(false);
    }

    public void categoriesWithItemAll() {
        categories(true);
    }

    private void categories(boolean isItemAll) {
        for (Category category : Category.values()) {
            System.out.printf("%d) %s\n", category.ordinal() + 1, category.name());
        }

        int item = Category.values().length;
        if (isItemAll) {
            System.out.printf("%d) %s\n", ++item, "All");
        }

        System.out.printf("%d) %s\n", ++item, "Back");
    }

    public void listTree(Map<Double, List<Category>> map) {
        for (Map.Entry<Double, List<Category>> entry : map.entrySet()) {
            Double sum = entry.getKey();
            String formatted = sum <= 0.001 ? "%s - $%.0f\n" : "%s - $%.2f\n";
            for (Category category : entry.getValue()) {
                System.out.printf(formatted, category.getName(), sum);
            }
        }
    }
}
