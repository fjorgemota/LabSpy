package config;
/**
 * Created by paladini on 4/11/15.
 *
 * The path chosen to write/read program config is based on the following answers on SO:
 *      http://stackoverflow.com/a/1510357/2127383
 *      http://stackoverflow.com/a/1510352/2127383
 *      http://stackoverflow.com/a/2243910/2127383
 */

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Config {
    
    private final String pathToSave = "config.obj";
    private static Config singleton = null;
    private List<Computer> computers;

    private Config() {
        computers = new ArrayList<Computer>();
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

    private boolean computerExists(String ip) {
        return getComputer(ip) != null;
    }

}
