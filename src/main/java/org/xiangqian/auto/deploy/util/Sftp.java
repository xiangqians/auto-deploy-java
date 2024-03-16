package org.xiangqian.auto.deploy.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpProgressMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 23:03 2024/03/14
 */
@Slf4j
public class Sftp implements Closeable {

    private Session session;
    private ChannelSftp channel;

    /**
     * @param host   服务器地址
     * @param port   服务器端口
     * @param user   用户
     * @param passwd 密码
     */
    public Sftp(String host, int port, String user, String passwd) throws Exception {
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

        // ChannelSftp上传、下载文件
        channel = (ChannelSftp) session.openChannel("sftp");

        // ChannelSftp连接
        channel.connect((int) Duration.ofMinutes(2).toMillis());
        Assert.isTrue(channel.isConnected(), "SftpChannel连接失败");
    }

    /**
     * 查询指定目录下的文件列表
     * <p>
     * 文件条目，示例：
     * drwxr-xr-x    2 root     root         4096 Jul  7 15:47 test
     * <p>
     * 解析：
     * 1、文件类型（d）
     * -：普通文件
     * d：目录文件
     * p：管理文件
     * l；链接文件
     * b：块设备文件
     * c：字符设备文件
     * s：套接字文件
     * <p>
     * 2、文件权限（rwxr-xr-x）
     * r：读权限
     * w：写权限
     * x：可执行权限
     * -：无权限
     * [第1组]：拥有者权限
     * [第2组]：组用户权限
     * [第3组]：其他用户权限
     * <p>
     * 3、数量（2）
     * 1）对于普通文件：链接数
     * 2）对于目录文件：第一级子目录数
     * <p>
     * 4、拥有者（root）
     * <p>
     * 5、组（root）
     * <p>
     * 6、文件大小（4096）
     * <p>
     * 7、最后修改时间（Jul  7 15:47）
     * <p>
     * 8、文件名（test）
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<String> ls(String path) throws Exception {
        Vector<?> vector = channel.ls(path);
        if (CollectionUtils.isEmpty(vector)) {
            return Collections.emptyList();
        }
        return vector.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * 进入指定目录
     *
     * @param path
     * @throws Exception
     */
    public void cd(String path) throws Exception {
        channel.cd(path);
    }

    /**
     * 创建目录
     *
     * @param path
     * @throws Exception
     */
    public void mkdir(String path) throws Exception {
        channel.mkdir(path);
    }

    /**
     * 删除普通文件、目录文件等等
     *
     * @param path
     * @throws Exception
     */
    void rm(String path) throws Exception {
        try {
            // 删除普通文件
            channel.rm(path);
        } catch (Exception e) {
            log.warn("删除普通文件异常", e);
            // 删除目录文件（只允许删除空目录）
            channel.rmdir(path);
        }
    }

    /**
     * 上传文件
     *
     * @param src      本地源文件
     * @param dst      远程服务器目标文件
     * @param consumer
     * @throws Exception
     */
    public void put(String src, String dst, Consumer<String> consumer) throws Exception {
        channel.put(src, dst,
                // 监控 SFTP 文件传输的进度
                new SftpProgressMonitorImpl(consumer),
                // 文件传输模式
                // 0，完全覆盖模式，这是JSch的默认文件传输模式，即如果目标文件已经存在，传输的文件将完全覆盖目标文件，产生新的文件。
                // 1，恢复模式，如果文件已经传输一部分，这时由于网络或其他任何原因导致文件传输中断，如果下一次传输相同的文件，则会从上一次中断的地方续传。
                // 2，追加模式，如果目标文件已存在，传输的文件将在目标文件后追加。
                0);
    }

    /**
     * 下载文件
     *
     * @param src      远程服务器源文件
     * @param dst      本地目标文件
     * @param consumer
     * @throws Exception
     */
    public void get(String src, String dst, Consumer<String> consumer) throws Exception {
        channel.get(src, dst, new SftpProgressMonitorImpl(consumer), 0);
    }

    @Override
    public void close() throws IOException {
        try {
            if (channel != null) {
                channel.disconnect();
                channel = null;
            }
        } finally {
            if (session != null) {
                session.disconnect();
                session = null;
            }
        }
    }

    // 监控 SFTP 文件传输的进度
    static class SftpProgressMonitorImpl implements SftpProgressMonitor {
        // 目标文件
        private String dest;

        // 文件总大小
        private long max;

        // 已传输大小
        private long count;

        private Consumer<String> consumer;

        SftpProgressMonitorImpl(Consumer<String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void init(int op, String src, String dest, long max) {
            this.dest = dest;
            this.max = max;
            consumer.accept(String.format("%s %s (%s) to %s",
                    (op == PUT ? "Uploading" : (op == GET ? "Fetching" : "")),
                    src,
                    OsUtil.humanByte(max, "KB"),
                    dest));
        }

        @Override
        public boolean count(long count) {
            this.count += count;
            consumer.accept(String.format("%s %.2f%% %s", dest, (this.count / (max * 1f) * 100), OsUtil.humanByte(this.count, "KB")));
            return true;
        }

        @Override
        public void end() {
        }
    }

}
