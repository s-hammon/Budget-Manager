package budget;

import java.util.*;
import java.util.stream.Collectors;

public class Budget {
    private final List<PurchaseItem> purchasesList = new ArrayList<>();
    private double balance = 0.00;

    public void addIncome(double num) {
        balance += num;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<PurchaseItem> getPurchasesList() {
        return new ArrayList<>(purchasesList);
    }

    public List<PurchaseItem> getPurchasesList(Category category) {
        return purchasesList.stream()
                .filter(item -> item.getCategory() == category)
                .collect(Collectors.toList());
    }

    public double getExpenses() {
        double sum = 0;
        for (PurchaseItem item : purchasesList) {
            sum += item.getPrice();
        }

        return sum;
    }

    public double getExpenses(Category category) {
        double sum = 0;
        for (PurchaseItem item : purchasesList) {
            if (item.getCategory() != category) continue;
            sum += item.getPrice();
        }

        return sum;
    }

    public void addPurchase(String name, double price, Category category) {
        purchasesList.add(new PurchaseItem(name, price, category));
        balance -= price;
        if (balance < 0) balance = 0;
    }

    public void importPurchases(String name, double price, Category category) {
        purchasesList.add(new PurchaseItem(name, price, category));
    }

    public Map<Double, List<Category>> getCategoryTotals() {
        //places names of categories & their total expenses into map
        Map<Double, List<Category>> map = new TreeMap<>((o1, o2) -> Double.compare(o2, o1));
        for (Category category : Category.values()) {
            Double sum = getExpenses(category);
            List<Category>list = map.getOrDefault(sum, new ArrayList<>());
            list.add(category);
            map.put(sum, list);
        }

        return map;
    }
}
