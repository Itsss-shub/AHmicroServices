package com.bank.homeloan.restapi.app.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.homeloan.restapi.app.model.Ledger;

@Repository
public interface LedgerRepositoryI  extends JpaRepository<Ledger, Integer>{

	public List<Ledger> getByCustId(Integer custId);
	
	public List<Ledger> getByCustIdAndCurrentMonthEmiStatus(Integer custId, String currentMonthEmiStatus);
	
	@Query("SELECT COUNT(l.ledgerId) FROM Ledger l WHERE l.custId = :custId AND l.currentMonthEmiStatus = :currentMonthEmiStatus")
    public Integer countByCustIdAndCurrentMonthEmiStatus(@Param("custId") Integer custId, @Param("currentMonthEmiStatus") String currentMonthEmiStatus);
}
