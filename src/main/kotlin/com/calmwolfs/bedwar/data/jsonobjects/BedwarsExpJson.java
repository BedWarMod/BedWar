package com.calmwolfs.bedwar.data.jsonobjects;

import com.google.gson.annotations.Expose;

import java.util.List;


public class BedwarsExpJson {
    @Expose
    public Integer easyLevelCount;

    @Expose
    public List<Integer> easyLevelExp;

    @Expose
    public Integer easyLevelExpTotal;

    @Expose
    public Integer expPerLevel;

    @Expose
    public Integer expPerPrestige;

    @Expose
    public Integer levelsPerPrestige;
}