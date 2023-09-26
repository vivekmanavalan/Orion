package org.example.trading;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class PriceFeedServer {

    public static void main(String[] args) {
        int port = 12345; // Port for the server
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Price feed server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // Generate and send price feed data every second
                    while (!clientSocket.isClosed()) {
                        String priceFeedData = generatePriceFeed();
                        out.println(priceFeedData);
                        TimeUnit.MILLISECONDS.sleep(100); // Publish data every second
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    private static String generatePriceFeed() {
        String[] symbols = {"AAPL", "GOOGL", "MSFT", "AMZN"};
        String symbol = symbols[new Random().nextInt(symbols.length)];
        double lastPrice = 100 + (Math.random() * 20);
        double bidPrice = lastPrice - 0.05;
        double askPrice = lastPrice + 0.05;
        int volume = new Random().nextInt(10000);

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedLastPrice = decimalFormat.format(lastPrice);
        String formattedBidPrice = decimalFormat.format(bidPrice);
        String formattedAskPrice = decimalFormat.format(askPrice);

        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("symbol", symbol)
                .add("timestamp", System.currentTimeMillis())
                .add("last_price", lastPrice)
                .add("bid_price", bidPrice)
                .add("ask_price", askPrice)
                .add("volume", volume);

        return builder.build().toString();
    }
}
