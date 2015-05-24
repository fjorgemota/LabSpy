package communication;

import config.Computer;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe responsável por conectar o computador do professor ao computador do estudante via SSH. Permite execução de comandos,
 * criação de pastas e transferência de arquivos via SSH.
 */
public class RemoteSSH {

    private Computer computer;
    private String username;
    private String password;
    private final Logger log = LoggerFactory.getLogger(RemoteSSH.class);

    public RemoteSSH(Computer computer, String username, String password) {
        this.computer = computer;
        this.username = username;
        this.password = password;
    }

    public void mkdir(String fullPath) throws IOException {
        SSHClient ssh = null;
        try {
            ssh = createConnection();
            final SFTPClient sftp = ssh.newSFTPClient();
            try {
                sftp.mkdir(fullPath);
            } finally {
                sftp.close();
            }
        } finally {
            ssh.disconnect();
        }
    }

    public void transferFile(String originFile, String destinationSrc) throws IOException {
        SSHClient ssh = null;
        try {
            ssh = createConnection();
            final SFTPClient sftp = ssh.newSFTPClient();
            try {
                sftp.put(new FileSystemFile(originFile), destinationSrc);
            } finally {
                sftp.close();
            }
        } finally {
            ssh.disconnect();
        }
    }

    public void executeCommand(final String command) throws IOException {
        SSHClient ssh = null;
        try {
            ssh = createConnection();
            executeCommandBySSH(ssh, command);
        } finally {
            ssh.disconnect();
        }
    }

    private SSHClient createConnection() throws IOException {
        final SSHClient ssh = new SSHClient();
        setupKeyVerifier(ssh);
        ssh.connect(computer.getIp());
        ssh.authPassword(username, password);
        return ssh;
    }

    private void executeCommandBySSH(final SSHClient ssh, final String command) throws ConnectionException, IOException, TransportException {

        final Session session = ssh.startSession();
        BufferedReader bf = null;

        try {
            final Session.Command cmd = session.exec(command);
            bf = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
            }
            cmd.join(1, TimeUnit.SECONDS);
        } finally {
            secureClose(bf);
            secureClose(session);
        }
    }

    private void setupKeyVerifier(final SSHClient ssh) {
        ssh.addHostKeyVerifier(
                new HostKeyVerifier() {
                    @Override
                    public boolean verify(String arg0, int arg1, PublicKey arg2) {
                        return true;
                    }
                });
    }

    private void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            log.error("Erro ao fechar recurso", ex);
        }
    }

}
