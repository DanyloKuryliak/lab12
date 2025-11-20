package com.example;

import java.util.List;

public class LimitedSupplyHandler extends ChangeHandler {
    private final int availableCoins;

    public LimitedSupplyHandler(int denomination, int availableCoins) {
        super(denomination);
        this.availableCoins = availableCoins;
    }

    @Override
    protected int calculateCoinsToUse(int amount) {
        int idealCoins = amount / denomination;
        return Math.min(idealCoins, availableCoins);
    }

    public boolean hasEnoughCoins(int amount) {
        int needed = amount / denomination;
        return availableCoins >= needed;
    }

    public int getAvailableCoins() {
        return availableCoins;
    }

    @Override
    public ChangeResult handle(int amount, List<ChangeResult.CoinCount> coinsUsed) {
        if (amount < 0) {
            return ChangeResult.failure("Invalid amount: cannot be negative", amount);
        }

        if (amount == 0) {
            int totalCoins = coinsUsed.stream()
                    .mapToInt(ChangeResult.CoinCount::getCount)
                    .sum();
            return ChangeResult.success(coinsUsed, totalCoins);
        }

        int coinsToUse = calculateCoinsToUse(amount);
        
        if (coinsToUse > 0) {
            coinsUsed.add(new ChangeResult.CoinCount(denomination, coinsToUse));
            int remaining = amount - (denomination * coinsToUse);
            
            if (nextHandler != null) {
                return nextHandler.handle(remaining, coinsUsed);
            } else if (remaining == 0) {
                int totalCoins = coinsUsed.stream()
                        .mapToInt(ChangeResult.CoinCount::getCount)
                        .sum();
                return ChangeResult.success(coinsUsed, totalCoins);
            } else {
                if (coinsToUse < availableCoins && remaining > 0) {
                    return ChangeResult.failure(
                        "Not enough coins available. " +
                        "Need more coins of denomination " + denomination + 
                        " or smaller denominations.",
                        remaining
                    );
                }
                return ChangeResult.failure(
                    "Cannot make exact change with available denominations.",
                    remaining
                );
            }
        } else {
            if (nextHandler != null) {
                return nextHandler.handle(amount, coinsUsed);
            } else {
                return ChangeResult.failure(
                    "Cannot make exact change. " +
                    "Remaining amount: " + amount + 
                    " cannot be satisfied with available denominations.",
                    amount
                );
            }
        }
    }
}

