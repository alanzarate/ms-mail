package com.ucb.edu.msmail.entity;
import java.sql.Time;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private  Integer eventId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "initial_date", nullable = true)
    private Timestamp initialDate = null;

    @Column(name = "final_date", nullable = true)
    private Timestamp finalDate = null;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "semester_id", nullable = false)
    private Integer semesterId;

    @Column(name="advice_id", nullable = true)
    private Integer adviceId = null;


}
