package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 项目部署记录信息
 *
 * @author xiangqian
 * @date 21:34 2024/03/04
 */
@Data
@TableName("record")
public class RecordEntity {

    private static final long serialVersionUID = 1L;

    // id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 项目id
    private Long itemId;

    // 用户id
    private Long userId;

    // 用户名
    @TableField(exist = false)
    private String userName;

    // 用户昵称
    @TableField(exist = false)
    private String userNickname;

    // 状态，0-部署中，1-部署成功，2-部署失败
    private Integer state;

    // 创建时间（时间戳，s）
    private Long addTime;

    // 【从远程仓库拉取代码】开始时间（时间戳，s）
    private Long pullStime;

    // 【从远程仓库拉取代码】结束时间（时间戳，s）
    private Long pullEtime;

    // 【从远程仓库拉取代码】信息
    private String pullMsg;

    // 提交id
    private String commitId;

    // 提交作者
    private String commitAuthor;

    // 提交日期
    private String commitTime;

    // 提交信息
    private String commitMsg;

    // 【构建】开始时间（时间戳，s）
    private Long buildStime;

    // 【构建】结束时间（时间戳，s）
    private Long buildEtime;

    // 【构建】信息
    private String buildMsg;

    // 【打包】开始时间（时间戳，s）
    private Long packStime;

    // 【打包】结束时间（时间戳，s）
    private Long packEtime;

    // 【打包】信息
    private String packMsg;

    // 【上传到远程主机】开始时间（时间戳，s）
    private Long uploadStime;

    // 【上传到远程主机】结束时间（时间戳，s）
    private Long uploadEtime;

    // 【上传到远程主机】信息
    private String uploadMsg;

    // 【解压缩包】开始时间（时间戳，s）
    private Long unpackStime;

    // 【解压缩包】结束时间（时间戳，s）
    private Long unpackEtime;

    // 【解压缩包】信息
    private String unpackMsg;

    // 【部署】开始时间（时间戳，s）
    private Long deployStime;

    // 【部署】结束时间（时间戳，s）
    private Long deployEtime;

    // 【部署】信息
    private String deployMsg;

    // 部署耗时（单位s）
    @TableField(exist = false)
    private Long duration;

    // 【从远程仓库拉取代码】耗时（单位s）
    public Long getPullDuration() {
        return null;
    }

    // 【构建】耗时（单位s）
    public Long getBuildDuration() {
        return null;
    }

    // 【打包】耗时（单位s）
    public Long getPackDuration() {
        return null;
    }

    // 【上传到远程主机】耗时（单位s）
    public Long getUploadDuration() {
        return null;
    }

    // 【解压缩包】耗时（单位s）
    public Long getUnpackDuration() {
        return null;
    }

    // 【部署】耗时（单位s）
    public Long getDeployDuration() {
        return null;
    }

}
