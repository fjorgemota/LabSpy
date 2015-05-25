package config;

import communication.RemoteSSH;

import java.io.File;
import java.io.IOException;

/**
 * Esta classe é responsável por realizar o setup / instalação do Cliente LabSpy (máquinas dos alunos) em uma máquina.
 * Utiliza classes como Computer e RemoteSSH para tal.
 *
 */
public class Setup {

    private RemoteSSH ssh;
    private final String pathToInstall = "/var/lib/LabSpy/";
    private final String clientSRC = "out/artifacts/Student/Student.jar";
    private final String scriptSRC = "assets/labspy.sh";

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
            ssh.transferFile(scriptSRC, pathToInstall);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Enabling the client to run at the startup.
        try {
            ssh.executeCommand("sudo ln -s /var/lib/LabSpy/labspy.sh /etc/init.d/labspy_client");
            ssh.executeCommand("sudo chmod +x /etc/init.d/labspy_client");
            ssh.executeCommand("sudo update-rc.d labspy_client defaults");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
