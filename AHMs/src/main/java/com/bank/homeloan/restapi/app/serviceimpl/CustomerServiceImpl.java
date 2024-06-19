package com.bank.homeloan.restapi.app.serviceimpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bank.homeloan.restapi.app.exception.CustomerDetailsApplictionNotFoundException;
import com.bank.homeloan.restapi.app.model.CustomerAccountDetails;
import com.bank.homeloan.restapi.app.model.CustomerDetails;
import com.bank.homeloan.restapi.app.model.Ledger;
import com.bank.homeloan.restapi.app.model.SanctionLetter;
import com.bank.homeloan.restapi.app.repository.AccountRepositoryI;
import com.bank.homeloan.restapi.app.repository.CustomerRepo;
import com.bank.homeloan.restapi.app.repository.LedgerRepositoryI;
import com.bank.homeloan.restapi.app.servicei.CustomerServiceI;

@Service
public class CustomerServiceImpl implements CustomerServiceI{

	@Autowired CustomerRepo cri;
	
	@Autowired LedgerRepositoryI lri;
	
	@Autowired AccountRepositoryI ari;
	
    @Autowired private JavaMailSender jmsender;
	
	@Autowired @Value("$spring.mail.username") String myMail;

	@Override
	public List<CustomerDetails> getAllSanction() {
		List<CustomerDetails> app = cri.findByStatus("sanction");
		if(!app.isEmpty()) {
			
		return app;
		}
		else {
			
			throw new CustomerDetailsApplictionNotFoundException("No Application found on this Status : pending");
		}
	}

	@Override
	public List<CustomerDetails> getAllAccepted() {
		List<CustomerDetails> app = cri.findByStatus("accepted");
		if(!app.isEmpty()) {
			
		return app;
		}
		else {
			
			throw new CustomerDetailsApplictionNotFoundException("No Application found on this Status : pending");
		}
	}

	@Override
	public List<CustomerDetails> getAllRejected() {
		List<CustomerDetails> app = cri.findByStatus("rejected");
		if(!app.isEmpty()) {
			
		return app;
		}
		else {
			
			throw new CustomerDetailsApplictionNotFoundException("No Application found on this Status : pending");
		}
	}

	@Override
	public List<CustomerDetails> getAllDisburse() {
		List<CustomerDetails> app = cri.findByStatus("disburse");
		if(!app.isEmpty()) {
			
		return app;
		}
		else {
			
			throw new CustomerDetailsApplictionNotFoundException("No Application found on this Status : pending");
		}
	}

