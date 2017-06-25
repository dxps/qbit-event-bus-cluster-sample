package tech.vision8.reactives.qbit.eventbus.cluster.sample.app;

import tech.vision8.reactives.qbit.eventbus.cluster.sample.services.TransactionClassifierService;

/**
 *
 * @author vision8
 * @since 2017-06-24
 */
public class TransactionClassifierServiceInstance1 {
	
	public static void main(String... args) {
		
		TransactionClassifierService.run(8881, 8882);
	}
	
}
