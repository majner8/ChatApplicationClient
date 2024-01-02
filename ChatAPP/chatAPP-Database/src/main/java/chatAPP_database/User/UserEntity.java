package chatAPP_database.User;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import chatAPP_CommontPart.Log4j2.Log4j2;

@Entity
public class UserEntity {

	public static final String userEntityTableName="";
	public static final String userIdColumnName="";
	public static final String emailColumnName="";
	public static final String countryPreflixColumnName="";
	public static final String phoneColumnName="";
	public static final String isUserActiveColumnName="";
	public static final String serNameColumnName="";
	public static final String lastNameColumnName="";
	public static final String nickColumnName="";
	public static final String bornDateColumnName="";
	public static final String VersionColumnName="";

	@Id
	@Column(name=UserEntity.userIdColumnName)
	private long userId;
	@Column(name=UserEntity.emailColumnName)
	private String email;
	@Column(name=UserEntity.countryPreflixColumnName)
	private String countryPreflix;
	@Column(name=UserEntity.phoneColumnName)
	private String phone;
	@Column(name=UserEntity.isUserActiveColumnName)
	private boolean isUserActive;
	@Column(name=UserEntity.serNameColumnName)
	private String serName;
	@Column(name=UserEntity.lastNameColumnName)
	private String lastName;
	@Column(name=UserEntity.nickColumnName)
	private String Nick;
	@Column(name=UserEntity.bornDateColumnName)
	private LocalDateTime bornDate;
	@javax.persistence.Version
	private long Version;
	
	public UserEntity() {
		
	}

	//with all containing value
	public UserEntity(Map<String,Object> values) {
		try {
			
			Class<?> clas = this.getClass();
			synchronized(values) {
				Iterator<String> set=values.keySet().iterator();
				while(set.hasNext()) {
					String fieldName=set.next();
					Object value=values.get(fieldName);
					Field field=clas.getDeclaredField(fieldName);
					field.set(this, value);
					
				}
				
			}
		}
		 catch (IllegalAccessException |NoSuchFieldException |SecurityException e) {
				Log4j2.log.fatal("UserEntity reflection class problem,Conctruction");
			e.printStackTrace();
		}
	}
	/**Metod return all as object connected with nameOf Field */
	public Map<String,Object> getValues(){
		HashMap map = new HashMap<String, Object>();
		try {
			Class<?> clas = this.getClass();
			for (Field field : clas.getDeclaredFields()) {
				map.put(field.getName(), field.get(this));
			} 
		}
		 catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log4j2.log.fatal("UserEntity reflection class problem");
			return null;
		}
		return map;
		
		
	}
	public LocalDateTime getBornDate() {
		return bornDate;
	}
	
	
	public long getVersion() {
		return Version;
	}

	public void setVersion(long version) {
		Version = version;
	}

	public void setBornDate(LocalDateTime bornDate) {
		this.bornDate = bornDate;
	}



	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public boolean isUserActive() {
		return isUserActive;
	}
	public void setUserActive(boolean isUserActive) {
		this.isUserActive = isUserActive;
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
	public String getNick() {
		return Nick;
	}
	public void setNick(String nick) {
		Nick = nick;
	}
	
}
