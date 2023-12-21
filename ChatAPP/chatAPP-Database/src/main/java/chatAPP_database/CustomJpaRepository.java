package chatAPP_database;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import chatAPP_database.Exception.InvalidDataException;



public abstract interface CustomJpaRepository<K,T> extends JpaRepository<K,T>  {

	/** Metod return saved entity, aling by primaryKey
	 * @throws RunTimeException InvalidDataException.EntityWasNotFound if message was not found with assign primaryKey */
	default K findByPrimaryKey(T primaryKey)  {
		Optional<K> entity=this.findById(primaryKey);
		if(entity.isEmpty())throw new InvalidDataException.EntityWasNotFound();
	
		return entity.get();
	}
	
}
