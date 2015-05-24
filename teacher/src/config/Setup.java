package config;

import communication.RemoteSSH;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for setting-up the LabSpy client on the student machine. It should be called for each added machine.
 *
 * TODO: Currently this class expects clientSRC to be an executable file. As labspy_client will not be an executable (it's a JAR file), we must create a .sh script to put in /etc/init.d/.
 */
public class Setup {

    private RemoteSSH ssh;
    private final String pathToInstall = "/var/lib/LabSpy/";
    private final String clientSRC = "/home/paladini/meuProgramaQueVouEnviar";

    public Setup(RemoteSSH ssh) {
        this.ssh = ssh;
    }

    public Setup(Computer c, String username, String password) {
        this.ssh = new RemoteSSH(c, username, password);
    }

    public void install() {

        // Creating folder in the remote machine.
        try {
            ssh.executeCommand("sudo mkdir -p " + pathToInstall);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Transfering the Client for the remote machine.
        try {
            ssh.transferFile(clientSRC, pathToInstall);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Enabling the client to run at the startup.
        try {
            ssh.executeCommand("sudo ln -s " + pathToInstall + clientSRC + " /etc/init.d/" + "labspy_client");
            ssh.executeCommand("sudo chmod +x /etc/init.d/labspy_client");
            ssh.executeCommand("sudo update-rc.d labspy_client defaults");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}