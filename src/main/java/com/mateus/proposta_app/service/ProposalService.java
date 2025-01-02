package com.mateus.proposta_app.service;

import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.mapper.ProposalMapper;
import com.mateus.proposta_app.repository.ProposalRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private ProposalRepository proposalRepository;

    private NotificationService notificationService;

    private String exchange;

    public ProposalService(ProposalRepository proposalRepository,
                           NotificationService notificationService,
                           @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationService;
        this.exchange = exchange;
    }

    public ProposalResponseDto create(ProposalRequestDto requestDto) {
        Proposals proposals = ProposalMapper.INSTANCE.proposalToDto(requestDto);
        Proposals savedProposals = proposalRepository.save(proposals);

        ProposalResponseDto responseDto = ProposalMapper.INSTANCE.convertEntityToDto(savedProposals);
        notificationService.notify(responseDto, exchange);

        return responseDto;
    }

    public List<ProposalResponseDto> getProposal() {
        return ProposalMapper.INSTANCE.convertListEntityToListDto(proposalRepository.findAll());
    }
}
