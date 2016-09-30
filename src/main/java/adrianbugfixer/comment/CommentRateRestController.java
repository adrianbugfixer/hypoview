package adrianbugfixer.comment;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;

import adrianbugfixer.accountInfo.AccountInfoRepository;

@RestController
@RequestMapping("/api/comment/{commentId}")
class CommentRateRestController {
	private final CommentRepository commentRepository;
	private final AccountInfoRepository accountInfoRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<?> rate(HttpServletRequest req, @PathVariable Long commentId, Rate rate) {
		Account account = AccountResolver.INSTANCE.getAccount(req);
		if(account!=null) {
			Comment comment = this.commentRepository.findOne(commentId);
			if(comment != null) {
				if(rate == Rate.UPVOTE){
					comment.upVote(account.getHref());
					this.commentRepository.save(comment);	
				} else if(rate == Rate.DOWNVOTE){
					comment.downVote(account.getHref());
					this.commentRepository.save(comment);
				} else {
					return new ResponseEntity<Object>(comment, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<Object>(new String("Comment not found"), HttpStatus.BAD_REQUEST);
			}
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri());
			return new ResponseEntity<Object>(comment, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN);
		}
		
	}
	
	@Autowired
	public CommentRateRestController(CommentRepository commentRepository, AccountInfoRepository accountInfoRepository) {
		this.commentRepository = commentRepository;
		this.accountInfoRepository = accountInfoRepository;
	}

}

