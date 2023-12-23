import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StockPortfolio {

    private static Map<String, Integer> stockHoldings = new HashMap<>();
    private static double cashBalance = 10000.0; // Initial cash balance
    private static final String DATA_FILE = "portfolio_data.txt";
    private static final String TRANSACTION_FILE = "stored.java";

    public static void main(String[] args) {
        loadPortfolioData();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nStock Portfolio Management System");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. Display Portfolio");
            System.out.println("4. Check Cash Balance");
            System.out.println("5. Save and Quit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    buyStock();
                    break;
                case 2:
                    sellStock();
                    break;
                case 3:
                    displayPortfolio();
                    break;
                case 4:
                    checkCashBalance();
                    break;
                case 5:
                    savePortfolioData();
                    saveTransactionData();
                    System.out.println("Data saved. Exiting the program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void buyStock() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the stock symbol: ");
        String stockSymbol = scanner.next().toUpperCase();

        System.out.print("Enter the number of shares to buy: ");
        int sharesToBuy = scanner.nextInt();

        // Simulate stock price (you may fetch real-time prices from an API)
        double stockPrice = 50.0;

        double totalCost = sharesToBuy * stockPrice;

        if (totalCost > cashBalance) {
            System.out.println("Insufficient funds. Cannot buy the stock.");
            return;
        }

        cashBalance -= totalCost;

        stockHoldings.put(stockSymbol, stockHoldings.getOrDefault(stockSymbol, 0) + sharesToBuy);

        System.out.println("Stock bought successfully!");

        // Save the transaction in the separate file
        saveTransaction(stockSymbol, sharesToBuy, stockPrice, "Buy");
    }

    private static void sellStock() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the stock symbol: ");
        String stockSymbol = scanner.next().toUpperCase();

        if (!stockHoldings.containsKey(stockSymbol)) {
            System.out.println("You don't own any shares of this stock.");
            return;
        }

        System.out.print("Enter the number of shares to sell: ");
        int sharesToSell = scanner.nextInt();

        int currentShares = stockHoldings.get(stockSymbol);

        if (sharesToSell > currentShares) {
            System.out.println("You don't have enough shares to sell.");
            return;
        }

        // Simulate stock price (you may fetch real-time prices from an API)
        double stockPrice = 50.0;

        double totalValue = sharesToSell * stockPrice;

        cashBalance += totalValue;

        stockHoldings.put(stockSymbol, currentShares - sharesToSell);

        System.out.println("Stock sold successfully!");

        // Save the transaction in the separate file
        saveTransaction(stockSymbol, sharesToSell, stockPrice, "Sell");
    }

    private static void displayPortfolio() {
        System.out.println("\nStock Portfolio:");

        if (stockHoldings.isEmpty()) {
            System.out.println("No stocks in the portfolio.");
        } else {
            for (Map.Entry<String, Integer> entry : stockHoldings.entrySet()) {
                String stockSymbol = entry.getKey();
                int shares = entry.getValue();
                System.out.println(stockSymbol + ": " + shares + " shares");
            }
        }
    }

    private static void checkCashBalance() {
        System.out.println("\nCash Balance: $" + cashBalance);
    }

    private static void loadPortfolioData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String stockSymbol = parts[0].trim();
                    int shares = Integer.parseInt(parts[1].trim());
                    stockHoldings.put(stockSymbol, shares);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading data. Starting with default values.");
        }
    }

    private static void savePortfolioData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, Integer> entry : stockHoldings.entrySet()) {
                String stockSymbol = entry.getKey();
                int shares = entry.getValue();
                writer.println(stockSymbol + ": " + shares);
            }
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    private static void saveTransaction(String stockSymbol, int quantity, double price, String action) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTION_FILE, true))) {
            writer.println("Stock: " + stockSymbol + ", Quantity: " + quantity + ", Price: $" + price + ", Action: " + action);
        } catch (IOException e) {
            System.out.println("Error saving transaction data.");
        }
    }

    private static void saveTransactionData() {
        System.out.println("\nTransaction History:");

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction data.");
        }
    }
}


