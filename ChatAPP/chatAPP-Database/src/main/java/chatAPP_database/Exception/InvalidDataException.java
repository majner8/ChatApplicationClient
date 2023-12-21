package chatAPP_database.Exception;


public abstract class InvalidDataException extends RuntimeException {

	
	public static class EntityWasNotFound extends InvalidDataException{
		
	}
}