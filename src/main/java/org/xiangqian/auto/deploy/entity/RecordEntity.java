package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;

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

    // 【上传】开始时间（时间戳，s）
    private Long uploadStime;

    // 【上传】结束时间（时间戳，s）
    private Long uploadEtime;

    // 【上传】信息
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

    // 【从远程仓库拉取代码】状态
    public Integer getPullState() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (pullEtime > 0) {
                return 1;
            }
            if (pullStime > 0) {
                return 0;
            }
            return null;
        }
        // 1-部署成功
        else if (state == 1) {
            return 1;
        }
        // 2-部署失败
        else if (state == 2) {
            return buildStime > 0 ? 1 : 2;
        }
        return null;
    }

    // 【从远程仓库拉取代码】耗时（单位s）
    public Long getPullDuration() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (pullEtime != 0) {
                return pullEtime - pullStime;
            }
            if (pullStime != 0) {
                return DateUtil.toSecond(LocalDateTime.now()) - pullStime;
            }
            return 0L;
        }
        // 1-部署成功
        // 2-部署失败
        else if (state == 1 || state == 2) {
            return pullEtime - pullStime;
        }
        return null;
    }

    // 【构建】状态
    public Integer getBuildState() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (buildEtime > 0) {
                return 1;
            }
            if (buildStime > 0) {
                return 0;
            }
            return null;
        }
        // 1-部署成功
        else if (state == 1) {
            return 1;
        }
        // 2-部署失败
        else if (state == 2) {
            return packStime > 0 ? 1 : 2;
        }
        return null;
    }

    // 【构建】耗时（单位s）
    public Long getBuildDuration() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (buildEtime != 0) {
                return buildEtime - buildStime;
            }
            if (buildStime != 0) {
                return DateUtil.toSecond(LocalDateTime.now()) - buildStime;
            }
            return 0L;
        }
        // 1-部署成功
        // 2-部署失败
        else if (state == 1 || state == 2) {
            return buildEtime - buildStime;
        }
        return null;
    }

    // 【打包】状态
    public Integer getPackState() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (packEtime > 0) {
                return 1;
            }
            if (packStime > 0) {
                return 0;
            }
            return null;
        }
        // 1-部署成功
        else if (state == 1) {
            return 1;
        }
        // 2-部署失败
        else if (state == 2) {
            return uploadStime > 0 ? 1 : 2;
        }
        return null;
    }

    // 【打包】耗时（单位s）
    public Long getPackDuration() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (packEtime != 0) {
                return packEtime - packStime;
            }
            if (packStime != 0) {
                return DateUtil.toSecond(LocalDateTime.now()) - packStime;
            }
            return 0L;
        }
        // 1-部署成功
        // 2-部署失败
        else if (state == 1 || state == 2) {
            return packEtime - packStime;
        }
        return null;
    }

    // 【上传】状态
    public Integer getUploadState() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (uploadEtime > 0) {
                return 1;
            }
            if (uploadStime > 0) {
                return 0;
            }
            return null;
        }
        // 1-部署成功
        else if (state == 1) {
            return 1;
        }
        // 2-部署失败
        else if (state == 2) {
            return unpackStime > 0 ? 1 : 2;
        }
        return null;
    }

    // 【上传】耗时（单位s）
    public Long getUploadDuration() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (uploadEtime != 0) {
                return uploadEtime - uploadStime;
            }
            if (uploadStime != 0) {
                return DateUtil.toSecond(LocalDateTime.now()) - uploadStime;
            }
            return 0L;
        }
        // 1-部署成功
        // 2-部署失败
        else if (state == 1 || state == 2) {
            return uploadEtime - uploadStime;
        }
        return null;
    }

    // 【解压缩包】状态
    public Integer getUnpackState() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (unpackEtime > 0) {
                return 1;
            }
            if (unpackStime > 0) {
                return 0;
            }
            return null;
        }
        // 1-部署成功
        else if (state == 1) {
            return 1;
        }
        // 2-部署失败
        else if (state == 2) {
            return deployStime > 0 ? 1 : 2;
        }
        return null;
    }

    // 【解压缩包】耗时（单位s）
    public Long getUnpackDuration() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (unpackEtime != 0) {
                return unpackEtime - unpackStime;
            }
            if (unpackStime != 0) {
                return DateUtil.toSecond(LocalDateTime.now()) - unpackStime;
            }
            return 0L;
        }
        // 1-部署成功
        // 2-部署失败
        else if (state == 1 || state == 2) {
            return unpackEtime - unpackStime;
        }
        return null;
    }

    // 【解压缩包】状态
    public Integer getDeployState() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (deployEtime > 0) {
                return 1;
            }
            if (deployStime > 0) {
                return 0;
            }
            return null;
        }
        // 1-部署成功
        else if (state == 1) {
            return 1;
        }
        // 2-部署失败
        else if (state == 2) {
            return 2;
        }
        return null;
    }

    // 【部署】耗时（单位s）
    public Long getDeployDuration() {
        if (state == null) {
            return null;
        }

        // 0-部署中
        if (state == 0) {
            if (deployEtime != 0) {
                return deployEtime - deployStime;
            }
            if (deployStime != 0) {
                return DateUtil.toSecond(LocalDateTime.now()) - deployStime;
            }
            return 0L;
        }
        // 1-部署成功
        // 2-部署失败
        else if (state == 1 || state == 2) {
            return deployEtime - deployStime;
        }
        return null;
    }

    // 部署耗时
    public Long getDuration() {
        if (state == null) {
            return null;
        }

        long duration = 0;
        duration += getPullDuration();
        duration += getBuildDuration();
        duration += getPackDuration();
        duration += getUploadDuration();
        duration += getUnpackDuration();
        duration += getDeployDuration();
        return duration;
    }

}
