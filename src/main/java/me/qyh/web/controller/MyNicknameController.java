package me.qyh.web.controller;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.exception.LogicException;
import me.qyh.service.UserService;

@Controller
@RequestMapping(value = "my/nickname")
public class MyNicknameController extends BaseController {

	@Value("${config.validation.user.nicknameMinLength}")
	private int minNicknameLength;
	@Value("${config.validation.user.nicknameMaxLength}")
	private int maxNicknameLength;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index() {
		return "my/nickname/index";
	}

	@RequestMapping(value = "change", method = RequestMethod.POST)
	public String change(@RequestParam(value = "nickname", defaultValue = "") String nickname, ModelMap model,
			RedirectAttributes ra) {
		if (!Jsoup.isValid(nickname, Whitelist.none())) {
			model.addAttribute(ERROR, new I18NMessage("validation.nickname.invalid"));
			return "my/nickname/index";
		}
		if (nickname.trim().length() < minNicknameLength || nickname.length() > maxNicknameLength) {
			model.addAttribute(ERROR,
					new I18NMessage("validation.nickname.length.invalid", minNicknameLength, maxNicknameLength));
			return "my/nickname/index";
		}
		try {
			userService.changeNickname(nickname);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/nickname/index";
		}
		ra.addFlashAttribute(SUCCESS, new I18NMessage("success.changeNickname"));
		return "redirect:/";
	}

}
