package com.bank.homeloan.restapi.app.servicei;

import java.util.List;

import com.bank.homeloan.restapi.app.model.CustomerDetails;
import com.bank.homeloan.restapi.app.model.Ledger;

public interface CustomerServiceI {

	List<CustomerDetails> getAllSanction();

	List<CustomerDetails> getAllAccepted();

	List<CustomerDetails> getAllRejected();

	List<CustomerDetails> getAllDisburse();

	CustomerDetails updateLedger(Integer customerDetailsId);

	List<Ledger> getLedger(Integer customerDetailsId);

	Ledger savedata(String emist ,Ledger led);

	List<Ledger> getLedgerDataWithStatus(Integer customerDetailsId, String status);

	Integer countdefaulter(Integer customerDetailsId, String status);

	


}
