package com.expense.utils;

import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

public final class Utils {

	public static final String LOGIN = "login";

	public static String getMsg(MessageSource messages, String code) {
		return messages.getMessage(code, new Object[] {}, Locale.US);
	}

	public static String ajaxLoginExpired(MessageSource messages) {
		return JsonUtils.toJson(new AjaxBean(Errors.INPUT, Utils.getMsg(messages, "login.expired")));
	}

	public static void copyBean(Object dest, Object src) {
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String logout(Logger logger, Model model, Exception e, MessageSource messages) {
		logger.error("System Error :: ", e);
		model.addAttribute("message", Utils.getMsg(messages, "system.error"));
		model.addAttribute("error", "Y");
		return Utils.LOGIN;
	}
}
