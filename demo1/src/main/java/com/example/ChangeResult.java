package com.example;

import java.util.ArrayList;
import java.util.List;

public class ChangeResult {
    private final boolean success;
    private final int totalCoins;
    private final List<CoinCount> coins;
    private final String errorMessage;
    private final int remainingAmount;

    private ChangeResult(boolean success, int totalCoins, List<CoinCount> coins, 
                        String errorMessage, int remainingAmount) {
        this.success = success;
        this.totalCoins = totalCoins;
        this.coins = coins != null ? new ArrayList<>(coins) : new ArrayList<>();
        this.errorMessage = errorMessage;
        this.remainingAmount = remainingAmount;
    }

    public static ChangeResult success(List<CoinCount> coins, int totalCoins) {
        return new ChangeResult(true, totalCoins, coins, null, 0);
    }

    public static ChangeResult failure(String errorMessage, int remainingAmount) {
        return new ChangeResult(false, 0, null, errorMessage, remainingAmount);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public List<CoinCount> getCoins() {
        return new ArrayList<>(coins);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    @Override
    public String toString() {
        if (success) {
            StringBuilder sb = new StringBuilder();
            sb.append("Success! Total coins: ").append(totalCoins).append("\n");
            sb.append("Coins used:\n");
            for (CoinCount coinCount : coins) {
                sb.append("  ").append(coinCount.getCount())
                  .append(" x ").append(coinCount.getDenomination())
                  .append(" = ").append(coinCount.getTotalValue()).append("\n");
            }
            return sb.toString();
        } else {
            return "Failure: " + errorMessage + 
                   (remainingAmount > 0 ? " (Remaining: " + remainingAmount + ")" : "");
        }
    }

    public static class CoinCount {
        private final int denomination;
        private final int count;

        public CoinCount(int denomination, int count) {
            this.denomination = denomination;
            this.count = count;
        }

        public int getDenomination() {
            return denomination;
        }

        public int getCount() {
            return count;
        }

        public int getTotalValue() {
            return denomination * count;
        }
    }
}