	@Override
	public CustomerDetails updateLedger(Integer customerDetailsId) {
		Optional<CustomerDetails> appl = cri.findById(customerDetailsId);
		if(appl!=null) {
			CustomerDetails customer = appl.get();
			
			Ledger ledger = customer.getLedger();
			
			SanctionLetter sanctionLetter = customer.getSanctionLetter();
			
			CustomerAccountDetails account = customer.getCustomerAccountDetails();
			
			double accountBalance = account.getAccountBalance();
			Double sancbal = sanctionLetter.getLoanAmountSanctioned();
			double ammt = accountBalance+sancbal;
			
			account.setAccountBalance(ammt);
			
			//
			
			if ("accepted".equals(customer.getStatus())) {
                ledger.setLedgerCreateDate(new Date());
                ledger.setTotalLoanAmount(sanctionLetter.getLoanAmountSanctioned());
                ledger.setCustId(customerDetailsId);
                double totalAmount = sanctionLetter.getLoanAmountSanctioned();
                double rateOfInterest = sanctionLetter.getRateOfInterest();
                Integer loanTenure = sanctionLetter.getLoanTenure();

                double payableAmountWithInterest = totalAmount + (totalAmount * rateOfInterest * loanTenure) / 100;
                ledger.setPayableAmountWithInterest(payableAmountWithInterest);
                ledger.setTenure(loanTenure);

                double monthlyEMI = payableAmountWithInterest / (loanTenure * 12);
                ledger.setMonthlyEmi(monthlyEMI);
                ledger.setAmountPaidTillDate(monthlyEMI);
                ledger.setRemainingAmount(payableAmountWithInterest);

                Date today = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DAY_OF_MONTH, 4);
                Date fourDaysLater = calendar.getTime();

                System.out.println("Today's Date: " + today);
                System.out.println("Date after 4 days: " + fourDaysLater);

                ledger.setNextEmiDateStart(today);
                ledger.setNextEmiDateEnd(fourDaysLater);
                ledger.setDefaulterCount(0);
             
                ledger.setCurrentMonthEmiStatus("unpaid");

                Calendar calendar1 = Calendar.getInstance();
                calendar1.add(Calendar.YEAR, loanTenure); // Assuming 'loanTenure' is in years
                Date lastDateOfLoan = calendar1.getTime();

                System.out.println("Last date of the loan: " + lastDateOfLoan);

                ledger.setLoanEndDate(lastDateOfLoan);
                ledger.setLoanStatus("unpaid");
                
                customer.setStatus("disburse");
			}else {
				throw new CustomerDetailsApplictionNotFoundException("not accepted status found for application");
			}
			
			System.out.println(customer.getLedger());
			
		return cri.save(customer);
		}
		else {
			throw new CustomerDetailsApplictionNotFoundException("no application found on database on the Id : "+customerDetailsId);
		
		}
	
	}
	
	//---------------------------

	@Override
	public List<Ledger> getLedger(Integer customerDetailsId) {
		
		List<Ledger> byCustId = null;
		if(customerDetailsId != null) {
			
			 byCustId = lri.getByCustId(customerDetailsId);
		}
		else {
			throw new CustomerDetailsApplictionNotFoundException("no ledger found on this id : "+customerDetailsId);
		}
		
		return byCustId;
	}

	@Override
	public Ledger savedata(String emist,Ledger led) {
		Integer custId = led.getCustId();
		if(emist.equalsIgnoreCase("paid")) {
		
		Double emi = led.getMonthlyEmi();
		
		Optional<CustomerDetails> appl = cri.findById(custId);
		CustomerDetails cust = appl.get();
		CustomerAccountDetails account = cust.getCustomerAccountDetails();
		double accountBalance = account.getAccountBalance();
		double newbalnce= accountBalance-emi;
		account.setAccountBalance(newbalnce);
		ari.save(account);
		
		Ledger ledger = cust.getLedger();
		
		Double a1 = ledger.getAmountPaidTillDate();
		double xy=a1+emi;
		ledger.setAmountPaidTillDate(xy);
		
		Double rem = ledger.getRemainingAmount();
		ledger.setRemainingAmount(rem-emi);
		
		ledger.setCurrentMonthEmiStatus("paid");
		return lri.save(ledger);
		}
		else if(emist.equalsIgnoreCase("skip")) {
			Optional<CustomerDetails> appl = cri.findById(custId);
			CustomerDetails cust = appl.get();
			Ledger ledger = cust.getLedger();
			ledger.setCurrentMonthEmiStatus("paid");
			return lri.save(ledger);
		}
		else {
			throw new CustomerDetailsApplictionNotFoundException("no action done on the ledger");
		}
	}

	@Override
	public List<Ledger> getLedgerDataWithStatus(Integer customerDetailsId, String status) {
		// TODO Auto-generated method stub
		List<Ledger> byCustIdAndCurrentMonthEmiStatus = null;
		if(status.equalsIgnoreCase("paid") || status.equalsIgnoreCase("unpaid"))
		{
			byCustIdAndCurrentMonthEmiStatus = lri.getByCustIdAndCurrentMonthEmiStatus(customerDetailsId, status);
		}else {
			throw new CustomerDetailsApplictionNotFoundException("no ledger found on this id : "+customerDetailsId);
		}
			
		return byCustIdAndCurrentMonthEmiStatus;
	}

	@Override
	public Integer countdefaulter(Integer customerDetailsId, String status) {
		Integer count = null;
		if(status.equalsIgnoreCase("paid") || status.equalsIgnoreCase("unpaid"))
		{
			count = lri.countByCustIdAndCurrentMonthEmiStatus(customerDetailsId, status);
		}else {
			throw new CustomerDetailsApplictionNotFoundException("no ledger found on this id : "+customerDetailsId);
		}
			
		return count;
	}
   
}
