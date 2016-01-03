package com.expense.mvc.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.expense.mvc.model.dao.AccountDAO;
import com.expense.mvc.model.dao.BillDAO;
import com.expense.mvc.model.dao.CategoryDAO;
import com.expense.mvc.model.dao.DataKeyDAO;
import com.expense.mvc.model.dao.TransactionDAO;
import com.expense.mvc.model.entity.Account;
import com.expense.mvc.model.entity.Bill;
import com.expense.mvc.model.entity.Category;
import com.expense.mvc.model.entity.DataKey;
import com.expense.mvc.model.ui.AccountUI;
import com.expense.mvc.model.ui.BillUI;
import com.expense.mvc.model.ui.CategoryUI;
import com.expense.mvc.model.ui.DataKeyUI;
import com.expense.mvc.model.ui.DescriptionUI;
import com.expense.mvc.model.ui.User;
import com.expense.utils.FormatUtils;
import com.expense.utils.Props;
import com.expense.utils.Utils;

@Service
public class LoginService {
	private static final Logger logger = LogManager.getLogger("log." + LoginService.class);

	@Autowired
	private DataKeyDAO dataKeyDAO;

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

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean canConnect(Model model, HttpSession session) {
		boolean valid = true;
		try {
			accountDAO.findAll();
		} catch (Exception e) {
			LoginService.logger.error("System Error...", e);
			if (model != null) {
				model.addAttribute("message", Utils.getMsg(messages, "system.db"));
				model.addAttribute("error", "Y");
			}
			valid = false;
		}
		return valid;
	}

	public boolean checkLogin(String uid, String pwd) {
		if (StringUtils.equalsIgnoreCase(uid, Props.expense.getString("UID"))
				&& StringUtils.equalsIgnoreCase(pwd, Props.expense.getString("PWD"))) {
			return true;
		}
		return false;
	}

	public boolean isLoggedIn(Model model, HttpSession session) {
		User user = (User) session.getAttribute("login_user");
		if (user != null && user.isAuthenticated()) {
			return true;
		} else {
			if (model != null) {
				model.addAttribute("message", Utils.getMsg(messages, "login.expired"));
			}
			return false;
		}
	}

	public DataKeyUI getDataKeyUI(HttpSession session) {
		return (DataKeyUI) session.getAttribute("login_datakey");
	}

	public int getDataKey(HttpSession session) {
		return ((DataKeyUI) session.getAttribute("login_datakey")).getDataKey();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public DataKeyUI getDefaultDataKey() {
		return new DataKeyUI(dataKeyDAO.findDefault());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public DataKeyUI getDataKeyById(int dataKey) {
		return new DataKeyUI(dataKeyDAO.findById(dataKey));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<DataKeyUI> getAllDataKeys() {
		List<DataKey> dkeys = dataKeyDAO.findAll();

		List<DataKeyUI> uis = new ArrayList<DataKeyUI>();
		for (DataKey dkey : dkeys) {
			uis.add(new DataKeyUI(dkey));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<AccountUI> getAllActiveAccounts(int dataKey) {
		List<Account> accts = accountDAO.findAllActive(dataKey);

		List<AccountUI> uis = new ArrayList<AccountUI>();
		for (Account acct : accts) {
			uis.add(new AccountUI(acct));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<AccountUI> getAllAccounts(int dataKey) {
		List<Account> accts = accountDAO.findAll(dataKey);

		List<AccountUI> uis = new ArrayList<AccountUI>();
		for (Account acct : accts) {
			uis.add(new AccountUI(acct));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<AccountUI> getAccountUI(int dataKey, int acctId) {
		List<Account> accts = accountDAO.find(dataKey, acctId);

		List<AccountUI> uis = new ArrayList<AccountUI>();
		for (Account acct : accts) {
			uis.add(new AccountUI(acct));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Account getAccount(int dataKey, int acctId) {
		Account acct = accountDAO.findById(acctId);

		if (acct.getLastBill() != null) {
			acct.getLastBill().getBillId();
		}
		if (acct.getOpenBill() != null) {
			acct.getOpenBill().getBillId();
		}

		return acct;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<CategoryUI> getAllCategories(int dataKey) {
		List<Category> cats = categoryDAO.findAllActive(dataKey);

		List<CategoryUI> uis = new ArrayList<CategoryUI>();
		for (Category cat : cats) {
			uis.add(new CategoryUI(cat));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Date> getAllEntryMonths(int dataKey) {
		return toDateList(transactionDAO.findAllEntryMonths(dataKey));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Date> getAllTransMonths(int dataKey) {
		return toDateList(transactionDAO.findAllTransMonths(dataKey));
	}

	private List<Date> toDateList(List<String> strs) {
		List<Date> dates = new ArrayList<Date>();
		for (String str : strs) {
			try {
				dates.add(FormatUtils.yyyyMMdd.parse(str));
			} catch (ParseException e) {
				dates.add(new Date(0));
			}
		}
		return dates;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<DescriptionUI> getAllDescription(int dataKey) {
		List<String> descs = transactionDAO.findAllDescription(dataKey);

		List<DescriptionUI> uis = new ArrayList<DescriptionUI>();
		for (String desc : descs) {
			uis.add(new DescriptionUI(desc));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<BillUI> getAllOpenBills(int dataKey) {
		List<Bill> bills = billDAO.findAllOpen(dataKey);

		List<BillUI> uis = new ArrayList<BillUI>();
		for (Bill bill : bills) {
			uis.add(new BillUI(bill));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Bill getBill(int dataKey, int billId) {
		Bill bill = billDAO.findById(billId);
		// Initialize the lazy initialized lists.
		bill.getTransForFromBill().size();
		bill.getTransForToBill().size();
		return bill;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveBill(Bill bill) {
		billDAO.save(bill);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveAccount(Account account) {
		accountDAO.save(account);
	}
}
