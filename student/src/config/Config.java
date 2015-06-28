package config;

import others.Computer;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by paladini on 6/25/15.
 */
public class Config {

    private static Config singleton = null;
    private ArrayList<Computer> teacherComputers;

    private Config() {
        this.teacherComputers = new ArrayList<Computer>();
    }

    public static Config getInstance() {
        if (singleton == null) {
            singleton = new Config();
        }
        return singleton;
    }

    public boolean isTeacherIPValid(String ip) {

        boolean found = !checkAndParseFile();
        for (Computer computer: this.teacherComputers) {
            if (computer.getIp().equals(ip)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private void add(String ip) {
        this.teacherComputers.add(new Computer(ip));
    }

    private boolean checkAndParseFile() {
        this.teacherComputers.clear();
        String filePath = "/var/lib/LabSpy/addressList";
        File fileInstance = new File(filePath);
        if (!fileInstance.exists()) {
            System.err.println("The trusted IP list does not exist. Ignoring..");
            return false;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while (true) {
                String ip = br.readLine();
                if (ip == null) {
                    break;
                }
                ip = ip.trim();
                if (ip.startsWith("#")) {
                    continue;
                }
                this.add(ip);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !this.teacherComputers.isEmpty();
    }
}
