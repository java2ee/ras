/**
 * 
 */
package ru.funsys.avalanche.ras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.funsys.avalanche.Application;
import ru.funsys.avalanche.AvalancheException;
import ru.funsys.avalanche.Messages;
import ru.funsys.avalanche.ras.station.BeanShell;
import ru.funsys.avalanche.ras.station.StationAdapter;

/**
 * @author Валерий Лиховских
 *
 */
public class Server extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = 430853441035509742L;

	/**
	 * Рабочие станции к котрым предоставляется удаленный доступ 
	 */
	private HashMap<String, StationAdapter> stations = new HashMap<String, StationAdapter>();
	
	/**
	 * @param name имя адаптера
	 * @param adapter экземпляр адаптера
	 * @return
	 */
	@Override
	public boolean addAdapter(String name, Object adapter) {
		boolean result = super.addAdapter(name, adapter);
		if (result && adapter instanceof StationAdapter) {
			StationAdapter station = (StationAdapter) adapter;
			stations.put(name, station);
		}
		return result;
	}
		
	public BeanShell create(String name, String host, String port, String user, String password) throws Exception {
		int p = Integer.parseInt(port);
		StationAdapter adapter = stations.get(name);
		if (adapter != null) {
			BeanShell shell = adapter.create(host, p, user, password);
			return shell;
		} else {
			throw new AvalancheException("RAS0014E", new Object[] {name});
		}
		
	}
	
	public String execute(BeanShell shell, String name, String command) throws Exception {
		StationAdapter adapter = stations.get(name);
		if (adapter != null) {
			String result = adapter.execute(shell.getHost(), shell.getUser(), shell.getId(), command);
			return result;
		} else {
			throw new AvalancheException("RAS0014E", new Object[] {name});
		}
	}
	
	public void destroy(BeanShell shell, String name) throws Exception {
		StationAdapter adapter = stations.get(name);
		if (adapter != null) {
			adapter.destroy(shell.getHost(), shell.getUser(), shell.getId());
		} else {
			throw new AvalancheException("RAS0014E", new Object[] {name});
		}
	}
	
	public List<BeanStation> getStations() {
		List<BeanStation> list = new ArrayList<BeanStation>();
		stations.forEach((key, value) -> {
			try {
				list.add(new BeanStation(key, value.getStationName()));
			} catch (Exception e) {
				list.add(new BeanStation(key, "error"));
			}
		});
		return list;
	}

	public String getMessage(String key, String lang, Object[] parameters) {
		return Messages.getMessage(key, lang, parameters, false);
	}

	public String getMessage(String key, String lang) {
		return Messages.getMessage(key, lang, null, false);
	}
	
}
