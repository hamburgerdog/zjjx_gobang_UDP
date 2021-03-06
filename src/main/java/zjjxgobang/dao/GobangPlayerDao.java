package zjjxgobang.dao;

import org.springframework.stereotype.Repository;
import zjjxgobang.pojo.GobangPlayer;
import org.apache.ibatis.annotations.Param;


public interface GobangPlayerDao {
    public GobangPlayer searchPlayerByEmail(String email);

    public int registerPlayer(@Param("email")String email, @Param("pwd") String pwd);

    int updateWinNumByEmail(@Param("email") String email);

    int updateDefeatNumByEmail(@Param("email")String email);
}
