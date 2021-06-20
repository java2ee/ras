/**
 * 
 */
package ru.funsys.avalanche.ras.station;

import java.lang.management.ManagementFactory;
import java.util.HashMap;

import ru.funsys.avalanche.Application;

/**
 * @author Валерий Лиховских
 *
 */
public class Station extends Application {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3211997452003168822L;
	
	private HashMap<String, Shell> shells = new HashMap<String, Shell>();
	
	public BeanShell create(String host, int port, String user, String password) throws Exception {
		Shell shell = new Shell(host, port, user, password);
		BeanShell beanShell = new BeanShell(host, user);
		shells.put(beanShell.toString(), shell);
		return beanShell;
	}

	
	public void destroy(String host, String user, String id) throws Exception {
		BeanShell beanShell = new BeanShell(host, user, id);
		Shell shell = shells.remove(beanShell.toString());
		shell.close();
	}

	public String execute(String host, String user, String id, String command) throws Exception {
		BeanShell beanShell = new BeanShell(host, user, id);
		Shell shell = shells.get(beanShell.toString());
		return shell.execute(command);
	}

	/**
	 * Получить имя рабочей станции
	 * 
	 * @return имя рабочей сьанции
	 */
	public String getStationName() {
		String processName = ManagementFactory.getRuntimeMXBean().getName();
		String[] names = processName.split("@");
		return names[1];
	}


	@Override
	public void done() {
		shells.forEach((key, shell) -> {
			try {
				shell.close();
			} catch (Exception e) {
			}
		});
		super.done();
	}

	
}
