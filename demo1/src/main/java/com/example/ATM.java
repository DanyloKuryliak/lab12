package com.example;

import java.util.ArrayList;
import java.util.List;

public class ATM {
    private ChangeHandler firstHandler;
    private final List<Integer> denominations;
    private final boolean unlimitedSupply;

    public ATM(int[] denominations) {
        this.denominations = new ArrayList<>();
        for (int d : denominations) {
            this.denominations.add(d);
        }
        this.unlimitedSupply = true;
        buildChain();
    }

    public ATM(int[] denominations, int[] availableCoins) {
        this.denominations = new ArrayList<>();
        for (int d : denominations) {
            this.denominations.add(d);
        }
        this.unlimitedSupply = false;
        buildChainWithLimits(availableCoins);
    }

    private void buildChain() {
        if (denominations.isEmpty()) {
            throw new IllegalArgumentException("At least one denomination is required");
        }

        List<Integer> sorted = new ArrayList<>(denominations);
        sorted.sort((a, b) -> Integer.compare(b, a));

        firstHandler = new ChangeHandler(sorted.get(0)) {};
        ChangeHandler current = firstHandler;

        for (int i = 1; i < sorted.size(); i++) {
            ChangeHandler next = new ChangeHandler(sorted.get(i)) {};
            current.setNext(next);
            current = next;
        }
    }

    private void buildChainWithLimits(int[] availableCoins) {
        if (denominations.size() != availableCoins.length) {
            throw new IllegalArgumentException(
                "Number of denominations must match number of available coin arrays"
            );
        }

        List<DenominationSupply> pairs = new ArrayList<>();
        for (int i = 0; i < denominations.size(); i++) {
            pairs.add(new DenominationSupply(denominations.get(i), availableCoins[i]));
        }
        pairs.sort((a, b) -> Integer.compare(b.denomination, a.denomination));

        firstHandler = new LimitedSupplyHandler(pairs.get(0).denomination, pairs.get(0).available);
        ChangeHandler current = firstHandler;

        for (int i = 1; i < pairs.size(); i++) {
            LimitedSupplyHandler next = new LimitedSupplyHandler(
                pairs.get(i).denomination, 
                pairs.get(i).available
            );
            current.setNext(next);
            current = next;
        }
    }

    public ChangeResult makeChange(int targetAmount) {
        if (targetAmount <= 0) {
            return ChangeResult.failure("Target amount must be positive", targetAmount);
        }

        List<ChangeResult.CoinCount> coinsUsed = new ArrayList<>();
        return firstHandler.handle(targetAmount, coinsUsed);
    }

    private static class DenominationSupply {
        final int denomination;
        final int available;

        DenominationSupply(int denomination, int available) {
            this.denomination = denomination;
            this.available = available;
        }
    }

    public List<Integer> getDenominations() {
        return new ArrayList<>(denominations);
    }

    public boolean isUnlimitedSupply() {
        return unlimitedSupply;
    }
}

