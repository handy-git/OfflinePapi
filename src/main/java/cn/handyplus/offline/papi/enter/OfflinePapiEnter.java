package cn.handyplus.offline.papi.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.enums.IndexEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author handy
 */
@Getter
@Setter
@TableName(value = "offline_papi", comment = "离线变量数据")
public class OfflinePapiEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid")
    private String playerUuid;

    @TableField(value = "papi", comment = "变量", indexEnum = IndexEnum.INDEX)
    private String papi;

    @TableField(value = "vault", comment = "值")
    private String vault;

}
