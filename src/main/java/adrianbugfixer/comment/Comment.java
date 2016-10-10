package adrianbugfixer.comment;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stormpath.sdk.account.Account;

import adrianbugfixer.accountInfo.AccountInfo;
import adrianbugfixer.website.Website;

@Entity
public class Comment {

	@Id
	@GeneratedValue
	private Long id;

	@JsonIgnore
	@ManyToOne
	private Website website;

	@JsonIgnore
	@ManyToOne
	private AccountInfo accountInfo;

	@ElementCollection
	public Set<String> upVotes;

	@ElementCollection
	public Set<String> downVotes;

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	private Date created;

	public Long rate;

	public String content;

	@PrePersist
	protected void onCreate() {
		setCreated(new Date());
	}

	public Long getRate() {
		return rate;
	}

	public void setRate(Long rate) {
		this.rate = rate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Comment(Website website, String content, AccountInfo accountInfo) {
		super();
		this.website = website;
		this.content = content;
		this.accountInfo = accountInfo;
		this.rate = 0L;
	}

	public Comment() {
	}

	@Override
	public String toString() {
		return "Comment [website=" + website + ", id=" + id + ", content=" + content + "]";
	}

	public Long getId() {
		return this.id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean upVote(String href) {
		if (!this.upVotes.contains(href)) {
			if (this.downVotes.contains(href)) {
				this.downVotes.remove(href);
			} else {
				this.upVotes.add(href);
			}
			this.rate++;
			return true;
		}
		return false;
	}

	public boolean downVote(String href) {
		if (!this.downVotes.contains(href)) {
			if (this.upVotes.contains(href)) {
				this.upVotes.remove(href);
			} else {
				this.downVotes.add(href);
			}
			this.rate--;
			return true;
		}
		return false;
	}

	public Set<String> getUpVotes() {
		return upVotes;
	}

	public void setUpVotes(Set<String> upVotes) {
		this.upVotes = upVotes;
	}

	public Set<String> getDownVotes() {
		return downVotes;
	}

	public void setDownVotes(Set<String> downVotes) {
		this.downVotes = downVotes;
	}

	public String getHowLongAgoWasCreated() {
		Date currentDate = new Date();
		long diff = currentDate.getTime() - this.created.getTime();
		diff /= 60000;
		
		if(diff < 1) {
			return "less than a minute ago";
		} else if (diff == 1) {
			return "1 minute ago";
		} else if (diff == 60) {
			return "1 hour ago";
		} else if (diff == 60 * 24) {
			return "1 hour ago";
		} else if (diff == 60 * 24 * 30) {
			return "1 month ago";
		} else if (diff > 1 && diff < 60) {
			return Long.toString(diff) + " minutes ago";
		} else if (diff >= 60 && diff < 60 * 24) {
			return Long.toString(diff) + " hours ago";
		} else if ((diff >= 60 * 24) && diff < 60 * 24 * 30) {
			return Long.toString(diff) + " days ago";
		}
		
		return Long.toString(diff);
	}
}
