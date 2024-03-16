package org.xiangqian.auto.deploy;

import org.apache.commons.io.IOUtils;
import org.xiangqian.auto.deploy.util.Sftp;

/**
 * @author xiangqian
 * @date 23:35 2024/03/14
 */
public class SftpTest {

    public static void main(String[] args) throws Exception {
        String host = System.getenv("SSH_HOST");
        int port = Integer.parseInt(System.getenv("SSH_PORT"));
        String user = System.getenv("SSH_USER");
        String passwd = System.getenv("SSH_PASSWD");

        Sftp sftp = null;
        try {
            sftp = new Sftp(host, port, user, passwd);

//            sftp.ls("/").forEach(System.out::println);
//            System.out.println();

//            sftp.ls("./").forEach(System.out::println);
//            System.out.println();

//            sftp.cd("/etc/ssh");

//            sftp.ls("./").forEach(System.out::println);
//            System.out.println();

//            sftp.put("E:\\tmp\\TeamViewer.zip", "./", System.out::println);

            sftp.get("./TeamViewer.zip", "E:\\tmp\\", System.out::println);
        } finally {
            IOUtils.closeQuietly(sftp);
        }
    }

}
