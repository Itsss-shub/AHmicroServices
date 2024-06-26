package com.bank.homeloan.restapi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.homeloan.restapi.app.model.CustomerAccountDetails;
@Repository
public interface AccountRepositoryI extends JpaRepository<CustomerAccountDetails, Integer>{

}
