package chatAPP_database;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;



@NoRepositoryBean
public abstract interface CustomJpaRepository<T,ID> extends JpaRepository<T,ID>  {

	/** Metod return saved entity, aling by primaryKey
	 * @throws RunTimeException EntityNotFoundException if message was not found with assign primaryKey */
	default T findByPrimaryKey(ID primaryKey)  {
		Optional<T> entity=this.findById(primaryKey);
		if(entity.isEmpty())throw new EntityNotFoundException();

		return entity.get();
	}
	
}
