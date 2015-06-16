/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacher.src.config;

import java.io.IOException;
import teacher.src.communication.RemoteSSH;

/**
 * This class is responsible for setting-up a student client on a remote machine. 
 * It will create folders, transfer files and run commands over an SSH connection to install the student client on a remote machine.
 * @author paladini
 */
public class SetupLinux extends Setup {
    
    private final String pathToInstall = "/var/lib/LabSpy/";
    private final String clientSRC = "out/artifacts/Student/Student.jar";
    private final String scriptSRC = "assets/labspy.sh";

    public SetupLinux(RemoteSSH ssh) {
        super(ssh);
    }
    
    public SetupLinux(Computer c, String username, String password) {
        super(c, username, password);
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
            ssh.executeCommand("sudo ln -s /var/lib/LabSpy/labspy.sh /etc/init.d/labspy_client");
            ssh.executeCommand("sudo chmod +x /etc/init.d/labspy_client");
            ssh.executeCommand("sudo update-rc.d labspy_client defaults");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    String getPathToInstall() {
        return this.pathToInstall;
    }

    @Override
    String getClientSRC() {
        return this.clientSRC;
    }
    
    
}
