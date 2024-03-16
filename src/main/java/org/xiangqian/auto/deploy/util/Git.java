package org.xiangqian.auto.deploy.util;

import lombok.Data;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * jgit demo
 * https://github.com/centic9/jgit-cookbook
 *
 * @author xiangqian
 * @date 23:20 2024/03/13
 */
public class Git {

    private org.eclipse.jgit.api.Git git;
    private CredentialsProvider credentialsProvider;

    /**
     * @param git                 JGit
     * @param credentialsProvider 凭证
     */
    private Git(org.eclipse.jgit.api.Git git, CredentialsProvider credentialsProvider) {
        this.git = git;
        this.credentialsProvider = credentialsProvider;
    }

    /**
     * $ git clone <uri> -b <branch>
     *
     * @param directory 本地目录
     * @param uri       仓库地址
     * @param branch    分支
     * @param user      用户
     * @param passwd    密码
     * @return
     * @throws Exception
     */
    public static Git clone(File directory, String uri, String branch, String user, String passwd) throws Exception {
        // 用户、密码凭证
        CredentialsProvider credentialsProvider = null;
        if (user != null && passwd != null) {
            credentialsProvider = new UsernamePasswordCredentialsProvider(user, passwd);
        }

        // JGit
        org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.cloneRepository()
                // 本地目录
                .setDirectory(directory)
                // 仓库地址
                .setURI(uri)
                // 分支名
                .setBranch(branch)
                // 用户、密码凭证
                .setCredentialsProvider(credentialsProvider)
                .call();

        return new Git(git, credentialsProvider);
    }

    /**
     * @param directory 本地目录
     * @param user      用户
     * @param passwd    密码
     * @return
     * @throws Exception
     */
    public static Git open(File directory, String user, String passwd) throws Exception {
        // 用户、密码凭证
        CredentialsProvider credentialsProvider = null;
        if (user != null && passwd != null) {
            credentialsProvider = new UsernamePasswordCredentialsProvider(user, passwd);
        }

        return new Git(org.eclipse.jgit.api.Git.open(directory), credentialsProvider);
    }

    /**
     * $ git log -<maxCount>
     *
     * @param maxCount
     * @return
     * @throws Exception
     */
    public List<Commit> log(int maxCount) throws Exception {
        Iterable<RevCommit> revCommitIterable = git.log()
                .setMaxCount(maxCount)
                .call();
        List<Commit> commits = new ArrayList<>(maxCount);
        Iterator<RevCommit> revCommitIterator = revCommitIterable.iterator();
        while (revCommitIterator.hasNext()) {
            RevCommit revCommit = revCommitIterator.next();
            Commit commit = new Commit();

            // 提交id
            commit.setId(revCommit.getName());

            // 提交作者
            PersonIdent authorIdent = revCommit.getAuthorIdent();
            commit.setAuthor(String.format("%s <%s>", authorIdent.getName(), authorIdent.getEmailAddress()));

            // 提交日期
            commit.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(revCommit.getCommitTime()), ZoneId.systemDefault()));

            // 提交信息
            commit.setMsg(revCommit.getShortMessage());

            commits.add(commit);
        }
        return commits;
    }

    // 提交信息
    @Data
    public static class Commit {
        // 提交id
        private String id;
        // 提交作者
        private String author;
        // 提交日期
        private LocalDateTime time;
        // 提交信息
        private String msg;
    }

    /**
     * $ git branch
     *
     * @return
     * @throws Exception
     */
    public String branch() throws Exception {
        return git.getRepository().getBranch();
    }

    /**
     * $ git pull
     *
     * @throws Exception
     */
    public void pull() throws Exception {
        PullResult result = git.pull()
                .setRemoteBranchName(branch())
                .setCredentialsProvider(credentialsProvider)
                .call();
    }

    /**
     * $ git reset --hard <commit-id>
     *
     * @throws Exception
     */
    public void reset(String commitId) throws Exception {
        Ref ref = git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .setRef(git.getRepository().resolve(commitId).getName())
                .call();
    }

}
