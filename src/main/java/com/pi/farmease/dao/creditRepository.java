package com.pi.farmease.dao;
import com.pi.farmease.entities.Credit ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;




@Repository
public interface creditRepository extends JpaRepository<Credit, Long> {
    @Query(value = "select * from credit order by date_demande desc",nativeQuery = true)
    List<Credit> getAllByDemand_Date() ;

    Credit findCreditsByIdCredit(long idCredit);

    @Query("SELECT c FROM Credit c WHERE c.user.id = :clientId AND c.Completed = true ORDER BY c.obtainingDate DESC")
    Credit getLatestCompletedCreditByClientId(@Param("clientId") Long clientId);


}

