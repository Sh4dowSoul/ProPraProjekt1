package applicationLogic;

/**
 * The Class Manager
 * 
 * @author name
 */

public class Manager {

	/** The instance. */
	private static Manager instance = null;

	/**
	 * Singleton design pattern if no instance is used, create new instance,
	 * otherwise return known instance.
	 *
	 * @return single instance of Manager
	 */
	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}
		return instance;
	}
	
	//... methods

}
