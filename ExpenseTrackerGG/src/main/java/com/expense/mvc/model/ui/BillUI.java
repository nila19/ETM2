package com.expense.mvc.model.ui;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;

import com.expense.mvc.model.entity.Bill;
import com.expense.utils.FormatUtils;
import com.expense.utils.Props;
import com.expense.utils.Utils;

public class BillUI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int billId;
	private int accountId;
	private String accountDesc;
	private Date createdDt;
	private Date billDt;
	private Date dueDt;

	private double billAmt;
	private double billBalance;
	private Date billPaidDt;

	private int payTranId;
	private Character status;

	private String fbillId = "";
	private String fcreatedDt = "";
	private String fbillDt = "";
	private String fdueDt = "";
	private String fbillPaidDt = "";
	private String fbillAmt = "";
	private String fbillBalance = "";

	private boolean dueDateWarning = false;
	private boolean open = true;

	public BillUI() {
	}

	public BillUI(Bill bill) {
		Utils.copyBean(this, bill);
		setAccountId(bill.getAccount().getAccountId());
		setAccountDesc(bill.getAccount().getDescription());

		open = (status == Bill.Status.OPEN.status);

		if (bill.getPayTran() != null) {
			setPayTranId(bill.getPayTran().getTransId());
		}

		checkDueDateWarning();
	}

	private void checkDueDateWarning() {
		if (billBalance > 0 && dueDt != null) {
			int DUE_DATE_WARNING = Integer.valueOf(Props.expense.getString("DUE.DATE.WARNING"));
			Date now = Calendar.getInstance().getTime();
			now = DateUtils.addDays(now, DUE_DATE_WARNING);

			if (DateUtils.truncatedCompareTo(dueDt, now, Calendar.DATE) <= 0) {
				setDueDateWarning(true);
			}
		}
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
		fbillId = FormatUtils.N0000.format(billId);
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountDesc() {
		return accountDesc;
	}

	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
		fcreatedDt = FormatUtils.ddMMMyyhhmma.format(createdDt);
	}

	public Date getBillDt() {
		return billDt;
	}

	public void setBillDt(Date billDt) {
		this.billDt = billDt;
		fbillDt = FormatUtils.ddMMMyy.format(billDt);
	}

	public Date getDueDt() {
		return dueDt;
	}

	public void setDueDt(Date dueDt) {
		this.dueDt = dueDt;
		fdueDt = FormatUtils.ddMMMyy.format(dueDt);
	}

	public double getBillAmt() {
		return billAmt;
	}

	public void setBillAmt(double billAmt) {
		this.billAmt = billAmt;
		fbillAmt = FormatUtils.AMOUNT.format(this.billAmt);
	}

	public double getBillBalance() {
		return billBalance;
	}

	public void setBillBalance(double billBalance) {
		this.billBalance = billBalance;
		fbillBalance = FormatUtils.AMOUNT.format(this.billBalance);
	}

	public Date getBillPaidDt() {
		return billPaidDt;
	}

	public void setBillPaidDt(Date billPaidDt) {
		this.billPaidDt = billPaidDt;
		if (billPaidDt != null && billPaidDt.getTime() > 0) {
			fbillPaidDt = FormatUtils.ddMMMyy.format(billPaidDt);
		}
	}

	public int getPayTranId() {
		return payTranId;
	}

	public void setPayTranId(int payTranId) {
		this.payTranId = payTranId;
	}

	public String getFbillId() {
		return fbillId;
	}

	public void setFbillId(String fbillId) {
		this.fbillId = fbillId;
	}

	public String getFcreatedDt() {
		return fcreatedDt;
	}

	public void setFcreatedDt(String fcreatedDt) {
		this.fcreatedDt = fcreatedDt;
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

	public String getFbillPaidDt() {
		return fbillPaidDt;
	}

	public void setFbillPaidDt(String fbillPaidDt) {
		this.fbillPaidDt = fbillPaidDt;
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

	public boolean isDueDateWarning() {
		return dueDateWarning;
	}

	public void setDueDateWarning(boolean dueDateWarning) {
		this.dueDateWarning = dueDateWarning;
	}

	// Custom methods.
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
