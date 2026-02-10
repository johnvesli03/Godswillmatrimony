package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.DatabaseSequence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public long getNextSequence(String seqName) {
        DatabaseSequence sequence = entityManager.find(
                DatabaseSequence.class,
                seqName,
                LockModeType.PESSIMISTIC_WRITE
        );

        if (sequence == null) {
            sequence = new DatabaseSequence();
            sequence.setId(seqName);
            sequence.setSeq(1);
            entityManager.persist(sequence);
            return 1;
        }

        sequence.setSeq(sequence.getSeq() + 1);
        entityManager.merge(sequence);
        return sequence.getSeq();
    }
}