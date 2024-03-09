package org.xiangqian.auto.deploy.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiangqian
 * @date 21:40 2024/03/04
 */
@Data
public class IndexVo {

    // 项目id
    private Long itemId;

    // 项目名称
    private String itemName;

    // 仓库地址
    private String repoUrl;

    // 分支名
    private String branch;

    // 最近一次提交id
    private String commitId;

    // 最近一次提交作者
    private String commitAuthor;

    // 最近一次提交日期
    private String commitTime;

    // 最近一次提交信息
    private String commitMsg;

    // 最近一次部署用户id
    private Long userId;

    // 最近一次部署用户名
    private String userName;

    // 最近一次部署用户昵称
    private String userNickname;

    // 最近一次部署时间（单位s）
    private Long time;

    // 最近一次部署状态，0-部署中，1-部署成功，2-部署失败
    private Integer state;

    // 最近一次部署耗时（单位s）
    private Long duration;

    public String getCommitString() {
        return commitAuthor + "  " + commitTime + "  " + commitMsg;
    }

    public String getUserString() {
        if (StringUtils.isEmpty(userNickname)) {
            return userName;
        }
        return userName + " ( " + userNickname + " )";
    }

}
