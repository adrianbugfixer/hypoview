package adrianbugfixer;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Comment {

	@JsonIgnore
	@ManyToOne
	private Website website;
	
	@Id
	@GeneratedValue
	private Long id;
	
	public String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Comment(Website website, String content) {
		super();
		this.website = website;
		this.content = content;
	} 
	
	public Comment() {}

	public Long getId() {
		return this.id;
	};
	
}
