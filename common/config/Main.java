package config;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by paladini on 4/12/15.
 *
 *  MUST run as sudo, otherwise it will not work.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Config config = Config.getInstance();
        JSONObject fullConfig = config.getFullConfig();

        System.out.println("Teste!");
        System.out.println("Size of the configuration file: " + fullConfig.size());

        // config.getAddress();
        // System.out.println("Will throw an exception");

        // Should create a configuration file at /var/lib/LabSpy/
        //config.setAddress("123.323.232.23");

        // Loads configs from the file.
        System.out.println("Loading data from file: " + config.getAddress());


    }

}
