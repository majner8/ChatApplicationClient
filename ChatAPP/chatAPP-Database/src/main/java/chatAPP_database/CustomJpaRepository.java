package chatAPP_database;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;




public abstract interface CustomJpaRepository<K,T> extends JpaRepository<K,T>  {

	/** Metod return saved entity, aling by primaryKey
	 * @throws RunTimeException EntityNotFoundException if message was not found with assign primaryKey */
	default K findByPrimaryKey(T primaryKey)  {
		Optional<K> entity=this.findById(primaryKey);
		if(entity.isEmpty())throw new EntityNotFoundException();

		return entity.get();
	}
	
}
