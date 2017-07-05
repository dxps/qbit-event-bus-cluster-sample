package tech.vision8.reactives.qbit.eventbus.cluster.sample.services;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.annotation.Listen;
import io.advantageous.qbit.annotation.PathVariable;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.http.POST;
import io.advantageous.qbit.eventbus.EventBusCluster;
import io.advantageous.qbit.eventbus.EventBusClusterBuilder;
import io.advantageous.qbit.events.EventManager;
import io.advantageous.qbit.reactive.Reactor;
import io.advantageous.qbit.reactive.ReactorBuilder;
import io.advantageous.qbit.service.BaseService;
import io.advantageous.qbit.service.stats.StatsCollector;
import io.advantageous.qbit.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.vision8.reactives.qbit.eventbus.cluster.sample.domain.Transaction;
import tech.vision8.reactives.qbit.eventbus.cluster.sample.domain.events.ClassifyTransactionEvent;

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
	
	private Logger logger = LoggerFactory.getLogger(TransactionClassifierService.class);
	
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
		
		logger.info("\n[classifyAsGet] fromAccount='{}' toAccount='{}' amount={} currency='{}'\n\n",
				fromAccount, toAccount, amount, currency);
		eventManager.sendArguments("ClassifyTransactionEvent",
				new ClassifyTransactionEvent(new Transaction(fromAccount, toAccount, BigDecimal.valueOf(amount), Currency.getInstance(currency))));
	}
	
	@POST("/txn")
	public void classifyAsPost(final Transaction transaction) {
		
		logger.info("\n[classifyAsPost] transaction={}\n", transaction);
		eventManager.sendArguments("ClassifyTransactionEvent",
				new ClassifyTransactionEvent(transaction));
	}
	
	@Listen("ClassifyTransactionEvent")
	public void onClassifyTransactionEvent(ClassifyTransactionEvent event) {
		
		logger.info("\n[onClassifyTransactionEvent] event.transaction={}\n\n", event.transaction());
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
