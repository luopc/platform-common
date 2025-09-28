package com.luopc.platform.cloud.data.types;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Robin
 */
public class Int2BooleanTypeHandler extends BaseTypeHandler<Boolean> {
    /**
     * 功能描述: <br>
     * <>
     *
     * @param: [ps, i, parameter, jdbcType]
     * i:Jdbc预编译时设置参数的索引值
     * parameter:要插入的参数值  true 或者false
     * jdbcType:要插入JDBC的类型
     * 里面的业务逻辑要根据实际开发场景来写 我这里就写的简单一点比较好理解一下
     * @return:
     * @author: wlt
     * @date: 2022/3/22 21:25
     **/
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        if (parameter) {
            ps.setInt(i, 1);
        } else {
            ps.setInt(i, 0);
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int man = rs.getInt(columnName);
        return man == 1;
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int man = rs.getInt(columnIndex);
        return man == 1;
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int man = cs.getInt(columnIndex);
        return man == 1;
    }
}

