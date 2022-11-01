package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@Service
public class TxService {
    @Autowired A1Dao a1Dao;
    @Autowired B1Dao b1Dao;
    @Autowired
    DataSource ds;

    public void insertA1WithoutTx() throws Exception{
        a1Dao.insert(1, 100);
        a1Dao.insert(2, 200);
    }

    public void insertA1WithTxManager() throws Exception{
        PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
        TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());//Tx의 속성을 정의

        try {
            a1Dao.insert(1, 100);
            a1Dao.insert(2, 200);
            tm.commit(status);  //Commit
        } catch (Exception e) {
            tm.rollback(status);  //Rollback
        }
    }

    //@Transactional  //'@Transactional'만 적으면 RuntimeException, Error만 rollback
    @Transactional(rollbackFor = Exception.class)   //Exception을 rollback
    public void insertA1WithTxFail() throws Exception{
        a1Dao.insert(1, 100);
        a1Dao.insert(1, 200);
    }

    @Transactional
    public void insertA1WithTxSuccess() throws Exception{
        a1Dao.insert(1, 100);
        a1Dao.insert(2, 200);
    }
}
