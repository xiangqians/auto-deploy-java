package org.xiangqian.auto.deploy.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * JSch
 * http://www.jcraft.com/jsch
 * http://www.jcraft.com/jsch/examples
 * https://github.com/is/jsch
 * <p>
 * 第三方JSch（第三方维护的版本）
 * https://github.com/mwiede/jsch
 *
 * @author xiangqian
 * @date 23:02 2024/03/14
 */
@Slf4j
public class Ssh implements Closeable {

    private Session session;

    /**
     * @param host   服务器地址
     * @param port   服务器端口
     * @param user   用户
     * @param passwd 密码
     */
    public Ssh(String host, int port, String user, String passwd) throws Exception {
        JSch jsch = new JSch();

        // 支持服务器身份验证，设置 known_host 文件位置
//        jsch.setKnownHosts();

        // public key authentication
//        jsch.addIdentity("location to private key file");

        // 设置服务器地址、端口、用户名、密码
        session = jsch.getSession(user, host, port);
        session.setPassword(passwd);

        // 跳过公钥检测
        session.setConfig("StrictHostKeyChecking", "no");

        // 连接到服务器
        session.connect((int) Duration.ofMinutes(2).toMillis());
        Assert.isTrue(session.isConnected(), "Session连接失败");
    }

    /**
     * 执行单行命令
     *
     * @param command  命令
     * @param consumer
     * @throws Exception
     */
    public void execute(String command, Consumer<String> consumer) throws Exception {
        ChannelExec channel = null;
        try {
            // ChannelExec执行单行命令通道
            channel = (ChannelExec) session.openChannel("exec");

            // 输出流
            OutputStream outputStream = new OutputStream() {
                private byte[] bytes;
                private int index;

                @Override
                public synchronized void write(int b) throws IOException {
                    if (bytes == null) {
                        bytes = new byte[1024];
                        index = 0;
                    }

                    // 将有符号的 byte 转换为无符号的 int，避免负值带来的问题
                    int unsignedByte = b & 0xFF;
                    // 根据ASCII码判断是否是换行符
                    if (unsignedByte == 13 // \r
                            || unsignedByte == 10) { // \n
                        if (index > 0) {
                            consumer.accept(new String(bytes, 0, index));
                        }
                        index = 0;
                        return;
                    }

                    int length = bytes.length;
                    if (index == length) {
                        byte[] newBytes = new byte[length + 1024];
                        System.arraycopy(bytes, 0, newBytes, 0, length);
                        bytes = newBytes;
                    }
                    bytes[index++] = (byte) b;
                }
            };

            // 获取输入流
            InputStream inputStream = channel.getInputStream();

            // 设置普通输出流
            channel.setOutputStream(outputStream);

            // 设置异常输出流
            channel.setErrStream(outputStream);

            // 设置命令
            channel.setCommand(command);

            // 连接并执行命令
            /**
             * {@link Channel#sendChannelOpen()}
             * 设置指定时间，会导致 retry = 1，默认是 retry = 2000
             * 所以，此处便不设置channel timeout
             */
            channel.connect((int) Duration.ofMinutes(1).toMillis());
            if (!channel.isConnected()) {
                return;
            }

            // 读取输入流数据
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    consumer.accept(line);
                }
            } finally {
                IOUtils.closeQuietly(reader);
            }
        } finally {
            channel.disconnect();
        }
    }

    @Override
    public void close() throws IOException {
        if (session != null) {
            session.disconnect();
            session = null;
        }
    }

}
