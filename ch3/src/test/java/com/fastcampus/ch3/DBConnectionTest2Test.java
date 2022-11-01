package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DBConnectionTest2Test {
    @Autowired
    DataSource ds;

    @Test
    public void insertUserTest() throws Exception{
        User user = new User("asdf15","1234","abc","aaa@aaa.com",new Date(),"fb",new Date());
        deleteAll();
        int rowCnt = insertUser(user);

        assertTrue(rowCnt==1);
    }

    @Test
    public void selectUserTest() throws Exception{
        User user = selectUser("asdf15");

        assertTrue(user.getId().equals("asdf15"));
    }

    public User selectUser(String id) throws Exception{
        Connection conn = ds.getConnection();
        String sql = "select * from user_info where id=?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,id);
        ResultSet rs =  pstmt.executeQuery();

        if(rs.next()){
            User user = new User();
            user.setId(rs.getString(1));
            user.setPwd(rs.getString(2));
            user.setName(rs.getString(3));

            return user;
        }
        return null;
    }

    private void deleteAll() throws Exception{
        Connection conn = ds.getConnection();
        String sql = "delete from user_info";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    @Test
    public void transactionTest() throws Exception{
        Connection conn = null;
        try {
            deleteAll();
            conn = ds.getConnection();
            conn.setAutoCommit(false); //default : true

            String sql = "insert into user_info values (?,?,?,?,?,?,now())";

            PreparedStatement pstmt = conn.prepareStatement(sql); //PreparedStatement를 사용하면 Sql Injection 공격에 안전, 성능향상
            pstmt.setString(1,"asdf");
            pstmt.setString(2,"1234");
            pstmt.setString(3,"abc");
            pstmt.setString(4,"aaa@aaa.com;");
            pstmt.setDate(5,new java.sql.Date(new Date().getTime())); //Util Date를 sql Date로 변경
            pstmt.setString(6,"fb");

            int rowCnt = pstmt.executeUpdate();

            pstmt.setString(1,"asdf2");
            rowCnt = pstmt.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
        }
    }

    public int insertUser(User user) throws Exception{
        Connection conn = ds.getConnection();
        String sql = "insert into user_info values (?,?,?,?,?,?,now())";

        PreparedStatement pstmt = conn.prepareStatement(sql); //PreparedStatement를 사용하면 Sql Injection 공격에 안전, 성능향상
        pstmt.setString(1,user.getId());
        pstmt.setString(2,user.getPwd());
        pstmt.setString(3,user.getName());
        pstmt.setString(4,user.getEmail());
        pstmt.setDate(5,new java.sql.Date(user.getBirth().getTime())); //Util Date를 sql Date로 변경
        pstmt.setString(6,user.getSns());

        int rowCnt = pstmt.executeUpdate();
        return rowCnt;
    }

    @Test
    public void main() throws Exception{
        //spring jdbc를 이용 - bean 사용
        //ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
        //DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // 데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);
        //테스트 성공여부 확인
        assertTrue(conn!=null); //괄호안의 조건식이 true명 성공, 아니면 실패
    }

}