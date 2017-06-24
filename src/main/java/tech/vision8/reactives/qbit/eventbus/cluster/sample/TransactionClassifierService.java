package tech.vision8.reactives.qbit.eventbus.cluster.sample;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.annotation.Listen;
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
import tech.vision8.reactives.qbit.eventbus.cluster.sample.domain.ClassifyTransactionEvent;

/**
 * Sample service that uses events for executing the business logic, instead of being a REST service.
 *
 * @author vision8
 * @implNote Created on 2017-06-24.
 */
@RequestMapping("/")
public class TransactionClassifierService extends BaseService {
	
	private final EventManager eventManager;
	
	
	public TransactionClassifierService(
			final EventManager eventManager, final String statKeyPrefix,
			final Reactor reactor, final Timer timer, final StatsCollector statsCollector) {
		
		super(statKeyPrefix, reactor, timer, statsCollector);
		this.eventManager = eventManager;
		reactor.addServiceToFlush(eventManager);
	}
	
	@POST("/classify")
	public void receiveEvent(ClassifyTransactionEvent event) {
		
		eventManager.sendArguments("ClassifyTransactionEvent", event);
	}
	
	@Listen("ClassifyTransactionEvent")
	public void consumeClassifyTransactionEvent(ClassifyTransactionEvent event) {
		
		System.out.println("\n------------------------------------------------------");
		System.out.printf("\n>>> consumeClassifyTransactionEvent > received event:\n%s\n", event);
		System.out.println("------------------------------------------------------\n");
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
