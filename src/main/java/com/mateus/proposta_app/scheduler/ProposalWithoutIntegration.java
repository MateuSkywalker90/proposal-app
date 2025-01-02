package com.mateus.proposta_app.scheduler;

import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.repository.ProposalRepository;
import com.mateus.proposta_app.service.NotificationRabbitService;
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
        proposalRepository.findAllByIntegradaIsFalse().forEach(proposals -> {
            try {
                notificationRabbitService.notify(proposals, exchange);
                updateProposal(proposals);
            } catch (RuntimeException ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    private void updateProposal(Proposals proposals) {
        proposals.setIntegrada(true);
        proposalRepository.save(proposals);
    }
}
