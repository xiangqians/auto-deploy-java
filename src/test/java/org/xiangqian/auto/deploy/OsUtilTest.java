package org.xiangqian.auto.deploy;

import org.junit.Test;
import org.xiangqian.auto.deploy.util.OsUtil;

/**
 * @author xiangqian
 * @date 21:54 2024/03/15
 */
public class OsUtilTest {

    @Test
    public void test() {
        System.out.println(OsUtil.WINDOWS);
        System.out.println(OsUtil.LINUX);
    }

    @Test
    public void execute() throws Exception {
        OsUtil.execute("java1 -version", System.out::println);
        OsUtil.execute("cd D:\\workspace-my\\auto-deploy && mvn package", System.out::println);
    }

}
