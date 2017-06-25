package tech.vision8.reactives.qbit.eventbus.cluster.sample.domain;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * "ClassifyTransaction" domain event.
 *
 * @author vision8
 * @since 2017-06-24
 */
public class ClassifyTransactionEvent {

	private Transaction transaction;
	
	
	/** Create a new event. */
	public ClassifyTransactionEvent(String fromAccount, String toAccount, double amount, String currency) {
		
		this.transaction = new Transaction(fromAccount, toAccount,
				BigDecimal.valueOf(amount), Currency.getInstance(currency));
	}
	
	
	/** Create a new event. */
	public ClassifyTransactionEvent(Transaction transaction) {
		this.transaction = transaction;
	}
	
	
	/** Get the transaction included in this event. */
	public Transaction transaction() {
		return transaction;
	}
	
	
	@Override
	public String toString() {
		return "ClassifyTransactionEvent{" +
				"transaction=" + transaction +
				'}';
	}
	
}
