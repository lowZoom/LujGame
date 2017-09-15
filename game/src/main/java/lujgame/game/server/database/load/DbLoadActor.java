package lujgame.game.server.database.load;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableSet;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.server.database.load.message.DbLoadSetReq;
import lujgame.game.server.database.load.message.DbLoadSetRsp;

public class DbLoadActor extends CaseActor {

  public DbLoadActor(DbLoadActorState state) {
    _state = state;

    addCase(DbLoadSetReq.class, this::onLoadSet);
  }

  private void onLoadSet(DbLoadSetReq msg) {
    String cacheKey = msg.getKey();
    String[] keyInfo = cacheKey.split("#");

    String tableName = keyInfo[0];
    String sql = "select val from `" + tableName + "` where key='" + keyInfo[1] + '\'';

    log().debug("数据库IO：{}", sql);

    HikariDataSource dataSource = _state.getDataSource();

    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql)) {

      ImmutableSet.Builder<Long> builder = ImmutableSet.builder();

      while (result.next()) {
        Long objId = result.getLong(1);
        builder.add(objId);
      }

      DbLoadSetRsp rsp = new DbLoadSetRsp(cacheKey, builder.build());
      ActorRef cacheRef = getSender();

      cacheRef.tell(rsp, getSelf());

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private final DbLoadActorState _state;
}
