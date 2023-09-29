import java.util.ArrayList;
import java.util.List;

public class VWAPTradingAlgorithm {
    private List<Trade> trades;
    private double vwap;
    private double cumulativePrice;
    private int cumulativeVolume;

    public VWAPTradingAlgorithm() {
        this.trades = new ArrayList<>();
        this.vwap = 0.0;
        this.cumulativePrice = 0.0;
        this.cumulativeVolume = 0;
    }

    public void processTrade(Trade trade) {
        trades.add(trade);
        cumulativePrice += trade.getPrice() * trade.getVolume();
        cumulativeVolume += trade.getVolume();
        vwap = cumulativePrice / cumulativeVolume;
    }

    public double getVWAP() {
        return vwap;
    }

    public static void main(String[] args) {
        VWAPTradingAlgorithm vwapAlgorithm = new VWAPTradingAlgorithm();

        // Simulated trades for demonstration purposes
        Trade trade1 = new Trade(100.0, 100);
        Trade trade2 = new Trade(101.0, 200);
        Trade trade3 = new Trade(99.5, 150);

        vwapAlgorithm.processTrade(trade1);
        vwapAlgorithm.processTrade(trade2);
        vwapAlgorithm.processTrade(trade3);

        System.out.println("Current VWAP: " + vwapAlgorithm.getVWAP());
    }
}

class Trade {
    private double price;
    private int volume;

    public Trade(double price, int volume) {
        this.price = price;
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }
}
