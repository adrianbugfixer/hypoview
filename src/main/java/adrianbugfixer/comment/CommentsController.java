package adrianbugfixer.comment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;

import adrianbugfixer.accountInfo.AccountInfo;
import adrianbugfixer.accountInfo.AccountInfoRepository;
import adrianbugfixer.website.Website;
import adrianbugfixer.website.WebsiteRepository;
import utils.*;

@Controller
public class CommentsController {

	@Autowired
	CommentRepository commentsRepository;
	@Autowired
	WebsiteRepository websiteRepository;
	@Autowired
	AccountInfoRepository accountInfoRepository;

	private String currentUrl = "";

	@GetMapping("/popup")
	public String comments(@RequestParam(value = "url", required = false, defaultValue = "hypoview") String url,
			Model model, Website website, Comment comment) {
		if (!url.equals("hypoview")) {
			currentUrl = url;
		}
		Collection<Comment> comments = commentsRepository.findByWebsite_Uri(url);
		model.addAttribute("comments", comments);
		model.addAttribute("url", url);
		return "popup";
	}

	@PostMapping("/popup")
	public String commentSubmission(HttpServletRequest req, Model model, Comment comment, Website website) {
		Optional<Website> websiteResult = websiteRepository.findByUri(currentUrl);
		Website websiteFinal = (websiteResult.isPresent()) ? website = websiteResult.get()
				: websiteRepository.save(new Website(currentUrl));

		Account account = AccountResolver.INSTANCE.getAccount(req);
		AccountInfo accountInfo = accountInfoRepository.save(new AccountInfo(account.getEmail(), account.getHref(), account.getFullName()));
		commentsRepository.save(new Comment(websiteFinal, comment.getContent(),accountInfo));
		Collection<Comment> comments = commentsRepository.findByWebsite_Uri(currentUrl);
		model.addAttribute("comments", comments);
		model.addAttribute("url", currentUrl);
		return "popup";
	}

}
