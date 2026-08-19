package com.project.qampus.model;

import com.project.qampus.model.enums.VoteType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"}),
                                            @UniqueConstraint(columnNames = {"user_id", "answer_id"})})

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Vote {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
