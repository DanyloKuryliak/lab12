package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ATMTest {

    private ATM unlimitedATM;
    private ATM limitedATM;

    @BeforeEach
    void setUp() {
        int[] denominations = {100, 50, 25, 10, 5, 1};
        unlimitedATM = new ATM(denominations);

        int[] limitedDenominations = {50, 25, 10, 5, 1};
        int[] available = {2, 3, 5, 10, 20};
        limitedATM = new ATM(limitedDenominations, available);
    }

    @Test
    void testUnlimitedSupplySuccess() {
        ChangeResult result = unlimitedATM.makeChange(237);
        assertTrue(result.isSuccess());
        assertEquals(6, result.getTotalCoins());
        List<ChangeResult.CoinCount> coins = result.getCoins();
        assertEquals(4, coins.size());
    }

    @Test
    void testUnlimitedSupplyCannotMakeExactChange() {
        ATM atm = new ATM(new int[]{10, 5});
        ChangeResult result = atm.makeChange(23);
        assertFalse(result.isSuccess());
        assertEquals(3, result.getRemainingAmount());
    }

    @Test
    void testLimitedSupplyNotEnoughMoney() {
        ChangeResult result = limitedATM.makeChange(300);
        assertFalse(result.isSuccess());
        assertTrue(result.getRemainingAmount() > 0);
    }

    @Test
    void testLimitedSupplySuccess() {
        ChangeResult result = limitedATM.makeChange(200);
        assertTrue(result.isSuccess());
        assertTrue(result.getTotalCoins() > 0);
    }

    @Test
    void testInvalidAmount() {
        ChangeResult result = unlimitedATM.makeChange(-10);
        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorMessage());
    }
}
