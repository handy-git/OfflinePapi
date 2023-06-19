package cn.handyplus.offline.papi.param;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 变量参数
 *
 * @author handy
 */
@Getter
@Setter
@Builder
public class OfflineParam {
    /**
     * 名称
     */
    private String playerName;

    /**
     * 变量
     */
    private String papi;

}
