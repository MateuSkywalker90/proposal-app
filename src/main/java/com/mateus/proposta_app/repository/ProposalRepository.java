package com.mateus.proposta_app.repository;

import com.mateus.proposta_app.entity.Proposals;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposals, Long> {

    List<Proposals> findAllByIntegradaIsFalse();

    @Transactional
    @Modifying
    @Query(value = "UPDATE proposals SET aprovada = :aprovada, observacao = :observacao WHERE id = :id", nativeQuery = true)
    void proposalUpdate(Long id, boolean aprovada, String observacao);
}
