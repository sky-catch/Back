package com.example.api.restaurant.dto.enums;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Category.class)
public class CategoryTypeHandler implements TypeHandler<Category> {

    @Override
    // 지정된 타입의 어떤 값을 DB에 저장할 것인가?
    public void setParameter(PreparedStatement ps, int i, Category parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getKoreanName());
    }

    @Override
    public Category getResult(ResultSet resultSet, String s) throws SQLException {
        String roleKey = resultSet.getString(s);
        return getCategory(roleKey);
    }

    @Override
    public Category getResult(ResultSet resultSet, int i) throws SQLException {
        String roleKey = resultSet.getString(i);
        return getCategory(roleKey);
    }

    @Override
    public Category getResult(CallableStatement callableStatement, int i) throws SQLException {
        String roleKey = callableStatement.getString(i);
        return getCategory(roleKey);
    }
    
    private Category getCategory(String str){
        Category category = null;
        switch (str){
            case "한식":
                category = Category.KOREAN;
                break;
            default:
                category = Category.ASIAN;
                break;
        }
        return category;
    }

}