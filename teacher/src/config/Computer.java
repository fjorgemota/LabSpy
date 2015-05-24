package config;

import java.io.Serializable;

/**
 * Classe responsável por representar um computador (do estudante) no LabSpy. Cada máquina tem um endereço IP associada a ela.
 */
public class Computer implements Serializable {

    private String ip;

    public Computer(String ip) {
        this.ip = ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }
    
    public String toString() {
        return this.ip;
    }

}
