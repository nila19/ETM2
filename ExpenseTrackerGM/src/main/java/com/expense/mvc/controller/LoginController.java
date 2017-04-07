package com.expense.mvc.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.expense.mvc.model.ui.DKey;
import com.expense.mvc.model.ui.DataKeyUI;
import com.expense.mvc.model.ui.User;
import com.expense.mvc.service.LoginService;
import com.expense.utils.Props;
import com.expense.utils.Utils;

@Controller
@RequestMapping("/access")
public class LoginController {
	private static final Logger logger = LogManager.getLogger("log." + LoginController.class);

	@Autowired
	private MessageSource messages;

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/in", method = RequestMethod.POST)
	public String login(@ModelAttribute("user") User user, Model model, HttpSession session) {
		// logger.info("Logging in :: " + user);
		if (!loginService.canConnect(model, session)) {
			return Utils.LOGIN;
		}

		try {
			boolean validlogin = false;
			if (StringUtils.equalsIgnoreCase(Props.aLogin, Props.ALogin.YES.aLogin)) {
				validlogin = true;
			} else {
				validlogin = loginService.checkLogin(user.getLogin(), user.getPassword());
			}

			if (validlogin) {
				user.setAuthenticated(true);
				session.setAttribute("login_user", user);
				session.setAttribute("login_datakeys", loginService.getAllDataKeys());

				DataKeyUI dataKey = loginService.getDefaultDataKey();
				setAttributes(session, dataKey);

				return "redirect:../entry/page";
			} else {
				model.addAttribute("message", Utils.getMsg(messages, "login.invalid"));
				model.addAttribute("error", "Y");
			}
			return Utils.LOGIN;
		} catch (Exception e) {
			model.addAttribute("error", "Y");
			return Utils.logout(LoginController.logger, model, e, messages);
		}
	}

	private void setAttributes(HttpSession session, DataKeyUI dataKey) {
		session.setAttribute("login_datakey", dataKey);
		session.setAttribute("login_accounts", loginService.getAllActiveAccounts(dataKey.getDataKey()));
		session.setAttribute("login_accounts_all", loginService.getAllAccounts(dataKey.getDataKey()));
		session.setAttribute("login_categories", loginService.getAllCategories(dataKey.getDataKey()));
		session.setAttribute("login_entrymonths", loginService.getAllEntryMonths(dataKey.getDataKey()));
		session.setAttribute("login_transmonths", loginService.getAllTransMonths(dataKey.getDataKey()));
		session.setAttribute("login_descriptions", loginService.getAllDescription(dataKey.getDataKey()));
	}

	@RequestMapping(value = "/out")
	public String logout(Model model, HttpSession session) {
		LoginController.logger.info("Logging out :: " + session.getAttribute("login_user"));
		session.invalidate();
		return Utils.LOGIN;
	}

	@RequestMapping(value = "/datakey", method = RequestMethod.POST)
	public String changeDataKey(@ModelAttribute("user2") DKey user2, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.LOGIN;
		}
		try {
			DataKeyUI dataKey = loginService.getDataKeyById(user2.getdKey());
			setAttributes(session, dataKey);
			LoginController.logger.info("DataKey changed... :: " + dataKey);
			return "redirect:../entry/page";
		} catch (Exception e) {
			LoginController.logger.error("DataKey change failed.. :: ", e);
			return Utils.logout(LoginController.logger, model, e, messages);
		}
	}
}
