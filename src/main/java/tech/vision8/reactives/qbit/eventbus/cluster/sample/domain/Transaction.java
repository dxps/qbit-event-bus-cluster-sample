package tech.vision8.reactives.qbit.eventbus.cluster.sample.domain;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A bare-bone financial transaction domain entity<br/>
 * (looking more like a Value Object).
 *
 * @author vision8
 * @since 2017-06-24
 */
public class Transaction {
	
	private final String fromAccount;
	
	private String toAccount;
	
	private BigDecimal amount;
	
	private Currency currency = Currency.getInstance("EUR");
	
	
	/** Crate a new transaction. */
	public Transaction(String fromAccount, String toAccount, BigDecimal amount) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
	}
	
	
	/** Crate a new transaction. */
	public Transaction(String fromAccount, String toAccount, BigDecimal amount, Currency currency) {
		this(fromAccount, toAccount, amount);
		this.currency = currency;
	}
	
	
	/** Get the source (debited) account of the transaction. */
	public String fromAccount() {
		return fromAccount;
	}
	
	
	/** Get the target(credited) account of the transaction. */
	public String toAccount() {
		return toAccount;
	}
	
	
	/** Get the amount of the transaction. */
	public BigDecimal amount() {
		return amount;
	}
	
	
	/** Get the currency of the transaction. */
	public Currency currency() {
		return currency;
	}
	
	
	@Override
	public String toString() {
		return String.format("Transaction{ fromAccount='%s', toAccount='%s', amount=%f, currency='%s' }",
				fromAccount, toAccount, amount, currency);
	}
	
}
