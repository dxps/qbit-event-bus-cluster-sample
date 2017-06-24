package tech.vision8.reactives.qbit.eventbus.cluster.sample.app;

import tech.vision8.reactives.qbit.eventbus.cluster.sample.TransactionClassifierService;

/**
 *
 * @author vision8
 * @implNote Created on 2017-06-24.
 */
public class TransactionClassifierServiceInstance2 {
	
	public static void main(String... args) {
		
		TransactionClassifierService.run(8883, 8884);
	}
	
}
