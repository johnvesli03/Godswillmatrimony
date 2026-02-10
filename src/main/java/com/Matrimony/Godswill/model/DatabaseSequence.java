package com.Matrimony.Godswill.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * NOTE:
 * This was used for MongoDB sequence generation.
 * For MySQL, you generally don't need this because @GeneratedValue handles IDs.
 * Keep it only if you truly need custom sequence logic.
 */
@Entity
@Table(name = "database_sequences")
@Data
public class DatabaseSequence {

    @Id
    @Column(length = 100)
    private String id;   // sequence name, e.g. "profile_sequence"

    private long seq;
}