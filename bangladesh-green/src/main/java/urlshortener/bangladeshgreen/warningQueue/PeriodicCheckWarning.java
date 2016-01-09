package urlshortener.bangladeshgreen.warningQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import urlshortener.bangladeshgreen.domain.URIAvailable;
import urlshortener.bangladeshgreen.repository.URIAvailableRepository;

import java.util.Date;
import java.util.List;

/**
 * Class for periodic check of the shorten URIs
 * It's scheduled and when it runs, obtains all the outdated links from the DB, checking all again.
 * It checks the URIs by inserting again the URIs in the queue.
 */
public class PeriodicCheckWarning {
	// Interval that sets when a URI has to be checked again (1/4 h)
	private final long interval = 250;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private URIAvailableRepository warningRepository;

	// One hour of delay (for checking "all" URIs)
	@Scheduled(fixedDelay = 4200000L)
	public void send() {
		Date now = new Date();
		now.setTime(now.getTime()-interval);
		List<URIAvailable> list = warningRepository.findByDateLessThan(now.getTime());
		for(URIAvailable uri : list) {
			this.rabbitTemplate.convertAndSend("warningQueue",uri.getTarget());
		}
	}

}