package org.xiangqian.auto.deploy;

import org.xiangqian.auto.deploy.util.Git;

import java.io.File;
import java.util.List;

/**
 * @author xiangqian
 * @date 21:59 2024/03/12
 */
public class GitTest {

    public static void main(String[] args) throws Exception {
        File directory = new File(System.getenv("GIT_DIRECTORY"));
        String uri = System.getenv("GIT_URI");
        String branch = System.getenv("GIT_BRANCH");
        String user = System.getenv("GIT_USER");
        String passwd = System.getenv("GIT_PASSWD");

        // clone
//        Git git = Git.clone(directory, uri, branch, user, passwd);

        // open
        Git git = Git.open(directory, user, passwd);

        // branch
        System.out.println(git.branch());
        System.out.println();

        // log
        Runnable log = () -> {
            try {
                List<Git.Commit> commits = git.log(10);
                commits.forEach(System.out::println);
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        log.run();

        // pull
        git.pull();

        // reset
        git.reset("6d9af206ad336f949a3ba6803c32dd459a61efdf");

        // log
        log.run();
    }

}