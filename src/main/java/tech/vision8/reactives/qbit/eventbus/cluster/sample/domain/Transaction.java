package tech.vision8.reactives.qbit.eventbus.cluster.sample.domain;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A bare-bone financial transaction domain entity<br/>
 * (looking more like a Value Object).
 *
 * @author vision8
 * @implNote Created on 2017-06-24.
 */
public class Transaction {
	
	private final String fromAccountIBAN;
	
	private String toAccountIBAN;
	
	private BigDecimal amount;
	
	private Currency currency = Currency.getInstance("EUR");
	
	public Transaction(String fromAccountIBAN, String toAccountIBAN, BigDecimal amount) {
		this.fromAccountIBAN = fromAccountIBAN;
		this.toAccountIBAN = toAccountIBAN;
		this.amount = amount;
	}
	
	public Transaction(String fromAccountIBAN, String toAccountIBAN, BigDecimal amount, Currency currency) {
		this(fromAccountIBAN, toAccountIBAN, amount);
		this.currency = currency;
	}
	
	public String fromAccountIBAN() {
		return fromAccountIBAN;
	}
	
	public String toAccountIBAN() {
		return toAccountIBAN;
	}
	
	public BigDecimal amount() {
		return amount;
	}
	
	public Currency currency() {
		return currency;
	}
	
	@Override
	public String toString() {
		return "Transaction{" +
				"fromAccountIBAN='" + fromAccountIBAN + '\'' +
				", toAccountIBAN='" + toAccountIBAN + '\'' +
				", amount=" + amount +
				", currency=" + currency +
				'}';
	}
	
}
