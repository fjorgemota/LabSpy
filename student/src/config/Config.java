package config;

import others.Computer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paladini on 6/25/15.
 */
public class Config {

    private static Config singleton = null;
    private Computer teacherComputer;

    private Config() {
        checkAndParseFile();
    }

    public static Config getInstance() {
        if (singleton == null) {
            singleton = new Config();
        }
        return singleton;
    }

    public String getTeacherIP() {
        return this.teacherComputer.getIp();
    }

    private void checkAndParseFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("/var/lib/LabSpy/addressList"))) {
            String sCurrentLine = br.readLine();
            teacherComputer = new Computer(br.readLine(), "Teacher's Computer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
