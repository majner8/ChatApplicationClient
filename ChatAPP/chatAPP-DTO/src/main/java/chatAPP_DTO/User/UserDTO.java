package chatAPP_DTO.User;

import java.time.LocalDateTime;



public class UserDTO {

	public static class UserProfileDTO{
		
		private String SerName;
		
		private String lastName;
		
		private String nickName;
		
		private String userID;
		
		
		public String getSerName() {
			return SerName;
		}


		public void setSerName(String serName) {
			SerName = serName;
		}


		public String getLastName() {
			return lastName;
		}


		public void setLastName(String lastName) {
			this.lastName = lastName;
		}


		public String getNickName() {
			return nickName;
		}


		public void setNickName(String nickName) {
			this.nickName = nickName;
		}


		public String getUserID() {
			return userID;
		}


		public void setUserID(String userID) {
			this.userID = userID;
		}


		public static class shareUserProfileDTO extends UserProfileDTO{
			
			
		}
	}
	
	public static class UserComunicationDTO{
		private String email;
		
		private String phonePreflix;
		
		private String phone;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhonePreflix() {
			return phonePreflix;
		}

		public void setPhonePreflix(String phonePreflix) {
			this.phonePreflix = phonePreflix;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
		
	}
	
	public static class UserAuthPasswordDTO{
		private String password;
		
		private LocalDateTime lastPasswordChange;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public LocalDateTime getLastPasswordChange() {
			return lastPasswordChange;
		}

		public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
			this.lastPasswordChange = lastPasswordChange;
		}
		
		
	}
	
	public static class UserAuthorizationDTO{
		private UserAuthPasswordDTO password;
		private UserComunicationDTO profile;
		public UserAuthPasswordDTO getPassword() {
			return password;
		}
		public void setPassword(UserAuthPasswordDTO password) {
			this.password = password;
		}
		public UserComunicationDTO getProfile() {
			return profile;
		}
		public void setProfile(UserComunicationDTO profile) {
			this.profile = profile;
		}
		
		
	}


	public static class UserProfileRegistrationDTO extends UserProfileDTO{
		private LocalDateTime userBorn;

		public LocalDateTime getUserBorn() {
			return userBorn;
		}

		public void setUserBorn(LocalDateTime userBorn) {
			this.userBorn = userBorn;
		}
		
	}
}
