package me.soo.helloworld.util.handler

import me.soo.helloworld.enumeration.EnumCategory
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.TypeException
import org.apache.ibatis.type.TypeHandler
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/*
    요청으로 들어온 ENUM 클래스의 상수 값을 MyBatis 를 통해 DB에 저장할 때, 해당 상수가 갖고 있는 Category 값으로 변환해주는 TypeHandler 입니다.
    반대로 DB 에서 저장된 데이터를 가져올 때는 다시 해당 상수로 변경해서 가져옵니다.

    각 ENUM 클래스마다 비슷한 내용을 가진 타입 핸들러의 중복구현을 피하기 위해서 Generic 과 상속 그리고 인터페이스와 활용해 하나의 타입 핸들러를
    해당 인터페이스를 구현한 ENUM 클래스들이 재사용할 수 있도록 하였습니다.
 */
open class EnumCategoryTypeHandler<E : Enum<E>>(val type: Class<E>) : TypeHandler<EnumCategory> {

    override fun setParameter(ps: PreparedStatement, i: Int, parameter: EnumCategory, jdbcType: JdbcType?) =
        ps.setInt(i, parameter.getCategory())

    override fun getResult(rs: ResultSet, columnName: String) = getEnumCategory(rs.getInt(columnName))

    override fun getResult(rs: ResultSet, columnIndex: Int) = getEnumCategory(rs.getInt(columnIndex))

    override fun getResult(cs: CallableStatement, columnIndex: Int) = getEnumCategory(cs.getInt(columnIndex))

    private fun getEnumCategory(category: Int): EnumCategory {
        val enumConstants = type.enumConstants.map { it as EnumCategory }

        for (const in enumConstants) {
            if (const.getCategory() == category) return const
        }

        throw TypeException("해당 타입은 변환이 불가능 합니다.")
    }
}
