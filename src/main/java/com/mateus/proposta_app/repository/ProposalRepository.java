package com.mateus.proposta_app.repository;

import com.mateus.proposta_app.entity.Proposals;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends CrudRepository<Proposals, Long> {

}
