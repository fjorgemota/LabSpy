package config;

import java.io.Serializable;

/**
 * Classe responsável por representar um computador (do estudante) no LabSpy. Cada máquina tem um endereço IP associada a ela.
 */
public class Computer implements Serializable {

    private String ip;
    private String label;

    public Computer(String ip) {
        this.ip = ip;
    }
    public Computer(String ip, String label) { this.ip = ip; this.label = label;  }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return this.ip;
    }
    public void setLabel(String label) { this.label = label; }
    public String getLabel() { return this.label; }
    
    public String toString() {
        return this.label.trim().isEmpty() ? this.ip : this.label;
    }

}
