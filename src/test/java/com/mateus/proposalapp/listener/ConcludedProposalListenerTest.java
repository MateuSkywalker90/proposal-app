package com.mateus.proposalapp.listener;

import com.mateus.proposalapp.dto.ProposalResponseDto;
import com.mateus.proposalapp.entity.Proposal;
import com.mateus.proposalapp.repository.ProposalRepository;
import com.mateus.proposalapp.service.WebSocketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConcludedProposalListenerTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private WebSocketService webSocketService;

    @Test
    void isRegisteredAsSpringBeanSoRabbitListenerIsActive() {
        // Without @Component, Spring never creates this class and the
        // @RabbitListener on it never receives concluded proposals.
        assertThat(ConcludedProposalListener.class.isAnnotationPresent(Component.class)).isTrue();
    }

    @Test
    void updatesProposalAndPushesResultToWebSocket() {
        Proposal concluded = new Proposal();
        concluded.setId(1L);
        concluded.setRequestedAmount(10000.0);
        concluded.setApproved(true);
        concluded.setObservation("Approved");

        new ConcludedProposalListener(proposalRepository, webSocketService).concludedProposal(concluded);

        verify(proposalRepository).updateResult(1L, true, "Approved");
        verify(webSocketService).notify(any(ProposalResponseDto.class));
    }
}
