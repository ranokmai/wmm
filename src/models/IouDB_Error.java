package models;

public class IouDB_Error extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String error;
		
	public IouDB_Error(String e) {
			error = e;
		}
}
