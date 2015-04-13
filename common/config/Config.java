package config;
/**
 * Created by paladini on 4/11/15.
 *
 * The path chosen to write/read program config is based on the following answers on SO:
 *      http://stackoverflow.com/a/1510357/2127383
 *      http://stackoverflow.com/a/1510352/2127383
 *      http://stackoverflow.com/a/2243910/2127383
 */

import java.io.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class Config {
    
    private String pathToSave = null;
    private JSONObject configs = null;
    private static Config singleton = null;

    private Config() {
        discoverPathToSave();
        checkAndParseFile();
    }

    public static Config getInstance() {
        if(singleton == null) {
            singleton = new Config();
        }
        return singleton;
    }

    public String getAddress() throws FileNotFoundException {
        checkConfigSize();
        return (String) configs.get("hostname");
    }

    public void setAddress(String address) {
        configs.put("hostname", address);
        saveFile();
    }

    public JSONObject getFullConfig() {
        return configs;
    }

    private void saveFile() {
        try {
            File file = new File(pathToSave);

            // Create the folders.
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            fw.write(configs.toJSONString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void discoverPathToSave() {
        if(System.getProperty("os.name").startsWith("Linux")) {
            pathToSave = "/var/lib/LabSpy/config.json";
        } else {
            pathToSave = "%APPDATA%\\LabSpy\\config.json";
        }
    }

    private void checkAndParseFile() {
        try {
            JSONParser parser = new JSONParser();
            configs = (JSONObject) parser.parse(new FileReader(this.pathToSave));
        } catch (FileNotFoundException e) {
            configs = new JSONObject();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void checkConfigSize() throws FileNotFoundException {
        if (configs.size() == 0) {
            throw new FileNotFoundException();
        }
    }



}
