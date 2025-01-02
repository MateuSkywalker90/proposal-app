package com.mateus.proposta_app.service;

import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.mapper.ProposalMapper;
import com.mateus.proposta_app.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;

    private final NotificationRabbitService notificationService;

    private final String exchange;

    public ProposalService(ProposalRepository proposalRepository,
                           NotificationRabbitService notificationRabbitService,
                           @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationRabbitService;
        this.exchange = exchange;
    }

    public ProposalResponseDto create(ProposalRequestDto requestDto) {
        Proposals proposals = ProposalMapper.INSTANCE.proposalToDto(requestDto);
        Proposals savedProposals = proposalRepository.save(proposals);

        notificationRabbitMQ(proposals);

        return ProposalMapper.INSTANCE.convertEntityToDto(savedProposals);
    }

    private void notificationRabbitMQ(Proposals proposals) {
        try {
            notificationService.notify(proposals, exchange);
        } catch (RuntimeException e) {
            //proposals.setIntegrada(false);
            proposalRepository.save(proposals);
        }

    }

    public List<ProposalResponseDto> getProposal() {
        return ProposalMapper.INSTANCE.convertListEntityToListDto(proposalRepository.findAll());
    }
}
