package tech.vision8.reactives.qbit.eventbus.cluster.sample;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.annotation.*;
import io.advantageous.qbit.annotation.http.GET;
import io.advantageous.qbit.annotation.http.POST;
import io.advantageous.qbit.eventbus.EventBusCluster;
import io.advantageous.qbit.eventbus.EventBusClusterBuilder;
import io.advantageous.qbit.events.EventManager;
import io.advantageous.qbit.reactive.Reactor;
import io.advantageous.qbit.reactive.ReactorBuilder;
import io.advantageous.qbit.service.BaseService;
import io.advantageous.qbit.service.stats.StatsCollector;
import io.advantageous.qbit.util.Timer;
import tech.vision8.reactives.qbit.eventbus.cluster.sample.domain.ClassifyTransactionEvent;
import tech.vision8.reactives.qbit.eventbus.cluster.sample.domain.Transaction;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Sample service that uses events for executing the business logic.<br/>
 * It uses a REST operation named "/classify" for getting the input.
 *
 * @author vision8
 * @since 2017-06-24
 */
@RequestMapping("/classify")
public class TransactionClassifierService extends BaseService {
	
	private final EventManager eventManager;
	
	
	public TransactionClassifierService(
			final EventManager eventManager, final String statKeyPrefix,
			final Reactor reactor, final Timer timer, final StatsCollector statsCollector) {
		
		super(statKeyPrefix, reactor, timer, statsCollector);
		this.eventManager = eventManager;
		reactor.addServiceToFlush(eventManager);
	}
	
	@RequestMapping("/txn/{0}/{1}/{2}/{3}")
	public void classifyAsGet(@PathVariable String fromAccount, @PathVariable String toAccount,
	                          @PathVariable double amount, @PathVariable String currency) {
		
		System.out.printf("\n[classifyAsGet] fromAccount='%s' toAccount='%s' amount=%f currency='%s'\n\n",
				fromAccount, toAccount, amount, currency);
		eventManager.sendArguments("ClassifyTransactionEvent",
				new ClassifyTransactionEvent(new Transaction(fromAccount, toAccount, BigDecimal.valueOf(amount), Currency.getInstance(currency))));
	}
	
	@POST("/txn")
	public void classifyAsPost(final Transaction transaction) {
		
		System.out.printf("\n[classifyAsPost] transaction=%s\n", transaction);
		eventManager.sendArguments("ClassifyTransactionEvent",
				new ClassifyTransactionEvent(transaction));
	}
	
	@Listen("ClassifyTransactionEvent")
	public void onClassifyTransactionEvent(ClassifyTransactionEvent event) {
		
		System.out.printf("\n[onClassifyTransactionEvent] %s\n\n", event.transaction());
	}
	
	
	public static void run(final int httpPort, final int replicatorPort) {
		
		final EventBusCluster eventBusCluster = EventBusClusterBuilder
				.eventBusClusterBuilder()
				.setEventBusName("event-bus")
				.setReplicationPortLocal(replicatorPort)
				.build();
		eventBusCluster.start();
		
		final ManagedServiceBuilder managedServiceBuilder = ManagedServiceBuilder
				.managedServiceBuilder().setRootURI("/")
				.setEventManager(eventBusCluster.eventManagerImpl())
				.setPort(httpPort);
		
		final TransactionClassifierService service = new TransactionClassifierService(
				eventBusCluster.createClientEventManager(),
				"event.",
				ReactorBuilder.reactorBuilder().build(),
				Timer.timer(),
				managedServiceBuilder.getStatServiceBuilder().buildStatsCollector());
		
		managedServiceBuilder.addEndpointService(service);
		
		managedServiceBuilder.getEndpointServerBuilder().build().startServerAndWait();
	}
	
}
