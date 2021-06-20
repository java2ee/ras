/**
 * 
 */
package ru.funsys.avalanche.ras.station;

import java.io.Serializable;

/**
 * Бин объект формирования пользовательского интерфейса 
 * 
 * @author Валерий Лиховских
 *
 */
public class BeanShell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6756238828343512903L;
	
	String host;
	String user;
	String id;

	public BeanShell(String host, String user) {
		super();
		this.host = host;
		this.user = user;
		id = new Long(System.currentTimeMillis()).toString();
	}

	public BeanShell(String host, String user, String id) {
		super();
		this.host = host;
		this.user = user;
		this.id = id;
	}
	
	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Shell-" + host + "-" + user + "-" + id;
	}

}
