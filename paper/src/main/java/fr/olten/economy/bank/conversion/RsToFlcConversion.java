package fr.olten.economy.bank.conversion;

import fr.olten.economy.bank.BalanceType;
import org.jetbrains.annotations.Range;

public class RsToFlcConversion implements Conversion {

    private static final int CONVERSION_RATE = 2000000;
    private static final int RS_MIN = 15;

    @Override
    public double convert(BalanceType from, BalanceType to, @Range(from = RS_MIN, to = Integer.MAX_VALUE) double amount) {
        if(from == BalanceType.FLC && to == BalanceType.RS) {
            return -1;
        }

        if(from == BalanceType.RS && to == BalanceType.FLC) {
            return amount * CONVERSION_RATE;
        }

        return 0;
    }
}
