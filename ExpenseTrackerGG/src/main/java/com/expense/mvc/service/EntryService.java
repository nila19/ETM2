package com.expense.mvc.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.expense.mvc.helper.BillCloser;
import com.expense.mvc.model.dao.AccountDAO;
import com.expense.mvc.model.dao.BillDAO;
import com.expense.mvc.model.dao.CategoryDAO;
import com.expense.mvc.model.dao.TransactionDAO;
import com.expense.mvc.model.entity.Account;
import com.expense.mvc.model.entity.Bill;
import com.expense.mvc.model.entity.Transaction;
import com.expense.mvc.model.ui.BillUI;
import com.expense.mvc.model.ui.TransactionUI;
import com.expense.utils.FormatUtils;
import com.expense.utils.Utils;

@Service
public class EntryService {

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private CategoryDAO categoryDAO;

	@Autowired
	private TransactionDAO transactionDAO;

	@Autowired
	private BillDAO billDAO;

	@Autowired
	private MessageSource messages;

	@Transactional(propagation = Propagation.REQUIRED)
	public int addExpense(TransactionUI ui, int dataKey) {
		Transaction t = new Transaction();
		t.setDataKey(dataKey);
		t.setEntryDate(new Date());
		t.setEntryMonth(DateUtils.truncate(new Date(), Calendar.MONTH));
		t.setTallyInd(Transaction.Tally.NO.status);
		t.setStatus(Transaction.Status.POSTED.status);
		copyTransFields(ui, t);

		Account fr = t.getFromAccount();
		Account to = t.getToAccount();
		if (fr.doesBills() && fr.getOpenBill() != null) {
			t.setFromBill(fr.getOpenBill());
		}
		if (to.doesBills() && to.getOpenBill() != null) {
			t.setToBill(to.getOpenBill());
		}

		transactionDAO.save(t);

		DecimalFormat df = FormatUtils.AMOUNT_NOCOMMA;

		// Set From/To accounts' 'BEFORE' balances before cash movement.
		if (t.getFromAccount().getAccountId() != 0) {
			t.setFromBalanceBf(Double.valueOf(df.format(t.getFromAccount().getBalanceAmt())));
		}
		if (t.getToAccount().getAccountId() != 0) {
			t.setToBalanceBf(Double.valueOf(df.format(t.getToAccount().getBalanceAmt())));
		}

		moveCash(t, t.getAmount());

		// Set From/To accounts' 'AFTER' balances after cash movement.
		if (t.getFromAccount().getAccountId() != 0) {
			t.setFromBalanceAf(Double.valueOf(df.format(t.getFromAccount().getBalanceAmt())));
		}
		if (t.getToAccount().getAccountId() != 0) {
			t.setToBalanceAf(Double.valueOf(df.format(t.getToAccount().getBalanceAmt())));
		}

		t.setTransSeq(t.getTransId());
		transactionDAO.save(t);
		return t.getTransId();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean modifyExpense(int transId, TransactionUI ui) {
		Transaction t = transactionDAO.findById(transId);
		Account from = t.getFromAccount();
		Account to = t.getToAccount();

		// Financial Impact - Identify if there is any change in Amount or Accounts.
		// Check Amount / FromAccount / ToAccount has changed.
		boolean finImpact = false;
		if (t.getAmount() != ui.getAmount() || from.getAccountId() != ui.getFromAccountId()
				|| to.getAccountId() != ui.getToAccountId()) {
			finImpact = true;
		}

		// If Financial Impact : for ADJUST both Accounts should be Active, else just FromAccount
		// should be Active.
		if (finImpact) {
			if (t.getAdjustInd() == Transaction.Adjust.YES.type) {
				if (from.getAccountId() != 0 && !from.isActive()) {
					return false;
				}
				if (to.getAccountId() != 0 && !to.isActive()) {
					return false;
				}
			} else if ((t.getAdjustInd() == Transaction.Adjust.NO.type) && !from.isActive()) {
				return false;
			}
		}

		// Move Cash only if there is any Financial impact.
		// Reverse the previous cash movement.
		if (finImpact) {
			t.setTallyInd(Transaction.Tally.NO.status);
			moveCash(t, t.getAmount() * -1);
		}

		copyTransFields(ui, t);
		transactionDAO.save(t);

		// Move Cash only if there is any Financial impact.
		if (finImpact) {
			moveCash(t, t.getAmount());
			// Fix the From/To Before/After amounts.
			adjustTransBalances(t);
		}
		return true;
	}

	// Find previous trans for the same Fr/To accounts to get the Ac balance at that time period.
	private void adjustTransBalances(Transaction t) {
		// Setting FromAccount Bf/Af balances
		int fr = t.getFromAccount().getAccountId();
		if (fr == 0) {
			t.setFromBalanceBf(0);
			t.setFromBalanceAf(0);
		} else {
			List<Transaction> allFr = transactionDAO.findByAccount(t.getDataKey(), fr, false, 0);
			for (Transaction t2 : allFr) {
				if (t2.getTransSeq() < t.getTransSeq()) {
					if (fr == t2.getFromAccount().getAccountId()) {
						t.setFromBalanceBf(t2.getFromBalanceAf());
					}
					if (fr == t2.getToAccount().getAccountId()) {
						t.setFromBalanceBf(t2.getToBalanceAf());
					}
					double transamt = (t.getFromAccount().getType() == Account.Type.CASH.type) ? t.getAmount() : t
							.getAmount() * -1;
					t.setFromBalanceAf(t.getFromBalanceBf() - transamt);
					break;
				}
			}
		}

		// Setting ToAccount Bf/Af balances
		int to = t.getToAccount().getAccountId();
		if (to == 0) {
			t.setToBalanceBf(0);
			t.setToBalanceAf(0);
		} else {
			List<Transaction> allTo = transactionDAO.findByAccount(t.getDataKey(), to, false, 0);
			for (Transaction t2 : allTo) {
				if (t2.getTransSeq() < t.getTransSeq()) {
					if (to == t2.getFromAccount().getAccountId()) {
						t.setToBalanceBf(t2.getFromBalanceAf());
					}
					if (to == t2.getToAccount().getAccountId()) {
						t.setToBalanceBf(t2.getToBalanceAf());
					}
					double transamt = (t.getToAccount().getType() == Account.Type.CASH.type) ? t.getAmount() : t
							.getAmount() * -1;
					t.setToBalanceAf(t.getToBalanceBf() + transamt);
					break;
				}
			}
		}

		transactionDAO.save(t);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteExpense(int transId) {
		Transaction t = transactionDAO.findById(transId);

		// Reverse the previous cash movement.
		moveCash(t, t.getAmount() * -1);

		if (t.getFromAccount().getAccountId() != 0) {
			t.getFromAccount().getTransForFromAccount().remove(t);
		}
		if (t.getToAccount().getAccountId() != 0) {
			t.getToAccount().getTransForToAccount().remove(t);
		}

		// TODO If bill is already closed, bill-balance is not getting adjusted...
		transactionDAO.delete(t);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public TransactionUI getById(int transId) {
		Transaction t = transactionDAO.findById(transId);
		return new TransactionUI(t);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public BillUI getBillById(int billId) {
		Bill b = billDAO.findById(billId);
		return new BillUI(b);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<BillUI> getBillsforAc(int dataKey, int accId) {
		List<Bill> bills = billDAO.findForAcct(dataKey, accId);

		List<BillUI> uis = new ArrayList<BillUI>();
		for (Bill bill : bills) {
			// Open Bills do not have bill amt & balance populated.
			if (bill.isOpen()) {
				double amt = BillCloser.calcBillAmt(bill);
				bill.setBillAmt(amt);
				bill.setBillBalance(amt);
			}
			uis.add(new BillUI(bill));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int payBill(TransactionUI ui, int dataKey) {
		int billId = ui.getToBillId();
		Bill bill = billDAO.findById(billId);

		ui.setAdjustInd(Transaction.Adjust.YES.type);
		ui.setAdhocInd(Transaction.Adhoc.NO.type);
		ui.setToAccountId(bill.getAccount().getAccountId());
		ui.setCategoryId(0);
		ui.setDescription(Utils.getMsg(messages, "billpay.description"));
		ui.setAmount(bill.getBillBalance());

		int tranId = addExpense(ui, dataKey);
		Transaction t = transactionDAO.findById(tranId);
		t.setToBill(bill);
		transactionDAO.save(t);

		bill.setBillBalance(bill.getBillBalance() - t.getAmount());
		bill.setBillPaidDt(t.getTransDate());
		bill.setPayTran(t);
		billDAO.save(bill);

		return t.getTransId();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void swapTransSeq(int transId_1, int transId_2) {
		Transaction t1 = transactionDAO.findById(transId_1);
		Transaction t2 = transactionDAO.findById(transId_2);

		int seq1 = t1.getTransSeq();
		int seq2 = t2.getTransSeq();

		// Always Oldest trans first.
		if (seq1 < seq2) {
			adjustBalances(t1, t2);
		} else {
			adjustBalances(t2, t1);
		}

		t1.setTransSeq(seq2);
		t2.setTransSeq(seq1);

		transactionDAO.save(t1);
		transactionDAO.save(t2);
	}

	private void adjustBalances(Transaction t1, Transaction t2) {
		HashMap<Integer, Double> bf = new HashMap<Integer, Double>();
		HashMap<Integer, Double> af = new HashMap<Integer, Double>();

		int t1fr = t1.getFromAccount().getAccountId();
		int t1to = t1.getToAccount().getAccountId();
		int t2fr = t2.getFromAccount().getAccountId();
		int t2to = t2.getToAccount().getAccountId();

		bf.put(t1fr, t1.getFromBalanceBf());
		af.put(t1fr, t1.getFromBalanceAf());
		bf.put(t1to, t1.getToBalanceBf());
		af.put(t1to, t1.getToBalanceAf());
		bf.put(t2fr, t2.getFromBalanceBf());
		af.put(t2fr, t2.getFromBalanceAf());
		bf.put(t2to, t2.getToBalanceBf());
		af.put(t2to, t2.getToBalanceAf());

		// Reverse the trans. #2 is the latest one.
		replayTrans(bf, af, t2, true);
		replayTrans(bf, af, t1, true);

		// Redo the trans from the reverse order.
		replayTrans(bf, af, t2, false);

		t2.setFromBalanceBf(bf.get(t2fr));
		t2.setFromBalanceAf(af.get(t2fr));
		t2.setToBalanceBf(bf.get(t2to));
		t2.setToBalanceAf(af.get(t2to));

		replayTrans(bf, af, t1, false);

		t1.setFromBalanceBf(bf.get(t1fr));
		t1.setFromBalanceAf(af.get(t1fr));
		t1.setToBalanceBf(bf.get(t1to));
		t1.setToBalanceAf(af.get(t1to));
	}

	private void replayTrans(HashMap<Integer, Double> bf, HashMap<Integer, Double> af, Transaction t, boolean reverse) {
		Account a1 = t.getFromAccount();
		Account a2 = t.getToAccount();

		double amt = reverse ? t.getAmount() * -1 : t.getAmount();

		if (a1.getAccountId() != 0) {
			double bf_amt = bf.get(a1.getAccountId());
			bf_amt = (a1.getType() == Account.Type.CASH.type) ? bf_amt - amt : bf_amt + amt;
			bf.put(a1.getAccountId(), bf_amt);

			double af_amt = af.get(a1.getAccountId());
			af_amt = (a1.getType() == Account.Type.CASH.type) ? af_amt - amt : af_amt + amt;
			af.put(a1.getAccountId(), af_amt);
		}

		if (a2.getAccountId() != 0) {
			double bf_amt = af.get(a2.getAccountId());
			bf_amt = (a2.getType() == Account.Type.CASH.type) ? bf_amt + amt : bf_amt - amt;
			af.put(a2.getAccountId(), bf_amt);

			double af_amt = af.get(a2.getAccountId());
			af_amt = (a2.getType() == Account.Type.CASH.type) ? af_amt + amt : af_amt - amt;
			af.put(a2.getAccountId(), af_amt);
		}
	}

	// ********************************************************************************************
	// ************************************** Common Methods **************************************

	private void copyTransFields(TransactionUI ui, Transaction t) {
		t.setTransDate(ui.getTransDate());
		t.setTransMonth(DateUtils.truncate(ui.getTransDate(), Calendar.MONTH));
		t.setDescription(WordUtils.capitalize(ui.getDescription()));
		t.setAmount(ui.getAmount());

		t.setAdjustInd(ui.getAdjustInd());
		if (ui.getAdjustInd() != Transaction.Adjust.YES.type) {
			t.setAdjustInd(Transaction.Adjust.NO.type);
		}

		t.setAdhocInd(ui.getAdhocInd());
		if (ui.getAdhocInd() != Transaction.Adhoc.YES.type) {
			t.setAdhocInd(Transaction.Adhoc.NO.type);
		}

		t.setCategory(categoryDAO.findById(ui.getCategoryId()));
		t.setFromAccount(accountDAO.findById(ui.getFromAccountId()));
		t.setToAccount(accountDAO.findById(ui.getToAccountId()));

		t.setFromBill(t.getFromAccount().doesBills() ? billDAO.findById(ui.getFromBillId()) : null);
		t.setToBill(t.getToAccount().doesBills() ? billDAO.findById(ui.getToBillId()) : null);
		// TODO If bill is already closed, bill-balance is not getting adjusted...
	}

	private void moveCash(Transaction t, double amount) {
		if (t.getFromAccount().getAccountId() != 0) {
			updateAccount(t.getFromAccount(), amount * -1, t);
		}
		if (t.getToAccount().getAccountId() != 0) {
			updateAccount(t.getToAccount(), amount, t);
		}
	}

	private void updateAccount(Account ac, double amount, Transaction t) {
		if (ac.getType() == Account.Type.CREDIT.type) {
			amount = amount * -1;
		}

		ac.setBalanceAmt(Double.valueOf(FormatUtils.AMOUNT_NOCOMMA.format(ac.getBalanceAmt() + amount)));
		accountDAO.save(ac);

		// Find all future trans post this & adjust the ac balance.
		// If Seq = null, it is an ADD. Don't do this for ADD, as Add does not have future trans.
		if (t.getTransSeq() != null) {
			updateTransItemBalances(ac, amount, t);
		}
	}

	private void updateTransItemBalances(Account ac, double amount, Transaction t) {
		for (Transaction ft : ac.getTransForFromAccount()) {
			if (ft.getTransSeq() >= t.getTransSeq()) {
				ft.setFromBalanceBf(ft.getFromBalanceBf() + amount);
				ft.setFromBalanceAf(ft.getFromBalanceAf() + amount);
				transactionDAO.save(ft);
			}
		}
		for (Transaction ft : ac.getTransForToAccount()) {
			if (ft.getTransSeq() >= t.getTransSeq()) {
				ft.setToBalanceBf(ft.getToBalanceBf() + amount);
				ft.setToBalanceAf(ft.getToBalanceAf() + amount);
				transactionDAO.save(ft);
			}
		}
	}
}
