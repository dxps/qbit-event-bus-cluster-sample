package tech.vision8.reactives.qbit.eventbus.cluster.sample.domain;

/**
 * "ClassifyTransaction" domain event.
 *
 * @author vision8
 * @implNote Created on 2017-06-24.
 */
public class ClassifyTransactionEvent {

	private Transaction transaction;
	
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
