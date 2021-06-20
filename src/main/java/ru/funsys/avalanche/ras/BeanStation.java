/**
 * 
 */
package ru.funsys.avalanche.ras;

/**
 * Объект сессии, используется в пользовательском интерфейсе
 * 
 * @author Валерий Лиховских
 *
 */
public class BeanStation {
	
	private String name;
	
	private String hostName;

	public BeanStation(String name, String hostName) {
		super();
		this.name = name;
		this.hostName = hostName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}
