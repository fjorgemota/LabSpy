package config;

import java.io.Serializable;

/**
 * This class represents a Computer on the class. Each instance will have an associated IP address (that can be used for many things).
 * Created by paladini on 5/21/15.
 */
public class Computer implements Serializable {

    private String ip;

    Computer(String ip) {
        this.ip = ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

}
