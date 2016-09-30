package adrianbugfixer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AccountInfo {
	@Id
	@GeneratedValue
	private Long id;
	
	public Long getId() {
		return id;
	}
	public String fullName;
	public String href;
	public String email;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AccountInfo(String fullName, String href, String email) {
		super();
		this.fullName = fullName;
		this.href = href;
		this.email = email;
	}
	public AccountInfo(){};
}
