package com.mateus.proposta_app.repository;

import com.mateus.proposta_app.entity.Proposals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposals, Long> {

    List<Proposals> findAllByIntegradaIsFalse();
}
