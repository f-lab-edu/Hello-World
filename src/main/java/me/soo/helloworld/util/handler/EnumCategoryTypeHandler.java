package me.soo.helloworld.util.handler;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.EnumCategory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    요청으로 들어온 ENUM 클래스의 상수 값을 MyBatis 를 통해 DB에 저장할 때, 해당 상수가 갖고 있는 값으로 변환해주는 TypeHandler 입니다.
    반대로 DB 에서 저장된 데이터를 가져올 때는 다시 해당 상수으로 변경해서 가져옵니다.

    각 ENUM 클래스마다 비슷한 내용을 가진 타입 핸들러의 중복구현을 피하기 위해서 Generic 과 상속 그리고 인터페이스와 활용해 하나의 타입 핸들러를
    해당 인터페이스를 구현한 ENUM 클래스들이 재사용할 수 있도록 하였습니다.
 */
@RequiredArgsConstructor
public class EnumCategoryTypeHandler<E extends Enum<E>> implements TypeHandler<EnumCategory> {

    private final Class<E> type;

    @Override
    public void setParameter(PreparedStatement ps, int i, EnumCategory parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCategory());
    }

    @Override
    public EnumCategory getResult(ResultSet rs, String columnName) throws SQLException {
        int category = rs.getInt(columnName);
        return getEnumCategory(category);
    }

    @Override
    public EnumCategory getResult(ResultSet rs, int columnIndex) throws SQLException {
        int category = rs.getInt(columnIndex);
        return getEnumCategory(category);
    }

    @Override
    public EnumCategory getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int category = cs.getInt(columnIndex);
        return getEnumCategory(category);
    }

    private EnumCategory getEnumCategory(int category) {
        try {
            EnumCategory[] enumConstants = (EnumCategory[]) type.getEnumConstants();
            for (EnumCategory enumCategory : enumConstants) {
                if (enumCategory.getCategory() == category) {
                    return enumCategory;
                }
            }

            return null;

        } catch (TypeException e) {
            throw new TypeException("해당 타입은 변환이 불가능 합니다.", e);
        }
    }
}
