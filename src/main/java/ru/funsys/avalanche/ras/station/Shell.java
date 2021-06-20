/**
 * 
 */
package ru.funsys.avalanche.ras.station;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;

/**
 * @author Валерий
 *
 */
public class Shell implements Runnable {

	class ShellCommand extends ShellTask {
		
		String command;
		boolean running;
		StringBuilder resultCommand;
		
		ShellCommand(SshClient ssh) {
			super(ssh);
			running = true;
		}

		String execute(String command) throws Exception {
			String result = null;
			error = null;
			this.command = command;
			synchronized (this) {
				notifyAll();
			}
			while (resultCommand == null && error == null && running) {
				synchronized (this) {
					try {
						wait(200L);
					} catch (Exception e) {
					}
				}
			}
			if (error != null) throw error;
			if (resultCommand != null) {
				result = resultCommand.toString();
				resultCommand = null;
			}
			return result;
		}
		
		protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException { 
	        ExpectShell shell = new ExpectShell(this);

	        // Execute the first command
		    ShellProcess process = shell.executeCommand("pwd");
		    process.drain();
		    currentDir = process.getCommandOutput();
		    
		    while (running) {
				synchronized (this) {
					try {
						wait(100L);
					} catch (InterruptedException e) {
					}
				}
		    	if (command != null) {
		    		try {
			    		String nextCommand = command;
			    		command = null;
				        // After processing output execute another
				        ShellProcessController controller = new ShellProcessController(shell.executeCommand(nextCommand));
				        ShellProcess command = controller.getProcess();
				        BufferedReader reader = new BufferedReader(new InputStreamReader(command.getInputStream()));      
				        String line;
				        StringBuilder resultNextCommand = new StringBuilder();
				        while((line = reader.readLine())!=null) {          
				        	 resultNextCommand.append(line).append(System.lineSeparator());      
				        }
				        resultCommand = resultNextCommand;
		    		} catch (Exception e) {
						error = e;
						throw e;
					}
		    	}
		    }
	        shell.close();
		}
		 
		void shutdown() {
			running = false;
			synchronized (this) {
				notifyAll();
			}
		}
	}
	
	private SshClient ssh;
	private ShellCommand shellCommand;
	private Thread thread;
	private Exception error;

	private String user;
	private String currentDir;
	
	public Shell(String host, int port, String user, String password) throws Exception {
		this.user = user;
		ssh = new SshClient(host, port, user, password.toCharArray());
		thread = new Thread(this, "shell-" + user);
		thread.start();
		do {
			synchronized (this) {
				try {
					wait(100L);
				} catch (Exception e) {
				}
			}
		} while ((currentDir == null && error == null) && shellCommand.running); 
		if (error != null) {
			throw error;
		}
	}

	public String getUser() {
		return user;
	}

	public String getCurrentDir() {
		return currentDir;
	}
	
	public void close() throws IOException {
		shellCommand.shutdown();
		ssh.disconnect();
		ssh.close();
	}
	
	public String execute(String commnad) throws Exception {
		return shellCommand.execute(commnad);
	}
	
	public void run() {
		shellCommand = new ShellCommand(ssh);
		try {
			ssh.runTask(shellCommand);
		} catch (Exception e) {
			error = e;
		}
	}
	
}
