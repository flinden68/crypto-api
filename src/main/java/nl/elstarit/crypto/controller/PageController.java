package nl.elstarit.crypto.controller;

import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class PageController {

	@Autowired
	CustomerService customerService;

	@GetMapping({"/", "/welcome"})
	public String welcome(Model model) {
		return "welcome";
	}

	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("customer", new Customer());

		return "registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("customer") Customer customer, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "registration";
		}

		customerService.save(customer);
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	@GetMapping("/customer")
	public String customer(Model model, @AuthenticationPrincipal Customer customer) {
		if (customer == null) {
			return "redirect:/";
		}

		List<String> coins = Arrays.asList("BTC","ETH","XRP","LINK","BAT","ADA","LTC","BCH","DASH","BNB","VET","EOS","ETC","XTZ","XMR","XLM","ZEC","DOGE","TRX","ATOM","ALGO","OMG","NANO","QTUM","LSK","ICX","WAVES","DOT","SNX","KNC");
		model.addAttribute("coins", coins);
		return "customer";
	}

}
