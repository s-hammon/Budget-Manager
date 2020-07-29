package budget;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static final String FILE = "purchases.txt";
    private static final String DELIMITER = ";";

    private final UserView view = new UserView();
    private final Budget budget = new Budget();
    final Scanner sc = new Scanner(System.in);

    public void run() {
        //display menu & collect user input
        String command;
        do {
            view.menu();
            command = sc.nextLine();
            switch (command) {
                case "0":
                    view.message("\nBye!");
                    break;
                case "1":
                    addIncome();
                    break;
                case "2":
                    addPurchase();
                    break;
                case "3":
                    showPurchases();
                    break;
                case "4":
                    balance();
                    break;
                case "5":
                    save();
                    break;
                case "6":
                    load();
                    break;
                case "7":
                    analyze();
                    break;
                default:
                    view.message("Invalid command!");
            }
        } while (!"0".equals(command));

        sc.close();
    }

    private void analyze() {
        while (true) {
            view.sortMenu();
            String command = sc.nextLine();
            switch (command) {
                case "1":
                    sortAllPurchases();
                    break;
                case "2":
                    sortByType();
                    break;
                case "3":
                    sortType();
                    break;
                case "4":
                    view.emptyLine();
                    return;
                default:
                    view.message("Invalid command!\n");
            }
        }
    }

    private void sortAllPurchases() {
        List<PurchaseItem> list = budget.getPurchasesList();
        if (list.isEmpty())
            view.message("\nPurchase list is empty!");
        else {
            list.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));

            view.message("\nAll:");
            view.list(list);
            view.messageWithSum("Total:", budget.getExpenses());
        }
    }

    private void sortByType() {
        view.message("\nTypes:");
        view.listTree(budget.getCategoryTotals());
        view.messageWithSum("Total sum:", budget.getExpenses());
    }

    private void sortType() {
        view.typeMenu();
        String command = sc.nextLine();
        if (command.length() != 1 || !"1|2|3|4".contains(command)) {
            view.message("Invalid command!");
            return;
        }

        Category category = Category.values()[Integer.parseInt(command) - 1];
        List<PurchaseItem> list = budget.getPurchasesList(category);
        if (list.isEmpty()) {
            view.message("\nPurchase list is empty!");
            return;
        }

        list.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));
        view.message(String.format("\n%s:", category.getName()));
        view.list(list);
        view.messageWithSum("Total sum:", budget.getExpenses(category));
    }

    private void showPurchases() {
        if (budget.getPurchasesList().isEmpty()) {
            view.message("\nPurchase list is empty!\n");
            return;
        }

        while (true) {
            view.message("\nChoose the type of purchase");
            view.categoriesWithItemAll();

            int input;
            try {
                input = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                view.message("Invalid command!");
                continue;
            }

            if (input == 6) {
                view.emptyLine();
                return;
            }
            if (input < 1 || input > 6) {
                view.message("Invalid command!");
                continue;
            }
            if (input == 5) {
                view.message("\nAll:");
                view.list(budget.getPurchasesList());
                view.messageWithSum("Total sum:", budget.getExpenses());
            } else {
                Category category = Category.values()[input - 1];
                List<PurchaseItem> purchasesList = budget.getPurchasesList(category);
                view.categoryName(category);
                if (purchasesList.isEmpty())
                    view.message("\nPurchase list is empty!");
                else {
                    view.list(purchasesList);
                    view.messageWithSum("Total sum:", budget.getExpenses(category));
                }
            }
        }
    }

    private void load() {
        File file = new File(FILE);
        if (!file.exists()) {
            view.message("File " + FILE + " does not exist.");
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            if (!sc.hasNextLine()) {
                view.message("The file is empty.");
                return;
            }

            budget.setBalance(Double.parseDouble(sc.nextLine().replaceAll(",", ".")));

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int firstDelimit = line.indexOf(DELIMITER);
                int secondDelimit = line.lastIndexOf(DELIMITER);
                budget.importPurchases(
                        line.substring(firstDelimit + 1, secondDelimit),
                        Double.parseDouble(line.substring(secondDelimit + 1).replaceAll(",", ".")),
                        Category.valueOf(line.substring(0, firstDelimit))
                );
            }

            view.message("\nPurchases were loaded!\n");
        } catch (IOException e) {
            view.message("File was not loaded: " + e.getMessage());
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            writer.write(String.format("%.2f\n", budget.getBalance()));

            for (PurchaseItem item : budget.getPurchasesList()) {
                writer.write(String.format("%s%s%s%s%.2f\n",
                        item.getCategory(),
                        DELIMITER,
                        item.getName(),
                        DELIMITER,
                        item.getPrice()));
            }

            view.message("\nPurchases were saved!\n");
        } catch (IOException e) {
            view.message("Error with saving file: " + e.getMessage());
        }
    }

    private void addPurchase() {
        while (true) {
            view.message("\nChoose the type of purchase:");
            view.categories();

            int input;
            try {
                input = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                view.message("Invalid command!");
                continue;
            }

            if (input == 5) {
                view.emptyLine();
                return;
            }
            if (input < 1 || input > 5) {
                view.message("Invalid command!");
                continue;
            }

            Category category = Category.values()[input - 1];
            view.message("\nEnter purchase name:");
            String name = sc.nextLine();
            view.message("\nEnter its price");
            double price = Double.parseDouble(sc.nextLine());
            budget.addPurchase(name, price, category);
            view.message("Purchase was added!");
        }
    }

    private void addIncome() {
        view.message("\nEnter income");
        double num = Double.parseDouble(sc.nextLine());
        budget.addIncome(num);
        view.message("Income was added!\n");
    }

    private void balance() {
        view.messageWithSum("\nBalance", budget.getBalance());
        view.emptyLine();
    }
}
