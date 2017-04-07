package com.expense.mvc.model.ui;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

import com.expense.mvc.model.entity.Transaction;
import com.expense.utils.FormatUtils;
import com.expense.utils.Utils;

public class TransactionUI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int transId = 0;
	private int categoryId;
	private String mainCategory = "";
	private String subCategory = "";
	private Date entryDate;
	private Date entryMonth;
	private String description = "";
	private double amount;
	private Date transDate;
	private Date transMonth;
	private int fromAccountId;
	private String fromAccountDesc = "";
	private int toAccountId;
	private String toAccountDesc = "";
	private char adhocInd;
	private char adjustInd;
	private double fromBalanceAf;
	private double toBalanceAf;
	private double fromBalanceBf;
	private double toBalanceBf;
	private Integer transSeq;
	private Character tallyInd;
	private Date tallyDate;
	private Character status;

	private int fromBillId;
	private Date fromBillDt;
	private int toBillId;
	private Date toBillDt;

	private char flag;

	public enum FLAG {
		ADHOC('H'), ADJUST('J');

		public char ind;

		private FLAG(char ind) {
			this.ind = ind;
		}
	}

	private String fAmount = "";
	private String fEntryDate = "";
	private String fTransDate = "";
	private String f2TransDate = "";
	private String fFromBalanceAf = "";
	private String fToBalanceAf = "";
	private String fFromBalanceBf = "";
	private String fToBalanceBf = "";
	private String fTallyDate = "";
	private String fFromBillDt = "";
	private String fToBillDt = "";

	@SuppressWarnings("unused")
	private String strTransMonth = "";
	@SuppressWarnings("unused")
	private String strEntryMonth = "";

	public TransactionUI() {
	}

	public TransactionUI(Transaction t) {
		Utils.copyBean(this, t);
		if (t.getCategory() != null) {
			categoryId = t.getCategory().getCategoryId();
			mainCategory = t.getCategory().getMainCategory();
			subCategory = t.getCategory().getSubCategory();
		}

		calculateFlag();

		setEntryDate(t.getEntryDate());
		setEntryMonth(t.getEntryMonth());
		setTransDate(t.getTransDate());
		setTransMonth(t.getTransMonth());
		setAmount(t.getAmount());
		setFromBalanceAf(t.getFromBalanceAf());
		setToBalanceAf(t.getToBalanceAf());
		setTallyDate(t.getTallyDate());

		fromAccountId = t.getFromAccount().getAccountId();
		fromAccountDesc = t.getFromAccount().getDescription();
		toAccountId = t.getToAccount().getAccountId();
		toAccountDesc = t.getToAccount().getDescription();

		if (t.getFromBill() != null) {
			fromBillId = t.getFromBill().getBillId();
			fromBillDt = t.getFromBill().getBillDt();
		}

		if (t.getToBill() != null) {
			toBillId = t.getToBill().getBillId();
			toBillDt = t.getToBill().getBillDt();
		}
	}

	public void calculateFlag() {
		if (adhocInd == 'Y') {
			flag = FLAG.ADHOC.ind;
		} else if (adjustInd == 'Y') {
			flag = FLAG.ADJUST.ind;
		}
	}

	public int getTransId() {
		return transId;
	}

	public void setTransId(int transId) {
		this.transId = transId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
		fAmount = FormatUtils.AMOUNT.format(this.amount);
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		try {
			this.entryDate = FormatUtils.yyyyMMddHHmmss.parse(FormatUtils.yyyyMMddHHmmss.format(entryDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		fEntryDate = FormatUtils.ddMMMyyhhmma.format(this.entryDate);
	}

	public Date getEntryMonth() {
		return entryMonth;
	}

	public void setEntryMonth(Date entryMonth) {
		try {
			this.entryMonth = FormatUtils.yyyyMMdd.parse(FormatUtils.yyyyMMdd.format(entryMonth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
		fTransDate = FormatUtils.ddMMM.format(this.transDate);
		f2TransDate = FormatUtils.yyyyMMdd.format(this.transDate);
	}

	public Date getTransMonth() {
		return transMonth;
	}

	public void setTransMonth(Date transMonth) {
		this.transMonth = transMonth;
	}

	public int getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(int fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getFromAccountDesc() {
		return fromAccountDesc;
	}

	public void setFromAccountDesc(String fromAccountDesc) {
		this.fromAccountDesc = fromAccountDesc;
	}

	public int getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(int toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getToAccountDesc() {
		return toAccountDesc;
	}

	public void setToAccountDesc(String toAccountDesc) {
		this.toAccountDesc = toAccountDesc;
	}

	public char getAdhocInd() {
		return adhocInd;
	}

	public void setAdhocInd(char adhocInd) {
		this.adhocInd = adhocInd;
	}

	public char getAdjustInd() {
		return adjustInd;
	}

	public void setAdjustInd(char adjustInd) {
		this.adjustInd = adjustInd;
	}

	public String getDescription() {
		return WordUtils.capitalize(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getFromBalanceAf() {
		return fromBalanceAf;
	}

	public void setFromBalanceAf(double fromBalanceAf) {
		this.fromBalanceAf = fromBalanceAf;
		setfFromBalanceAf(FormatUtils.AMOUNT.format(this.fromBalanceAf));
	}

	public double getToBalanceAf() {
		return toBalanceAf;
	}

	public void setToBalanceAf(double toBalanceAf) {
		this.toBalanceAf = toBalanceAf;
		setfToBalanceAf(FormatUtils.AMOUNT.format(this.toBalanceAf));
	}

	public double getFromBalanceBf() {
		return fromBalanceBf;
	}

	public void setFromBalanceBf(double fromBalanceBf) {
		this.fromBalanceBf = fromBalanceBf;
		setfFromBalanceBf(FormatUtils.AMOUNT.format(this.fromBalanceBf));
	}

	public double getToBalanceBf() {
		return toBalanceBf;
	}

	public void setToBalanceBf(double toBalanceBf) {
		this.toBalanceBf = toBalanceBf;
		setfToBalanceBf(FormatUtils.AMOUNT.format(this.toBalanceBf));
	}

	public Integer getTransSeq() {
		return transSeq;
	}

	public void setTransSeq(Integer transSeq) {
		this.transSeq = transSeq;
	}

	public Character getTallyInd() {
		return tallyInd;
	}

	public void setTallyInd(Character tallyInd) {
		this.tallyInd = tallyInd;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getTallyDate() {
		return tallyDate;
	}

	public void setTallyDate(Date tallyDate) {
		try {
			this.tallyDate = FormatUtils.yyyyMMddHHmmss.parse(FormatUtils.yyyyMMddHHmmss.format(tallyDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		fTallyDate = FormatUtils.ddMMMyyhhmma.format(this.tallyDate);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getStrTransDate() {
		if (getTransDate() != null) {
			try {
				return FormatUtils.yyyyMMdd.format(getTransDate());
			} catch (Exception e) {
			}
		}
		return null;
	}

	public void setStrTransDate(String strTransDate) {
		try {
			setTransDate(FormatUtils.yyyyMMdd.parse(strTransDate));
		} catch (Exception e) {
		}
	}

	public String getStrTransMonth() {
		if (getTransMonth() != null) {
			try {
				return FormatUtils.yyyyMM.format(getTransMonth());
			} catch (Exception e) {
			}
		}
		return null;
	}

	public void setStrTransMonth(String strTransMonth) {
		this.strTransMonth = strTransMonth;
		try {
			setTransMonth(FormatUtils.yyyyMM.parse(strTransMonth));
		} catch (Exception e) {
		}
	}

	public String getStrEntryMonth() {
		if (getEntryMonth() != null) {
			try {
				return FormatUtils.yyyyMM.format(getEntryMonth());
			} catch (Exception e) {
			}
		}
		return null;
	}

	public void setStrEntryMonth(String strEntryMonth) {
		this.strEntryMonth = strEntryMonth;
		try {
			setEntryMonth(FormatUtils.yyyyMM.parse(strEntryMonth));
		} catch (Exception e) {
		}
	}

	public String getFAmount() {
		return fAmount;
	}

	public String getFEntryDate() {
		return fEntryDate;
	}

	public String getFTransDate() {
		return fTransDate;
	}

	public String getCategory() {
		return mainCategory + " ~ " + subCategory;
	}

	public String getF2TransDate() {
		return f2TransDate;
	}

	public void setF2TransDate(String f2TransDate) {
		this.f2TransDate = f2TransDate;
	}

	public String getfFromBalanceAf() {
		return fFromBalanceAf;
	}

	public void setfFromBalanceAf(String fFromBalanceAf) {
		this.fFromBalanceAf = fFromBalanceAf;
	}

	public String getfToBalanceAf() {
		return fToBalanceAf;
	}

	public void setfToBalanceAf(String fToBalanceAf) {
		this.fToBalanceAf = fToBalanceAf;
	}

	public String getfFromBalanceBf() {
		return fFromBalanceBf;
	}

	public void setfFromBalanceBf(String fFromBalanceBf) {
		this.fFromBalanceBf = fFromBalanceBf;
	}

	public String getfToBalanceBf() {
		return fToBalanceBf;
	}

	public void setfToBalanceBf(String fToBalanceBf) {
		this.fToBalanceBf = fToBalanceBf;
	}

	public String getfTallyDate() {
		return fTallyDate;
	}

	public void setfTallyDate(String fTallyDate) {
		this.fTallyDate = fTallyDate;
	}

	public char getFlag() {
		return flag;
	}

	public void setFlag(char flag) {
		this.flag = flag;
	}

	public int getFromBillId() {
		return fromBillId;
	}

	public void setFromBillId(int fromBillId) {
		this.fromBillId = fromBillId;
	}

	public Date getFromBillDt() {
		return fromBillDt;
	}

	public void setFromBillDt(Date fromBillDt) {
		this.fromBillDt = fromBillDt;
		fFromBillDt = FormatUtils.ddMMM.format(this.fromBillDt);
	}

	public int getToBillId() {
		return toBillId;
	}

	public void setToBillId(int toBillId) {
		this.toBillId = toBillId;
	}

	public Date getToBillDt() {
		return toBillDt;
	}

	public void setToBillDt(Date toBillDt) {
		this.toBillDt = toBillDt;
		fToBillDt = FormatUtils.ddMMM.format(this.toBillDt);
	}

	public String getfAmount() {
		return fAmount;
	}

	public void setfAmount(String fAmount) {
		this.fAmount = fAmount;
	}

	public String getfEntryDate() {
		return fEntryDate;
	}

	public void setfEntryDate(String fEntryDate) {
		this.fEntryDate = fEntryDate;
	}

	public String getfTransDate() {
		return fTransDate;
	}

	public void setfTransDate(String fTransDate) {
		this.fTransDate = fTransDate;
	}

	public String getfFromBillDt() {
		return fFromBillDt;
	}

	public void setfFromBillDt(String fFromBillDt) {
		this.fFromBillDt = fFromBillDt;
		try {
			setFromBillDt(FormatUtils.ddMMM.parse(fFromBillDt));
		} catch (Exception e) {
		}
	}

	public String getfToBillDt() {
		return fToBillDt;
	}

	public void setfToBillDt(String fToBillDt) {
		this.fToBillDt = fToBillDt;
		try {
			setToBillDt(FormatUtils.ddMMM.parse(fToBillDt));
		} catch (Exception e) {
		}
	}
}
