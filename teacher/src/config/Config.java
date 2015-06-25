package config;
/**
 * Esta classe é responsável por armazenar as configurações do LabSpy em um arquivo de objetos serializados.
 */

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Config {
    
    private String pathToSave;
    private static Config singleton = null;
    private List<Computer> computers;

    private Config() {
        computers = new ArrayList<Computer>();
        checkEnvironment();
        checkAndParseFile();
    }



    public static Config getInstance() {
        if (singleton == null) {
            singleton = new Config();
        }
        return singleton;
    }

    public void addComputer(Computer computer) {
        if (!computerExists(computer.getIp())) {
            this.computers.add(computer);
            saveFile();
        }
    }
    public void addComputer(int pos, Computer computer) {
        if (!computerExists(computer.getIp())) {
            this.computers.add(pos, computer);
            saveFile();
        }
    }
    public void replaceComputer(int pos, Computer computer) {
        this.computers.set(pos, computer);
        saveFile();
    }
    public void removeComputer(Computer computer) {
        this.computers.remove(computer);
        saveFile();
    }
    public void removeComputer(int pos) {
        this.computers.remove(pos);
        saveFile();
    }
    public Computer getComputer(String ip) {
        for(Computer c : this.computers) {
            if (c.getIp().equalsIgnoreCase(ip)) {
                return c;
            }
        }
        return null; // better to throw an exception?
    }
    public Computer getComputer(int pos) {
        return this.computers.get(pos);
    }
    public List<Computer> getComputers() {
        return this.computers;
    }
    
    public boolean firstTime() {
            File f = new File(pathToSave);
            if (!f.exists()) {
                saveFile();
                return true;
            }
            return false;
    }

    private void saveFile() {
        try {
            OutputStream buffer = new BufferedOutputStream(new FileOutputStream(pathToSave));
            ObjectOutput output = new ObjectOutputStream(buffer);

           try {
               output.writeObject(computers);
           } finally {
               output.close();
           }
            output.flush();
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void checkAndParseFile() {
        try {
            InputStream buffer = new BufferedInputStream(new FileInputStream(pathToSave));
            ObjectInput input = new ObjectInputStream(buffer);

            try {
                computers = (ArrayList<Computer>) input.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkEnvironment() {
        File f = new File("out/artifacts/Teacher/Teacher.jar");
        if (!f.exists()) { // is in production environment
            pathToSave = System.getProperty("user.home") + "/.labspy/config.obj";
        } else { // is in test / development environment
            pathToSave = "config.obj";
        }
    }

    private boolean computerExists(String ip) {
        return getComputer(ip) != null;
    }

}
