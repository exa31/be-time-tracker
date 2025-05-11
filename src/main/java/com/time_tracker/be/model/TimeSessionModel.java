package com.time_tracker.be.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time_sessions")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeSessionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_time_session", insertable = false, updatable = false)
    private Long idTimeSession;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserModel idUser;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime; // Gunakan ZonedDateTime  untuk waktu mulai

    @Column(name = "end_time", nullable = true)
    private ZonedDateTime endTime;   // Gunakan ZonedDateTime  untuk waktu selesai

    @Transient
    private Duration duration; // Gunakan Duration untuk menghitung durasi

    @Column(name = "description")
    private String description;

    // Method untuk menghitung durasi
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }
}
