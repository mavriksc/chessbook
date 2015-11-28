package chessbook.lichess.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class LiChessUserProfile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1654694L;
	private String bio;
	private String country;
	private String firstName;
	private String lastName;
	private String location;
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	

}
