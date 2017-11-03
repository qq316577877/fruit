/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.mybatis;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2014-11-07
 * Project        : fruit
 * File Name      : FruitSqlSessionTemplate.java
 */
public class FruitSqlSessionTemplate extends SqlSessionTemplate implements SqlSession
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FruitSqlSessionTemplate.class);

    private static final String SQL_STATEMENT_NAME = "SQL";

    public FruitSqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
    {
        super(sqlSessionFactory);
    }

    public FruitSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType)
    {
        super(sqlSessionFactory, executorType);
    }

    public FruitSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator)
    {
        super(sqlSessionFactory, executorType, exceptionTranslator);
    }

    /**
     * {@inheritDoc}
     */
    public <T> T selectOne(final String statement)
    {
        return track(statement, new Operation<T>()
        {
            @Override
            public T doOperation()
            {
                return FruitSqlSessionTemplate.super.selectOne(statement);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <T> T selectOne(final String statement, final Object parameter)
    {
        return track(statement, new Operation<T>()
        {
            @Override
            public T doOperation()
            {
                return FruitSqlSessionTemplate.super.selectOne(statement, parameter);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <K, V> Map<K, V> selectMap(final String statement, final String mapKey)
    {
        return track(statement, new Operation<Map<K, V>>()
        {
            @Override
            public Map<K, V> doOperation()
            {
                return FruitSqlSessionTemplate.super.selectMap(statement, mapKey);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <K, V> Map<K, V> selectMap(final String statement, final Object parameter, final String mapKey)
    {
        return track(statement, new Operation<Map<K, V>>()
        {
            @Override
            public Map<K, V> doOperation()
            {
                return FruitSqlSessionTemplate.super.selectMap(statement, parameter, mapKey);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <K, V> Map<K, V> selectMap(final String statement, final Object parameter, final String mapKey, final RowBounds rowBounds)
    {
        return track(statement, new Operation<Map<K, V>>()
        {
            @Override
            public Map<K, V> doOperation()
            {
                return FruitSqlSessionTemplate.super.selectMap(statement, parameter, mapKey, rowBounds);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <E> List<E> selectList(final String statement)
    {
        return track(statement, new Operation<List<E>>()
        {
            @Override
            public List<E> doOperation()
            {
                return FruitSqlSessionTemplate.super.selectList(statement);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <E> List<E> selectList(final String statement, final Object parameter)
    {
        return track(statement, new Operation<List<E>>()
        {
            @Override
            public List<E> doOperation()
            {
                return FruitSqlSessionTemplate.super.selectList(statement, parameter);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public <E> List<E> selectList(final String statement, final Object parameter, final RowBounds rowBounds)
    {
        return track(statement, new Operation<List<E>>()
        {
            @Override
            public List<E> doOperation()
            {
                return FruitSqlSessionTemplate.super.selectList(statement, parameter, rowBounds);
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    public void select(final String statement, final ResultHandler handler)
    {
        voidTrack(statement, new VoidOperation()
        {
            @Override
            public void doOperation()
            {
                FruitSqlSessionTemplate.super.select(statement, handler);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void select(final String statement, final Object parameter, final ResultHandler handler)
    {
        voidTrack(statement, new VoidOperation()
        {
            @Override
            public void doOperation()
            {
                FruitSqlSessionTemplate.super.select(statement, parameter, handler);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void select(final String statement, final Object parameter, final RowBounds rowBounds, final ResultHandler handler)
    {
        voidTrack(statement, new VoidOperation()
        {
            @Override
            public void doOperation()
            {
                FruitSqlSessionTemplate.super.select(statement, parameter, rowBounds, handler);
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    public int insert(final String statement)
    {
        return track(statement, new Operation<Integer>()
        {
            @Override
            public Integer doOperation()
            {
                return FruitSqlSessionTemplate.super.insert(statement);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public int insert(final String statement, final Object parameter)
    {
        return track(statement, new Operation<Integer>()
        {
            @Override
            public Integer doOperation()
            {
                return FruitSqlSessionTemplate.super.insert(statement, parameter);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public int update(final String statement)
    {
        return track(statement, new Operation<Integer>()
        {
            @Override
            public Integer doOperation()
            {
                return FruitSqlSessionTemplate.super.update(statement);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public int update(final String statement, final Object parameter)
    {
        return track(statement, new Operation<Integer>()
        {
            @Override
            public Integer doOperation()
            {
                return FruitSqlSessionTemplate.super.update(statement, parameter);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public int delete(final String statement)
    {
        return track(statement, new Operation<Integer>()
        {
            @Override
            public Integer doOperation()
            {
                return FruitSqlSessionTemplate.super.delete(statement);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public int delete(final String statement, final Object parameter)
    {
        return track(statement, new Operation<Integer>()
        {
            @Override
            public Integer doOperation()
            {
                return FruitSqlSessionTemplate.super.delete(statement, parameter);
            }
        });
    }

    private static interface Operation<RESULT>
    {
        RESULT doOperation();
    }

    private static interface VoidOperation
    {
        void doOperation();
    }

    private static <T> T track(String name, Operation<T> operation)
    {
        Transaction transaction = Cat.newTransaction(SQL_STATEMENT_NAME, name);
        try
        {
            T t = operation.doOperation();
            transaction.setStatus(Transaction.SUCCESS);
            return t;
        }
        catch (RuntimeException e)
        {
            transaction.setStatus(e);
            LOGGER.error("failed to execute sql: " + name, e);
            Cat.logError(e);
            throw e;
        }
        finally
        {
            transaction.complete();
        }
    }

    private static void voidTrack(String name, VoidOperation operation)
    {
        Transaction transaction = Cat.newTransaction(SQL_STATEMENT_NAME, name);
        try
        {
            operation.doOperation();
            transaction.setStatus(Transaction.SUCCESS);
        }
        catch (RuntimeException e)
        {
            transaction.setStatus(e);
            LOGGER.error("failed to execute sql: " + name, e);
            Cat.logError(e);
            throw e;
        }
        finally
        {
            transaction.complete();
        }
    }
}