package chessbook.lichess.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Opening {
	@Column(name="OPENING_CODE")
	String code;
	@Column(name="OPENING_NAME")
	String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
