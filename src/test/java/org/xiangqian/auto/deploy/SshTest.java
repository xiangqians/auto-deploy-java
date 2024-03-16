package org.xiangqian.auto.deploy;

import org.apache.commons.io.IOUtils;
import org.xiangqian.auto.deploy.util.Ssh;

/**
 * @author xiangqian
 * @date 23:34 2024/03/14
 */
public class SshTest {

    public static void main(String[] args) throws Exception {
        String host = System.getenv("SSH_HOST");
        int port = Integer.parseInt(System.getenv("SSH_PORT"));
        String user = System.getenv("SSH_USER");
        String passwd = System.getenv("SSH_PASSWD");

        Ssh ssh = null;
        try {
            ssh = new Ssh(host, port, user, passwd);
            ssh.execute("cd /etc/ssh && pwd && ls -l", System.out::println);
        } finally {
            IOUtils.closeQuietly(ssh);
        }
    }

}
