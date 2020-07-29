package budget;

public enum Category {
    FOOD, CLOTHES, ENTERTAINMENT, OTHER;

    public String getName() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
}
