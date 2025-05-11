package com.time_tracker.be.repository;

import com.time_tracker.be.model.TimeSessionModel;
import com.time_tracker.be.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSessionRepository extends JpaRepository<TimeSessionModel, Long> {
    @Query("SELECT ts FROM TimeSessionModel ts WHERE ts.idUser = :idUser")
    List<TimeSessionModel> findByIdUser(@Param("idUser") UserModel idUser);

    @Query("SELECT ts FROM TimeSessionModel ts WHERE ts.idUser = :idUser ORDER BY ts.startTime DESC LIMIT 1")
    TimeSessionModel findLatestByIdUser(@Param("idUser") UserModel idUser);


}
