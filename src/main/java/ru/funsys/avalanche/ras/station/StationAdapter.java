/**
 * 
 */
package ru.funsys.avalanche.ras.station;

import ru.funsys.avalanche.AvalancheRemote;

/**
 * Адаптер передачи команд рабочей станции
 * 
 * @author Валерий Лиховских
 *
 */
public interface StationAdapter {

	public String getStationName() throws AvalancheRemote;

	public BeanShell create(String host, int port, String user, String password) throws AvalancheRemote;
	
	public void destroy(String host, String user, String id) throws AvalancheRemote;

	public String execute(String host, String user, String id, String command) throws AvalancheRemote;
	
}
