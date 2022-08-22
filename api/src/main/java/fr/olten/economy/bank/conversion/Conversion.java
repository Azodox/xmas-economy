package fr.olten.economy.bank.conversion;

import fr.olten.economy.bank.BalanceType;

public interface Conversion {

    double convert(BalanceType from, BalanceType to, double amount);

}
