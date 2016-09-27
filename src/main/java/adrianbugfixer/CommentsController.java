package adrianbugfixer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import utils.*;

@Controller
public class CommentsController {

	@Autowired
	CommentRepository commentsRepository;
	@Autowired
	WebsiteRepository websiteRepository;

	private String currentUrl = "";
	
	@GetMapping("/popup")
	public String comments(@RequestParam(value = "url", required = false, defaultValue = "hypoview") String url,
			Model model, Website website, Comment comment) {
		if(!url.equals("hypoview")){
			currentUrl = url;	
		}
		Collection<Comment> comments = commentsRepository.findByWebsite_Uri(url);
		model.addAttribute("comments", comments);
		model.addAttribute("url", url);
		return "popup";
	}

	@PostMapping("/popup")
	public String commentSubmission(Model model, Comment comment, Website website) {
		Optional<Website> websiteResult = websiteRepository.findByUri(currentUrl);
		Website websiteFinal = (websiteResult.isPresent()) ? website = websiteResult.get()
				: websiteRepository.save(new Website(currentUrl));
		commentsRepository.save(new Comment(websiteFinal, comment.getContent()));
		Collection<Comment> comments = commentsRepository.findByWebsite_Uri(currentUrl);
		model.addAttribute("comments", comments);
		model.addAttribute("url", currentUrl);
		return "popup";
	}

}
