package cn.handyplus.offline.papi.service;

import cn.handyplus.lib.db.Db;
import cn.handyplus.offline.papi.enter.OfflinePapiEnter;

/**
 * 玩家papi数据
 *
 * @author handy
 */
public class OfflinePapiService {
    private OfflinePapiService() {
    }

    private static class SingletonHolder {
        private static final OfflinePapiService INSTANCE = new OfflinePapiService();
    }

    public static OfflinePapiService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 新增或更新数据
     *
     * @param enter 记录
     */
    public void saveOrUpdate(OfflinePapiEnter enter) {
        OfflinePapiEnter offlinePapiEnter = this.findByPlayerUuidAndPapi(enter.getPlayerUuid(), enter.getPapi());
        if (offlinePapiEnter == null) {
            this.add(enter);
            return;
        }
        this.update(enter);
    }

    /**
     * 新增
     *
     * @param enter 入参
     */
    private void add(OfflinePapiEnter enter) {
        Db.use(OfflinePapiEnter.class).execution().insert(enter);
    }

    /**
     * 根据玩家名查询
     *
     * @param playerName 玩家名称
     * @param papi       变量
     * @return 数据
     */
    public OfflinePapiEnter findByPlayerUuidAndPapi(String playerName, String papi) {
        Db<OfflinePapiEnter> use = Db.use(OfflinePapiEnter.class);
        use.where().eq(OfflinePapiEnter::getPlayerName, playerName)
                .eq(OfflinePapiEnter::getPapi, papi);
        return use.execution().selectOne();
    }

    /**
     * 根据玩家uid更新
     *
     * @param enter 入参
     */
    private void update(OfflinePapiEnter enter) {
        Db<OfflinePapiEnter> use = Db.use(OfflinePapiEnter.class);
        use.update().set(OfflinePapiEnter::getVault, enter.getVault());
        use.where().eq(OfflinePapiEnter::getPlayerUuid, enter.getPlayerUuid())
                .eq(OfflinePapiEnter::getPapi, enter.getPapi());
        use.execution().update();
    }

}