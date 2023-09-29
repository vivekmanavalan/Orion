import java.util.ArrayList;
import java.util.List;

public class GridTradingAlgorithm {
    private List<Order> orders;
    private double initialPrice;
    private double gridSize;
    private int numberOfLevels;

    public GridTradingAlgorithm(double initialPrice, double gridSize, int numberOfLevels) {
        this.initialPrice = initialPrice;
        this.gridSize = gridSize;
        this.numberOfLevels = numberOfLevels;
        this.orders = new ArrayList<>();
        initializeOrders();
    }

    private void initializeOrders() {
        double price = initialPrice;
        for (int i = 0; i < numberOfLevels; i++) {
            Order buyOrder = new Order(OrderType.BUY, price, 1); // Adjust quantity as needed
            Order sellOrder = new Order(OrderType.SELL, price + gridSize, 1); // Adjust quantity as needed
            orders.add(buyOrder);
            orders.add(sellOrder);
            price += gridSize;
        }
    }

    public void executeOrders() {
        for (Order order : orders) {
            // Place the order in the market (actual implementation not provided)
            // You would need to use a trading API or platform for this.
            // Example: placeOrder(order);
            System.out.println("Placed order: " + order);
        }
    }

    public static void main(String[] args) {
        double initialPrice = 100.0; // Initial price of the asset
        double gridSize = 2.0; // Price interval between grid levels
        int numberOfLevels = 5; // Number of grid levels

        GridTradingAlgorithm gridTrading = new GridTradingAlgorithm(initialPrice, gridSize, numberOfLevels);
        gridTrading.executeOrders();
    }
}

enum OrderType {
    BUY,
    SELL
}

class Order {
    private OrderType type;
    private double price;
    private int quantity;

    public Order(OrderType type, double price, int quantity) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return type + " " + quantity + " @ " + price;
    }
}
