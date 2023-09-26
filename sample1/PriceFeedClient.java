package org.example.trading;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class PriceFeedClient {

    private static double profit = 0;
    private static double cashBalance = 1000;
    private static double portfolio = 0;


    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change this to the server's address
        int serverPort = 12345; // Change this to the server's port

        // Queue and sum for the short moving average (e.g., 10 periods)
        Queue<Double> shortWindow = new LinkedList<>();
        double shortSum = 0.0;
        int shortWindowSize = 10;

        // Queue and sum for the long moving average (e.g., 50 periods)
        Queue<Double> longWindow = new LinkedList<>();
        double longSum = 0.0;
        int longWindowSize = 50;

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to the Price Feed Server");
            boolean state = false;
            boolean prevState = false;

            String line;
            while ((line = in.readLine()) != null) {
                JsonObject jsonObject = parseJson(line);
//                System.out.println("Received: " + jsonObject.toString());

                // Extract the stock price value from the JSON object (assuming it's a double field named "price")
                double price = jsonObject.getJsonNumber("last_price").doubleValue();

                // Add the price value to both the short and long windows
                shortWindow.add(price);
                longWindow.add(price);

                shortSum += price;
                longSum += price;

                // If the windows are larger than the specified sizes, remove the oldest value
                if (shortWindow.size() > shortWindowSize) {
                    shortSum -= shortWindow.poll();
                }

                if (longWindow.size() > longWindowSize) {
                    longSum -= longWindow.poll();
                }

                double shortMovingAverage = 0;
                // Calculate and print the moving averages
                if (shortWindow.size() == shortWindowSize) {
                    shortMovingAverage = shortSum / shortWindowSize;
//                    System.out.println("Short Moving Average: " + shortMovingAverage);
                }

                double longMovingAverage = 0;

                if (longWindow.size() == longWindowSize) {
                    longMovingAverage = longSum / longWindowSize;
//                    System.out.println("Long Moving Average: " + longMovingAverage);
                }

                prevState = state;
                if (longMovingAverage > shortMovingAverage) {
                    state = false;
                }

                if (longMovingAverage < shortMovingAverage) {
                    state = true;
                }

                if (prevState && !state) {
                    buy(price);
                }

                if (!prevState && state) {
                    sell(price);
                }
            }
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }

    private static JsonObject parseJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        }
    }

    private static void buy(double price) {

        if (cashBalance <= 0 ) {
            return;
        }

        int qty = (int) (cashBalance / price);

        double totValue = qty * price;

        cashBalance = cashBalance - totValue;
        portfolio = portfolio + qty;


        System.out.println("buy:" + price + " cash balance " + cashBalance + " portfolio : " + portfolio);
    }

    private static void sell(double price) {

        if (portfolio <= 0){
            return;
        }

        double totValue = portfolio * price;
        cashBalance = cashBalance + totValue;
        portfolio = 0;

        System.out.println("sell:" + price + " cash balance " + cashBalance + " portfolio : " + portfolio);
    }


}
