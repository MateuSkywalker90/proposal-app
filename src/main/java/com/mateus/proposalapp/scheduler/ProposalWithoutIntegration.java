package com.mateus.proposalapp.scheduler;

import com.mateus.proposalapp.entity.Proposal;
import com.mateus.proposalapp.repository.ProposalRepository;
import com.mateus.proposalapp.service.NotificationRabbitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ProposalWithoutIntegration {

    private final ProposalRepository proposalRepository;

    private final NotificationRabbitService notificationRabbitService;

    private final String exchange;

    private final Logger logger = LoggerFactory.getLogger(ProposalWithoutIntegration.class);

    public ProposalWithoutIntegration(ProposalRepository proposalRepository,
                                      NotificationRabbitService notificationRabbitService,
                                      @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationRabbitService = notificationRabbitService;
        this.exchange = exchange;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void findNotIntegratedProposals() {
        proposalRepository.findAllByIntegratedIsFalse().forEach(proposal -> {
            try {
                notificationRabbitService.notify(proposal, exchange);
                markAsIntegrated(proposal);
            } catch (RuntimeException ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    private void markAsIntegrated(Proposal proposal) {
        proposal.setIntegrated(true);
        proposalRepository.save(proposal);
    }
}
