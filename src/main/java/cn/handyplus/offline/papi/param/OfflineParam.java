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
     * 变量 有%
     */
    private String papi;

    /**
     * 变量 无%
     */
    private String papiType;

}
