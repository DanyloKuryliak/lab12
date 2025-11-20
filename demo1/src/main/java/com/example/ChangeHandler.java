package com.example;

import java.util.ArrayList;
import java.util.List;

public abstract class ChangeHandler {
    protected ChangeHandler nextHandler;
    protected final int denomination;

    public ChangeHandler(int denomination) {
        this.denomination = denomination;
    }

    public ChangeHandler setNext(ChangeHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

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
                return ChangeResult.failure(
                    "Cannot make exact change with available denominations. " +
                    "No handler available for remaining amount.",
                    remaining
                );
            }
        } else {
            if (nextHandler != null) {
                return nextHandler.handle(amount, coinsUsed);
            } else {
                return ChangeResult.failure(
                    "Cannot make exact change with available denominations. " +
                    "Remaining amount cannot be satisfied.",
                    amount
                );
            }
        }
    }

    protected int calculateCoinsToUse(int amount) {
        if (amount >= denomination) {
            return amount / denomination;
        }
        return 0;
    }

    public int getDenomination() {
        return denomination;
    }
}

