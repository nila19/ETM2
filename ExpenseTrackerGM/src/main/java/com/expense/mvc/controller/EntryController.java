package com.expense.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expense.mvc.model.entity.DataKey;
import com.expense.mvc.model.ui.BillUI;
import com.expense.mvc.model.ui.DataKeyUI;
import com.expense.mvc.model.ui.DescriptionUI;
import com.expense.mvc.model.ui.TransactionUI;
import com.expense.mvc.service.EntryService;
import com.expense.mvc.service.LoginService;
import com.expense.mvc.service.TallyService;
import com.expense.utils.AjaxBean;
import com.expense.utils.Errors;
import com.expense.utils.JsonUtils;
import com.expense.utils.Utils;

@Controller
@RequestMapping("/entry")
public class EntryController {
	private static final Logger logger = LogManager.getLogger("log." + EntryController.class);

	@Autowired
	private MessageSource messages;

	@Autowired
	private LoginService loginService;

	@Autowired
	private EntryService entryService;

	@Autowired
	private TallyService tallyService;

	@RequestMapping(value = "/page")
	public String page(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}
		return "entry";
	}

	@RequestMapping(value = "/pageexp")
	public String pageexp(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}
		return "entryexp";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@ModelAttribute("tui") TransactionUI tui, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			DataKeyUI ui = loginService.getDataKeyUI(session);
			// Update only if DataKey is active.
			if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
				entryService.addExpense(tui, ui.getDataKey());
				EntryController.logger.info("Expense added... :: " + tui);
				return JsonUtils.toJson(new AjaxBean(Errors.OK, Utils.getMsg(messages, "exp.add.ok")));
			} else {
				return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
			}
		} catch (Exception e) {
			EntryController.logger.error("Expense entry failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "exp.add.fail")));
		}
	}

	@RequestMapping(value = "/tally/{acctId}", method = RequestMethod.POST)
	@ResponseBody
	public String tally(@PathVariable int acctId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			DataKeyUI ui = loginService.getDataKeyUI(session);
			if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
				tallyService.tallyAccount(acctId);
				EntryController.logger.info("Account tallied... :: " + acctId);
				return JsonUtils.toJson(new AjaxBean(Errors.OK, Utils.getMsg(messages, "acc.tally.ok")));
			} else {
				return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
			}
		} catch (Exception e) {
			EntryController.logger.error("Account tally failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "acc.tally.fail")));
		}
	}

	@RequestMapping(value = "/acbalance")
	@ResponseBody
	public String acbalance(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			return JsonUtils.toJson(
					new AjaxBean(Errors.OK, "", loginService.getAllActiveAccounts(loginService.getDataKey(session))));
		} catch (Exception e) {
			EntryController.logger.error("Account acbalance failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@RequestMapping(value = "/acbalance/{acctId}")
	@ResponseBody
	public String acbalance(@PathVariable int acctId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			return JsonUtils.toJson(
					new AjaxBean(Errors.OK, "", loginService.getAccountUI(loginService.getDataKey(session), acctId)));
		} catch (Exception e) {
			EntryController.logger.error("Account acbalance failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@RequestMapping(value = "/pfbalance")
	@ResponseBody
	public String pfbalance(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			Map<String, String> pfBalance = tallyService.calcPFBalance(loginService.getDataKey(session));
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", pfBalance));
		} catch (Exception e) {
			EntryController.logger.error("Account pfbalance failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/desc")
	@ResponseBody
	public String getAllDescription(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			List<DescriptionUI> uis = (List<DescriptionUI>) session.getAttribute("login_descriptions");
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", uis));
		} catch (Exception e) {
			EntryController.logger.error("Account getAllDescription failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@RequestMapping(value = "/trans")
	@ResponseBody
	public String trans(@RequestParam("acctId") int acctId, @RequestParam("pending") String pending,
			@RequestParam("billId") int billId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			List<TransactionUI> tuis = tallyService.getTransactions(acctId, BooleanUtils.toBoolean(pending), billId,
					loginService.getDataKey(session));
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", tuis));
		} catch (Exception e) {
			EntryController.logger.error("Account trans failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@RequestMapping(value = "/get/{transId}")
	@ResponseBody
	public String get(@PathVariable int transId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		TransactionUI ui = entryService.getById(transId);
		return JsonUtils.toJson(new AjaxBean(Errors.OK, "", ui));
	}

	@RequestMapping(value = "/getBill/{billId}")
	@ResponseBody
	public String getBill(@PathVariable int billId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		BillUI ui = entryService.getBillById(billId);
		return JsonUtils.toJson(new AjaxBean(Errors.OK, "", ui));
	}

	@RequestMapping(value = "/getBillsforAc/{accId}")
	@ResponseBody
	public String getBillsforAc(@PathVariable int accId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		List<BillUI> uis = entryService.getBillsforAc(loginService.getDataKey(session), accId);
		return JsonUtils.toJson(new AjaxBean(Errors.OK, "", uis));
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ResponseBody
	public String modify(@ModelAttribute("tui") TransactionUI tui, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			DataKeyUI ui = loginService.getDataKeyUI(session);
			if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
				if (entryService.modifyExpense(tui.getTransId(), tui)) {
					EntryController.logger.info("Expense modified... :: " + tui);
					return JsonUtils.toJson(new AjaxBean(Errors.OK, Utils.getMsg(messages, "exp.modify.ok")));
				} else {
					return JsonUtils
							.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "exp.modify.inactive")));
				}
			} else {
				return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
			}
		} catch (Exception e) {
			EntryController.logger.error("Expense modification failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "exp.modify.fail")));
		}
	}

	@RequestMapping(value = "/delete/{transId}")
	@ResponseBody
	public String delete(@PathVariable int transId, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			DataKeyUI ui = loginService.getDataKeyUI(session);
			if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
				entryService.deleteExpense(transId);
				EntryController.logger.info("Expense deleted... :: " + transId);
				return JsonUtils.toJson(new AjaxBean(Errors.OK, Utils.getMsg(messages, "exp.delete.ok")));
			} else {
				return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
			}
		} catch (Exception e) {
			EntryController.logger.error("Expense deletion failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "exp.delete.fail")));
		}
	}

	@RequestMapping(value = "/swapTransSeq/{transId_1}/{transId_2}")
	@ResponseBody
	public String swapTransSeq(@PathVariable int transId_1, @PathVariable int transId_2, Model model,
			HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			DataKeyUI ui = loginService.getDataKeyUI(session);
			if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
				entryService.swapTransSeq(transId_1, transId_2);
				EntryController.logger.info("Expense seq swapped... :: " + transId_1 + ", " + transId_2);
				return JsonUtils.toJson(new AjaxBean(Errors.OK, Utils.getMsg(messages, "exp.seqswap.ok")));
			} else {
				return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
			}
		} catch (Exception e) {
			EntryController.logger.error("Expense seq swap failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "exp.seqswap.fail")));
		}
	}

	@RequestMapping(value = "/paybill", method = RequestMethod.POST)
	@ResponseBody
	public String paybill(@ModelAttribute("tui") TransactionUI tui, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			DataKeyUI ui = loginService.getDataKeyUI(session);
			if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
				entryService.payBill(tui, ui.getDataKey());
				EntryController.logger.info("Bill Paid... :: " + tui);
				return JsonUtils.toJson(new AjaxBean(Errors.OK, Utils.getMsg(messages, "bill.pay.ok")));
			} else {
				return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
			}
		} catch (Exception e) {
			EntryController.logger.error("Bill pay failed.. :: ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "bill.pay.fail")));
		}
	}
}
