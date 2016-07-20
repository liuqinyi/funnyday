package com.lqy.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.lqy.greendao.Province;
import com.lqy.greendao.City;
import com.lqy.greendao.County;

import com.lqy.greendao.ProvinceDao;
import com.lqy.greendao.CityDao;
import com.lqy.greendao.CountyDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig provinceDaoConfig;
    private final DaoConfig cityDaoConfig;
    private final DaoConfig countyDaoConfig;

    private final ProvinceDao provinceDao;
    private final CityDao cityDao;
    private final CountyDao countyDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        provinceDaoConfig = daoConfigMap.get(ProvinceDao.class).clone();
        provinceDaoConfig.initIdentityScope(type);

        cityDaoConfig = daoConfigMap.get(CityDao.class).clone();
        cityDaoConfig.initIdentityScope(type);

        countyDaoConfig = daoConfigMap.get(CountyDao.class).clone();
        countyDaoConfig.initIdentityScope(type);

        provinceDao = new ProvinceDao(provinceDaoConfig, this);
        cityDao = new CityDao(cityDaoConfig, this);
        countyDao = new CountyDao(countyDaoConfig, this);

        registerDao(Province.class, provinceDao);
        registerDao(City.class, cityDao);
        registerDao(County.class, countyDao);
    }
    
    public void clear() {
        provinceDaoConfig.getIdentityScope().clear();
        cityDaoConfig.getIdentityScope().clear();
        countyDaoConfig.getIdentityScope().clear();
    }

    public ProvinceDao getProvinceDao() {
        return provinceDao;
    }

    public CityDao getCityDao() {
        return cityDao;
    }

    public CountyDao getCountyDao() {
        return countyDao;
    }

}