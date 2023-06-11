package ksh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// 사용자 정보 DAO
public class UserInfoDAO {
    private static final String URL = "jdbc:mysql://localhost/studycafe";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public User getUserInfo(String userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL 문장 작성
            String sql = "SELECT * FROM users WHERE uId = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, userId);

            // SQL 문장 실행
            resultSet = statement.executeQuery();

            // 결과 처리
            if (resultSet.next()) {
                String name = resultSet.getString("uName");
                String password = resultSet.getString("uPw");
                String phone = resultSet.getString("uPhone");

                return new User(name, userId, password, phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null; // 회원정보가 없는 경우 null 반환
    }
}

