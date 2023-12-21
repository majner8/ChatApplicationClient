package chatAPP_database.User;

import java.util.Optional;


import chatAPP_database.CustomJpaRepository;

public interface UserEntityRepository extends CustomJpaRepository<UserEntity,Long> {

	
    boolean existsByEmail(String email);
	boolean existsByPhoneAndCountryPreflix(String phone,String countryPreflix);
	default boolean existsByEmailOrPhoneAndCountryPreflix(String email,String phone,String CountryPreflix) {
		if(email==null) {
			return this.existsByPhoneAndCountryPreflix(phone, CountryPreflix);
		}
		else {
			return this.existsByEmail(email);
		}
	}
	default Optional<UserEntity> findByEmailOrPhoneAndCountryPreflix(String email,String phone,String CountryPreflix){
		if(email==null) {
			return this.findByPhoneAndCountryPreflix(phone, CountryPreflix);
		}
		return this.findByEmail(email);
	}	
	Optional<UserEntity> findByEmail(String emai);
	Optional<UserEntity> findByPhoneAndCountryPreflix(String phone,String countryPreflix);

}
