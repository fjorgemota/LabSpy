/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.IOException;
import communication.RemoteSSH;
import others.Computer;

/**
 * This class is responsible for setting-up a student client on a remote machine. 
 * It will create folders, transfer files and run commands over an SSH connection to install the student client on a remote machine.
 * @author paladini
 */
public class SetupLinux extends Setup {

    private String clientSRC;
    private String scriptSRC;

    public SetupLinux(RemoteSSH ssh) {
        super(ssh);
        checkEnvironment();
    }

    public SetupLinux(Computer c, String username, String password) {
        super(c, username, password);
        checkEnvironment();
    }

    @Override
    void uninstall() {
        try {
            this.ssh.executeCommand("sudo /etc/init.d/labspy_client stop");
            this.ssh.executeCommand("sudo update-rc.d -f labspy_client remove");
            this.ssh.executeCommand("sudo rm -f /etc/init.d/labspy_client");
            this.ssh.executeCommand("sudo rm -rf /var/lib/LabSpy");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void createDirectory() {
        try {
            this.ssh.executeCommand("sudo mkdir -p " + getPathToInstall());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void transferFiles() {
         try {
            ssh.transferFile(clientSRC, getPathToInstall());
            ssh.transferFile(scriptSRC, getPathToInstall());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void installToRunAtStartup() {
        try {
            ssh.executeCommand("sudo bash -c \"echo ${SSH_CLIENT%% *} > /var/lib/LabSpy/addressList\"");
            ssh.executeCommand("sudo ln -s /var/lib/LabSpy/labspy.sh /etc/init.d/labspy_client");
            ssh.executeCommand("sudo chmod +x /etc/init.d/labspy_client");
            ssh.executeCommand("sudo update-rc.d labspy_client defaults 99 01");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    String getPathToInstall() {
        String pathToInstall = "/var/lib/LabSpy/";
        return pathToInstall;
    }

    @Override
    String getClientSRC() {
        return this.clientSRC;
    }

    private void checkEnvironment() {
        String env = System.getenv("LABSPY_ENV");
        boolean dev = false;
        if (env != null) {
            if (env.equalsIgnoreCase("dev")) {
                clientSRC = "out/artifacts/Student/Student.jar";
                scriptSRC = "assets/labspy.sh";
                dev = true;
            }
        }

        if (!dev) {
            clientSRC = System.getProperty("user.home") + "/.labspy/bin/Student.jar";
            scriptSRC = System.getProperty("user.home") + "/.labspy/bin/labspy.sh";
        }

    }

}
