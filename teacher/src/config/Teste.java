package config;

/**
 * Created by paladini on 6/21/15.
 */
public class Teste {
    public static void main (String[] args) {

        Computer c = new Computer("192.168.0.13");
        SetupLinux setup = new SetupLinux(c, "root", "sp95fccod2");

        setup.install();

    }

}
