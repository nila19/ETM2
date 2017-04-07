package com.expense.mvc.model.ui;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;

import com.expense.mvc.model.entity.Account;
import com.expense.mvc.model.entity.Bill;
import com.expense.mvc.model.entity.Transaction;
import com.expense.utils.FormatUtils;
import com.expense.utils.Props;
import com.expense.utils.Utils;

public class AccountUI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int accountId;
	private String description = "";
	private double tallyBalance;
	private Date tallyDate2;
	private double tallyExpenseAmt;
	private int tallyExpenseCnt;
	private double balanceAmt;
	private char type;
	private String imageCode;
	private char status;

	private char billOption;
	private int closingDay;
	private int dueDay;

	private String fbalanceAmt = "";
	private String ftallyExpenseAmt = "";
	private String ftallyBalance = "";
	private String ftallyDate = "";

	// Bill Information
	private boolean dueDtWarning = false;
	private Date billDt;
	private Date dueDt;
	private double billAmt;
	private double billBalance;
	private Date billPaidDt;
	private double openBillAmt;

	private String fbillDt = "";
	private String fdueDt = "";
	private String fbillAmt = "";
	private String fbillBalance = "";
	private String fbillPaidDt = "";
	private String fopenBillAmt = "";

	public AccountUI() {
	}

	public AccountUI(Account ac) {
		Utils.copyBean(this, ac);

		setBalanceAmt(ac.getBalanceAmt());
		setTallyBalance(ac.getTallyBalance());
		setTallyDate2(ac.getTallyDate());

		// TALLY COUNT, AMT
		setTallyExpenses(ac);

		setBillInfo(ac);
	}

	private void setBillInfo(Account ac) {
		if (ac.getLastBill() != null) {
			Bill lbill = ac.getLastBill();

			setBillDt(lbill.getBillDt());
			setDueDt(lbill.getDueDt());
			setBillAmt(lbill.getBillAmt());
			setBillBalance(lbill.getBillBalance());
			setBillPaidDt(lbill.getBillPaidDt());

			checkDueDateWarning(lbill);
		}

		if (ac.getOpenBill() != null) {
			setOpenBillAmt(ac.getOpenBill().getBillBalance());
		}
	}

	private void checkDueDateWarning(Bill lastBill) {
		if (lastBill.getBillBalance() > 0) {
			Date dueDt = lastBill.getDueDt();

			int DUE_DATE_WARNING = Integer.valueOf(Props.expense.getString("DUE.DATE.WARNING"));
			Date now = Calendar.getInstance().getTime();
			now = DateUtils.addDays(now, DUE_DATE_WARNING);

			if (DateUtils.truncatedCompareTo(dueDt, now, Calendar.DATE) <= 0) {
				setDueDtWarning(true);
			}
		}
	}

	private void setTallyExpenses(Account ac) {
		tallyExpenseCnt = 0;
		tallyExpenseAmt = 0;
		for (Transaction t : ac.getTransForFromAccount()) {
			if (!t.isTallied()) {
				tallyExpenseCnt++;
				tallyExpenseAmt += t.getAmount();
			}
		}
		for (Transaction t : ac.getTransForToAccount()) {
			if (!t.isTallied()) {
				tallyExpenseCnt++;
				tallyExpenseAmt -= t.getAmount();
			}
		}

		if (tallyExpenseAmt != 0 && ac.getType() == Account.Type.CASH.type) {
			tallyExpenseAmt = tallyExpenseAmt * -1;
		}

		setTallyExpenseAmt(tallyExpenseAmt);
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getBalanceAmt() {
		return balanceAmt;
	}

	public void setBalanceAmt(double balanceAmt) {
		this.balanceAmt = balanceAmt;
		fbalanceAmt = FormatUtils.AMOUNT.format(this.balanceAmt);
	}

	public double getTallyBalance() {
		return tallyBalance;
	}

	public void setTallyBalance(double tallyBalance) {
		this.tallyBalance = tallyBalance;
		ftallyBalance = FormatUtils.AMOUNT.format(this.tallyBalance);
	}

	public Date getTallyDate2() {
		return tallyDate2;
	}

	public void setTallyDate2(Date tallyDate) {
		tallyDate2 = tallyDate;
		ftallyDate = FormatUtils.ddMMMyyhhmma.format(tallyDate2);
	}

	public double getTallyExpenseAmt() {
		return tallyExpenseAmt;
	}

	public void setTallyExpenseAmt(double tallyExpenseAmt) {
		this.tallyExpenseAmt = tallyExpenseAmt;
		ftallyExpenseAmt = FormatUtils.AMOUNT.format(this.tallyExpenseAmt);
	}

	public int getTallyExpenseCnt() {
		return tallyExpenseCnt;
	}

	public void setTallyExpenseCnt(int tallyExpenseCnt) {
		this.tallyExpenseCnt = tallyExpenseCnt;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public char getBillOption() {
		return billOption;
	}

	public void setBillOption(char billOption) {
		this.billOption = billOption;
	}

	public int getClosingDay() {
		return closingDay;
	}

	public void setClosingDay(Integer closingDay) {
		this.closingDay = closingDay == null ? 0 : closingDay;
	}

	public int getDueDay() {
		return dueDay;
	}

	public void setDueDay(Integer dueDay) {
		this.dueDay = dueDay == null ? 0 : dueDay;
	}

	public String getFbalanceAmt() {
		return fbalanceAmt;
	}

	public void setFbalanceAmt(String fbalanceAmt) {
		this.fbalanceAmt = fbalanceAmt;
	}

	public String getFtallyExpenseAmt() {
		return ftallyExpenseAmt;
	}

	public void setFtallyExpenseAmt(String ftallyExpenseAmt) {
		this.ftallyExpenseAmt = ftallyExpenseAmt;
	}

	public String getFtallyBalance() {
		return ftallyBalance;
	}

	public void setFtallyBalance(String ftallyBalance) {
		this.ftallyBalance = ftallyBalance;
	}

	public String getFtallyDate() {
		return ftallyDate;
	}

	public void setFtallyDate(String ftallyDate) {
		this.ftallyDate = ftallyDate;
	}

	public boolean isActive() {
		return status == Account.Status.ACTIVE.status;
	}

	public boolean isBilled() {
		return billOption == Account.BillOption.YES.billOption;
	}

	public boolean isCash() {
		return type == Account.Type.CASH.type;
	}

	public boolean isDueDtWarning() {
		return dueDtWarning;
	}

	public void setDueDtWarning(boolean dueDtWarning) {
		this.dueDtWarning = dueDtWarning;
	}

	// Custom methods.
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Date getBillDt() {
		return billDt;
	}

	public void setBillDt(Date billDt) {
		this.billDt = billDt;
		fbillDt = FormatUtils.ddMMM.format(billDt);
	}

	public Date getDueDt() {
		return dueDt;
	}

	public void setDueDt(Date dueDt) {
		this.dueDt = dueDt;
		fdueDt = FormatUtils.ddMMM.format(dueDt);
	}

	public double getBillAmt() {
		return billAmt;
	}

	public void setBillAmt(double billAmt) {
		this.billAmt = billAmt;
		fbillAmt = FormatUtils.AMOUNT.format(billAmt);
	}

	public double getBillBalance() {
		return billBalance;
	}

	public void setBillBalance(double billBalance) {
		this.billBalance = billBalance;
		fbillBalance = FormatUtils.AMOUNT.format(billBalance);
	}

	public Date getBillPaidDt() {
		return billPaidDt;
	}

	public void setBillPaidDt(Date billPaidDt) {
		this.billPaidDt = billPaidDt;
		fbillPaidDt = FormatUtils.ddMMM.format(billPaidDt);
	}

	public double getOpenBillAmt() {
		return openBillAmt;
	}

	public void setOpenBillAmt(double openBillAmt) {
		this.openBillAmt = openBillAmt;
		fopenBillAmt = FormatUtils.AMOUNT.format(openBillAmt);
	}

	public String getFbillDt() {
		return fbillDt;
	}

	public void setFbillDt(String fbillDt) {
		this.fbillDt = fbillDt;
	}

	public String getFdueDt() {
		return fdueDt;
	}

	public void setFdueDt(String fdueDt) {
		this.fdueDt = fdueDt;
	}

	public String getFbillAmt() {
		return fbillAmt;
	}

	public void setFbillAmt(String fbillAmt) {
		this.fbillAmt = fbillAmt;
	}

	public String getFbillBalance() {
		return fbillBalance;
	}

	public void setFbillBalance(String fbillBalance) {
		this.fbillBalance = fbillBalance;
	}

	public String getFbillPaidDt() {
		return fbillPaidDt;
	}

	public void setFbillPaidDt(String fbillPaidDt) {
		this.fbillPaidDt = fbillPaidDt;
	}

	public String getFopenBillAmt() {
		return fopenBillAmt;
	}

	public void setFopenBillAmt(String fopenBillAmt) {
		this.fopenBillAmt = fopenBillAmt;
	}
}
