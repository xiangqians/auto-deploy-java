package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Optional;

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

    // 项目名称
    @TableField(exist = false)
    private String itemName;

    // 仓库地址
    @TableField(exist = false)
    private String uri;

    // 分支名
    @TableField(exist = false)
    private String branch;

    // 部署用户id
    private Long userId;

    // 用户名
    @TableField(exist = false)
    private String userName;

    // 用户昵称
    @TableField(exist = false)
    private String userNickname;

    // 创建时间（时间戳，单位s）
    private Long addTime;

    // 【拉取】开始时间（时间戳，单位s）
    private Long pullStime;

    // 【拉取】结束时间（时间戳，单位s）
    private Long pullEtime;

    // 【拉取】信息
    private String pullMsg;

    // 【拉取】状态，0-待拉取，1-拉取中，2-拉取成功，3-拉取失败
    private Integer pullState;

    // 提交id
    private String commitId;

    // 提交作者
    private String commitAuthor;

    // 提交日期
    private String commitTime;

    // 提交信息
    private String commitMsg;

    // 【构建】开始时间（时间戳，单位s）
    private Long buildStime;

    // 【构建】结束时间（时间戳，单位s）
    private Long buildEtime;

    // 【构建】信息
    private String buildMsg;

    // 【构建】状态，0-待构建，1-构建中，2-构建成功，3-构建失败
    private Integer buildState;

    // 【压缩】开始时间（时间戳，单位s）
    private Long packStime;

    // 【压缩】结束时间（时间戳，单位s）
    private Long packEtime;

    // 【压缩】信息
    private String packMsg;

    // 【压缩】状态，0-待压缩，1-压缩中，2-压缩成功，3-压缩失败
    private Integer packState;

    // 【上传】开始时间（时间戳，单位s）
    private Long uploadStime;

    // 【上传】结束时间（时间戳，单位s）
    private Long uploadEtime;

    // 【上传】信息
    private String uploadMsg;

    // 【上传】状态，0-待上传，1-上传中，2-上传成功，3-上传失败
    private Integer uploadState;

    // 【解压】开始时间（时间戳，单位s）
    private Long unpackStime;

    // 【解压】结束时间（时间戳，单位s）
    private Long unpackEtime;

    // 【解压】信息
    private String unpackMsg;

    // 【解压】状态，0-待解压，1-解压中，2-解压成功，3-解压失败
    private Integer unpackState;

    // 【部署】开始时间（时间戳，单位s）
    private Long deployStime;

    // 【部署】结束时间（时间戳，单位s）
    private Long deployEtime;

    // 【部署】信息
    private String deployMsg;

    // 【部署】状态，0-待部署，1-部署中，2-部署成功，3-部署失败
    private Integer deployState;

    // 【拉取】耗时（单位s）
    public Long getPullDuration() {
        // 待拉取
        if (pullState == null || pullState == 0) {
            return null;
        }

        // 拉取中
        if (pullState == 1) {
            return DateUtil.toSecond(LocalDateTime.now()) - pullStime;
        }

        // 拉取成功
        if (pullState == 2
                // 拉取失败
                || pullState == 3) {
            return pullEtime - pullStime;
        }

        return null;
    }

    // 【构建】耗时（单位s）
    public Long getBuildDuration() {
        // 待构建
        if (buildState == null || buildState == 0) {
            return null;
        }

        // 构建中
        if (buildState == 1) {
            return DateUtil.toSecond(LocalDateTime.now()) - buildStime;
        }

        // 构建成功
        if (buildState == 2
                // 构建失败
                || buildState == 3) {
            return buildEtime - buildStime;
        }

        return null;
    }

    // 【压缩】耗时（单位s）
    public Long getPackDuration() {
        // 待压缩
        if (packState == null || packState == 0) {
            return null;
        }

        // 压缩中
        if (packState == 1) {
            return DateUtil.toSecond(LocalDateTime.now()) - packStime;
        }

        // 压缩成功
        if (packState == 2
                // 压缩失败
                || packState == 3) {
            return packEtime - packStime;
        }

        return null;
    }

    // 【上传】耗时（单位s）
    public Long getUploadDuration() {
        // 待上传
        if (uploadState == null || uploadState == 0) {
            return null;
        }

        // 上传中
        if (uploadState == 1) {
            return DateUtil.toSecond(LocalDateTime.now()) - uploadStime;
        }

        // 上传成功
        if (uploadState == 2
                // 上传失败
                || uploadState == 3) {
            return uploadEtime - uploadStime;
        }

        return null;
    }

    // 【解压】耗时（单位s）
    public Long getUnpackDuration() {
        if (unpackState == null || unpackState == 0) {
            return null;
        }

        // 解压中
        if (unpackState == 1) {
            return DateUtil.toSecond(LocalDateTime.now()) - unpackStime;
        }

        // 解压成功
        if (unpackState == 2
                // 解压失败
                || unpackState == 3) {
            return unpackEtime - unpackStime;
        }

        return null;
    }

    // 【部署】耗时（单位s）
    public Long getDeployDuration() {
        // 待部署
        if (deployState == null || deployState == 0) {
            return null;
        }

        // 部署中
        if (deployState == 1) {
            return DateUtil.toSecond(LocalDateTime.now()) - deployStime;
        }

        // 部署成功
        if (deployState == 2
                // 部署失败
                || deployState == 3) {
            return deployEtime - deployStime;
        }

        return null;
    }

    // 状态，0-待部署，1-部署中，2-部署成功，3-部署失败
    public Integer getState() {
        if (deployState != null && deployState != 0) {
            return deployState;
        }

        if (unpackState != null && unpackState != 0) {
            return unpackState;
        }

        if (uploadState != null && uploadState != 0) {
            return uploadState;
        }

        if (packState != null && packState != 0) {
            return packState;
        }

        if (buildState != null && buildState != 0) {
            return buildState;
        }

        if (pullState != null && pullState != 0) {
            return pullState;
        }

        // 是否存在部署记录
        return id != null
                ? 0  // 存在部署记录
                : null; // 不存在部署记录
    }

    // 耗时
    public Long getDuration() {
        long duration = 0L;
        duration += Optional.ofNullable(getPullDuration()).orElse(0L);
        duration += Optional.ofNullable(getBuildDuration()).orElse(0L);
        duration += Optional.ofNullable(getPackDuration()).orElse(0L);
        duration += Optional.ofNullable(getUploadDuration()).orElse(0L);
        duration += Optional.ofNullable(getUnpackDuration()).orElse(0L);
        duration += Optional.ofNullable(getDeployDuration()).orElse(0L);
        return duration;
    }

}
