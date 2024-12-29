package com.mateus.proposta_app.mapper;

import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.entity.Proposals;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    @Mapping(target = "users.firstName", source = "firstName")
    @Mapping(target = "users.lastName", source = "lastName")
    @Mapping(target = "users.cpf", source = "cpf")
    @Mapping(target = "users.phone", source = "phone")
    @Mapping(target = "users.income", source = "income")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "integrated", ignore = true)
    @Mapping(target = "observation", ignore = true)
    Proposals proposalToDto(ProposalRequestDto proposalRequestDto);

    @Mapping(target = "firstName", source = "users.firstName")
    @Mapping(target = "lastName", source = "users.lastName")
    @Mapping(target = "cpf", source = "users.cpf")
    @Mapping(target = "phone", source = "users.phone")
    @Mapping(target = "income", source = "users.income")
    ProposalResponseDto convertEntityToDto(Proposals proposals);
}
