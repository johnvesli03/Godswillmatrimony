package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.DatabaseSequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long getNextSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                new Query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );

        // upsert(true) should ensure it's never null, but keeping safe fallback
        return (counter == null) ? 1 : counter.getSeq();
    }
}