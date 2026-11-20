package com.mateus.proposalapp.repository;

import com.mateus.proposalapp.entity.Proposal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findAllByIntegratedIsFalse();

    @Transactional
    @Modifying
    @Query(value = "UPDATE proposals SET approved = :approved, observation = :observation WHERE id = :id", nativeQuery = true)
    void updateResult(Long id, boolean approved, String observation);
}
