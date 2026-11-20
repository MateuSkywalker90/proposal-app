package com.mateus.proposta_app.scheduler;

import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.repository.ProposalRepository;
import com.mateus.proposta_app.service.NotificationRabbitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProposalWithoutIntegrationTest {

    private static final String EXCHANGE = "proposta-pendente.ex";

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private NotificationRabbitService notificationRabbitService;

    @Test
    void republishesPendingProposalAndMarksItAsIntegrated() {
        Proposals pending = pendingProposal();
        when(proposalRepository.findAllByIntegradaIsFalse()).thenReturn(List.of(pending));

        scheduler().findNotIntegratedProposals();

        verify(notificationRabbitService).notify(pending, EXCHANGE);
        verify(proposalRepository).save(pending);
        assertThat(pending.isIntegrada()).isTrue();
    }

    @Test
    void leavesProposalPendingWhenBrokerIsStillDown() {
        Proposals pending = pendingProposal();
        when(proposalRepository.findAllByIntegradaIsFalse()).thenReturn(List.of(pending));
        doThrow(new AmqpException("broker unavailable"))
                .when(notificationRabbitService).notify(any(Proposals.class), eq(EXCHANGE));

        scheduler().findNotIntegratedProposals();

        verify(proposalRepository, never()).save(any(Proposals.class));
        assertThat(pending.isIntegrada()).isFalse();
    }

    private ProposalWithoutIntegration scheduler() {
        return new ProposalWithoutIntegration(proposalRepository, notificationRabbitService, EXCHANGE);
    }

    private Proposals pendingProposal() {
        Proposals proposal = new Proposals();
        proposal.setId(1L);
        proposal.setValorSolicitado(10000.0);
        proposal.setPrazoPagamento(24);
        proposal.setIntegrada(false);
        return proposal;
    }
}
