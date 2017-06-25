package tech.vision8.reactives.qbit.eventbus.cluster.sample.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Logback converter to get the thread id in the logs.
 *
 */
public class ThreadIdConverter extends ClassicConverter {
	
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	private static final ThreadLocal<String> threadId = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			int nextId = nextId();
			return String.format("%d", nextId);
		}
	};
	
	private static int nextId() {
		return nextId.incrementAndGet();
	}
	
	@Override
	public String convert(ILoggingEvent event) {
		return threadId.get();
	}
	
}
