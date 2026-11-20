package com.mateus.proposalapp.mapper;

import com.mateus.proposalapp.dto.ProposalRequestDto;
import com.mateus.proposalapp.dto.ProposalResponseDto;
import com.mateus.proposalapp.entity.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.NumberFormat;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.cpf", source = "cpf")
    @Mapping(target = "user.phone", source = "phone")
    @Mapping(target = "user.income", source = "income")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "integrated", constant = "false")
    @Mapping(target = "observation", ignore = true)
    Proposal toEntity(ProposalRequestDto proposalRequestDto);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "cpf", source = "user.cpf")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "income", source = "user.income")
    @Mapping(target = "requestedAmountFormatted", expression = "java(formatRequestedAmount(proposal))")
    ProposalResponseDto toDto(Proposal proposal);

    List<ProposalResponseDto> toDtoList(Iterable<Proposal> proposals);

    default String formatRequestedAmount(Proposal proposal) {
        return NumberFormat.getCurrencyInstance().format(proposal.getRequestedAmount());
    }
}
