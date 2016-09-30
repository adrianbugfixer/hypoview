//tag::runner[]
package adrianbugfixer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;

import adrianbugfixer.accountInfo.AccountInfo;
import adrianbugfixer.accountInfo.AccountInfoRepository;
import adrianbugfixer.comment.Comment;
import adrianbugfixer.comment.CommentRepository;
import adrianbugfixer.website.Website;
import adrianbugfixer.website.WebsiteRepository;
import utils.HtmlEncoding;

@SpringBootApplication
public class HypoServerApplication {

	@Bean
	public CommandLineRunner init(AccountInfoRepository accountInfoRepository, WebsiteRepository websiteRepository,
			CommentRepository commentRepository) {
		return (evt) -> Arrays.asList("http://google.com", "http://www.wykop.pl/").forEach(a -> {
			Website website = websiteRepository.save(new Website(a));
			AccountInfo accountInfo = accountInfoRepository
					.save(new AccountInfo("Adrian", "", "adrian.bugfixer@gmail.com"));
			commentRepository.save(new Comment(website, "Ladna stronka", accountInfo));
			commentRepository.save(new Comment(website, "Omg, ale zal", accountInfo));
			commentRepository.save(new Comment(website, "Twoja stara nie ma kolan", accountInfo));
		});
	}

	public static void main(String[] args) {
		SpringApplication.run(HypoServerApplication.class, args);
	}
}
// end::runner[]
@RestController
@CrossOrigin
@RequestMapping("/api/website/")
class WebsiteRestController {
	private final CommentRepository commentRepository;
	private final WebsiteRepository websiteRepository;

	// @RequestMapping(method = RequestMethod.POST)
	// ResponseEntity<?> add(@RequestBody Website input) {
	// Website result = this.websiteRepository.save(new
	// Website(input.getUri()));
	// HttpHeaders httpHeaders = new HttpHeaders();
	// httpHeaders.setLocation(ServletUriComponentsBuilder
	// .fromCurrentRequest().path("/{id}")
	// .buildAndExpand(result.getId()).toUri());
	// return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	// }
	//
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> getOne(@RequestBody Website input) {
		Optional<Website> result = this.websiteRepository.findByUri(input.uri);
		HttpHeaders httpHeaders = new HttpHeaders();
		Website website;
		if (result.isPresent()) {
			website = result.get();
		} else {
			website = this.websiteRepository.save(new Website(input.getUri()));
		}
		httpHeaders.setLocation(
				ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(website.getId()).toUri());
		return new ResponseEntity<Object>(website, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	List<Website> readWebsites() {
		return this.websiteRepository.findAll();
	}

	@RequestMapping(value = "/{websiteId}", method = RequestMethod.GET)
	Website readWebsite(@PathVariable Long websiteId) {
		return this.websiteRepository.findOne(websiteId);
	}

	// @RequestMapping(value= "/uri/{websiteUri}", method = RequestMethod.GET)
	// Optional<Website> readWebsiteByUri(@PathVariable String websiteUri) {
	// this.validateWebsite(websiteUri);
	// return this.websiteRepository.findByUri(websiteUri);
	// }

	@Autowired
	public WebsiteRestController(CommentRepository commentRepository, WebsiteRepository websiteRepository) {
		this.commentRepository = commentRepository;
		this.websiteRepository = websiteRepository;
	}

	private void validateWebsite(String websiteUri) {
		this.websiteRepository.findByUri(websiteUri).orElseThrow(() -> new WebsiteNotFoundException(websiteUri));
	}
}

@RestController
@CrossOrigin
@RequestMapping("/api/website/{websiteId}/comments")
class CommentRestController {
	private final CommentRepository commentRepository;
	private final WebsiteRepository websiteRepository;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@PathVariable Long websiteId, @RequestBody Comment input) {
		return this.websiteRepository.findById(websiteId).map(website -> {
			Comment result = commentRepository.save(
					new Comment(website, input.content, new AccountInfo("Adrian", "", "adrian.bugfixer@gmail.com")));

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(result.getId()).toUri());
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
		}).get();
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
	Comment readComment(@PathVariable String websiteId, @PathVariable Long commentId) {
		return this.commentRepository.findOne(commentId);
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<Comment> readComments(@PathVariable Long websiteId) {
		return this.commentRepository.findByWebsiteId(websiteId);
	}

	@Autowired
	public CommentRestController(CommentRepository commentRepository, WebsiteRepository websiteRepository) {
		this.commentRepository = commentRepository;
		this.websiteRepository = websiteRepository;
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class WebsiteNotFoundException extends RuntimeException {

	public WebsiteNotFoundException(String websiteId) {
		super("could not find website '" + websiteId + "'.");
	}
}