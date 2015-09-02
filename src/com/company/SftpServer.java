package com.company;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;
import java.util.Arrays;

/**
 * Created by jgonzalez on 9/2/15.
 */
public class SftpServer {
    private static final int PORT = 3001;
    //private static final String HOST_KEY = "src/test/resources/hostkey.pem";
    private SshServer sshd;

    public SftpServer() {
        Security.addProvider(new BouncyCastleProvider());
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(PORT);
        //FileKeyPairProvider fileKeyPairProvider = new FileKeyPairProvider(new String[]{HOST_KEY});
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
        SftpSubsystem.Factory factory = new SftpSubsystem.Factory();
        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(factory));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setShellFactory(new ProcessShellFactory());
        sshd.setPasswordAuthenticator(PasswordAuthenticator());
    }

    private PasswordAuthenticator PasswordAuthenticator() {
        return new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String arg0, String arg1, ServerSession arg2) {
                return true;
            }};
    }

    public void start(){
        try {
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();}
        sshd = null;
    }
}