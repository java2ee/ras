/**
 * 
 */
package ru.funsys.avalanche.ras;

/**
 * Сессионый бин пользовательского интерфейса
 * 
 * @author Валерий Лиховских
 *
 */
public class BeanSession extends BeanStation {

	private String id;

	public BeanSession(String name, String hostName, String id) {
		super(name, hostName);
		setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
