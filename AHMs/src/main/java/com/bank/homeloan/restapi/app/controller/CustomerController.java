package com.bank.homeloan.restapi.app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.homeloan.restapi.app.model.CustomerDetails;
import com.bank.homeloan.restapi.app.model.Ledger;
import com.bank.homeloan.restapi.app.servicei.CustomerServiceI;


import lombok.extern.slf4j.Slf4j;




@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/bank/homeloan/restapi/customer")
public class CustomerController {
	
	@Autowired CustomerServiceI csi;
	

	@GetMapping("/sanction")
	public ResponseEntity<List<CustomerDetails>> allSanction(){
		List<CustomerDetails> aplist =csi.getAllSanction();
		log.info("all Application fetched with status : sanction");
		return new ResponseEntity<List<CustomerDetails>>(aplist,HttpStatus.OK);
	}
	
	@GetMapping("/accepted")
	public ResponseEntity<List<CustomerDetails>> allAccepted(){
		List<CustomerDetails> aplist =csi.getAllAccepted();
		log.info("all Application fetched with status : accepted");
		return new ResponseEntity<List<CustomerDetails>>(aplist,HttpStatus.OK);
	}
	
	@GetMapping("/rejected")
	public ResponseEntity<List<CustomerDetails>> allRejected(){
		List<CustomerDetails> aplist =csi.getAllRejected();
		log.info("all Application fetched with status : rejected");
		return new ResponseEntity<List<CustomerDetails>>(aplist,HttpStatus.OK);
	}
	
	

	@GetMapping("/disburse")
	public ResponseEntity<List<CustomerDetails>> allDisburse(){
		List<CustomerDetails> aplist =csi.getAllDisburse();
		log.info("all Application fetched with status : disburse");
		return new ResponseEntity<List<CustomerDetails>>(aplist,HttpStatus.OK);
	}
	
	
	@GetMapping("/updateLedger/{customerDetailsId}")
	public ResponseEntity<CustomerDetails> updateLedger(@PathVariable Integer customerDetailsId){
		CustomerDetails cus =  csi.updateLedger(customerDetailsId);
		log.info("ledger updated on the Id : "+customerDetailsId+"   updated Status : "+cus.getStatus());
	return new ResponseEntity<CustomerDetails>(cus,HttpStatus.OK);	
	}

	//emi pay
	@PostMapping("/payEMI/{emist}")
	public ResponseEntity<Ledger> savedata(@PathVariable String emist,@RequestBody Ledger led){
		Ledger aplist =csi.savedata(emist,led);
		log.info("all Application fetched with status : sanction");
		return new ResponseEntity<Ledger>(aplist,HttpStatus.OK);
	}
	//---------------------------
	
	// get All Ledger Data
	@GetMapping("/ledgerList/{customerDetailsId}")
	public ResponseEntity<List<Ledger>> getAllLedger(@PathVariable Integer customerDetailsId){
		List<Ledger> ledgList =  csi.getLedger(customerDetailsId);
		
	return new ResponseEntity<List<Ledger>>(ledgList,HttpStatus.OK);	
	}
	
	// get EMI paid/ unpaid
	@GetMapping("/EMITrackerWithStatus/{customerDetailsId}/{status}")
	public ResponseEntity<List<Ledger>> getWithStatusLedger(@PathVariable Integer customerDetailsId, @PathVariable String status){
		List<Ledger> ledgList =  csi.getLedgerDataWithStatus(customerDetailsId, status);
		
	return new ResponseEntity<List<Ledger>>(ledgList,HttpStatus.OK);	
	}
	
	
	@GetMapping("/getDefaultcount/{customerDetailsId}/{status}")
	public ResponseEntity<Integer> countdefaulter(@PathVariable Integer customerDetailsId, @PathVariable String status){
		Integer count =  csi.countdefaulter(customerDetailsId, status);
		
	return new ResponseEntity<Integer>(count,HttpStatus.OK);	
	}
	
	
}
