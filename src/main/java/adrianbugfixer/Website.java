package adrianbugfixer;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.Id;

@Entity
public class Website {
	@Id
	@GeneratedValue
	private Long id;

	public String uri;

	@OneToMany(mappedBy = "website")
	public Set<Comment> comments = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Website(String uri) {
		super();
		this.uri = uri;
	}

	public Website() {
	};

}
