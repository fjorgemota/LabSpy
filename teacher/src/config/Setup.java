package config;

import communication.RemoteSSH;
import others.Computer;


/**
 * Esta classe é responsável por realizar o setup / instalação do Cliente LabSpy (máquinas dos alunos) em uma máquina.
 * Utiliza classes como Computer e RemoteSSH para tal.
 *
 */
public abstract class Setup {

    RemoteSSH ssh;

    public Setup(RemoteSSH ssh) {
        this.ssh = ssh;
    }

    public Setup(Computer c, String username, String password) {
        this.ssh = new RemoteSSH(c, username, password);
    }

    public void install() {

        // Uninstall previous version of LabSpy for student (only if it already exists).
        // You should kill the process of previous installed (and running) Labspy, remove it from startup and remove the older installation folder.
        uninstall();

        // Creating folder in the remote machine.
        // You should create the folder where you'll install the student client.
        createDirectory();

        // Transfering the Client for the remote machine.
        // You should transfer "Student.jar" (commonly on "out/artifacts/Student/Student.jar")
        // from your computer to the remote student computer. Any other needed files must be sent as well.
        transferFiles();

        // Enabling the client to run at the startup.
        // You should configure the remote OS to run student client at startup.
        installToRunAtStartup();

    }

    abstract void uninstall();
    abstract void createDirectory();
    abstract void transferFiles();
    abstract void installToRunAtStartup();

    // Forces developer to create the variables "pathToInstall" and clientSRC
    abstract String getPathToInstall();
    abstract String getClientSRC();
    
}
