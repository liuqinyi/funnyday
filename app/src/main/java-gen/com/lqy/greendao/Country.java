package com.lqy.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table country.
 */
public class Country {

    private Long id;
    /** Not-null value. */
    private String country_name;
    private String country_code;
    private Integer city_id;

    public Country() {
    }

    public Country(Long id) {
        this.id = id;
    }

    public Country(Long id, String country_name, String country_code, Integer city_id) {
        this.id = id;
        this.country_name = country_name;
        this.country_code = country_code;
        this.city_id = city_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getCountry_name() {
        return country_name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

}
