package AuthorizationDTO;



public class AutorizationRequestDTO {

	private String email;
	private String countryPreflix;
	private String phone;
	private String password;
	private int deviceID;
	private Boolean isDeviceNew;
	
	
	public Boolean getIsDeviceNew() {
		return isDeviceNew;
	}
	public void setIsDeviceNew(Boolean isDeviceNew) {
		this.isDeviceNew = isDeviceNew;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountryPreflix() {
		return countryPreflix;
	}
	public void setCountryPreflix(String countryPreflix) {
		this.countryPreflix = countryPreflix;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	
	
}

