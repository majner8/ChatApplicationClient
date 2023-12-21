package User;

import java.time.LocalDateTime;

public abstract class UserProfileDTO {
	
	private String serName;
	private String lastName;
	private String nickName;
	
	
	
	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getSerName() {
		return serName;
	}


	public void setSerName(String serName) {
		this.serName = serName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
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
