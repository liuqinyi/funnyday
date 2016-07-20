package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {

        //创建一个用于添加实体的Schema对象，第一个参数表示数据库的版本，第二个参数表示在java-gen目录下自动生成的实体类和DAO类存放的包名
        Schema schema = new Schema(1, "com.lqy.greendao");
        //schema.setDefaultJavaPackageDao("com.lqy.greendao");
        // 假如你不想实体类和DAO类都放在一个包中，你可以重新为DAO类设置一个新的包

        //创建一个实体，一个实体对应一张表，此处表示生成的实体名为Student，同样它默认也是表名
        Entity entity = schema.addEntity("Province");
        //entity.setTableName("Students");
        //你如果不想实体名和表名相同，也可以重新设置一个表名
        //为Student表添加字段,这里的参数表示实体类Student的字段名，生成的表中的字段会变成大写，如name在表中字段为NAME
        entity.addIdProperty().autoincrement().primaryKey();
        entity.addStringProperty("province_name").notNull();
        entity.addStringProperty("province_code");


        //添加City实体类
        Entity entity_city = schema.addEntity("City");
        entity_city.addIdProperty().autoincrement().primaryKey();
        entity_city.addStringProperty("city_name").notNull();
        entity_city.addStringProperty("city_code");
        entity_city.addIntProperty("province_id");

        Entity entity_county = schema.addEntity("County");
        entity_county.addIdProperty().autoincrement().primaryKey();
        entity_county.addStringProperty("county_name").notNull();
        entity_county.addStringProperty("county_code");
        entity_county.addIntProperty("city_id");



        //最后通过DaoGenerator对象的generateAll()方法来生成相应的实体类和DAO类，参数分别为Schema对象和java-gen目录路径
        new DaoGenerator().generateAll(schema, "../FunnyDay/app/src/main/java-gen");
    }
}
