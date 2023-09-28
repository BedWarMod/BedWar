package com.calmwolfs.bedwar.data.jsonobjects;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

public class PrestigesJson {
    @Expose
    public Map<Integer, PrestigeInfo> prestiges;

    public static class PrestigeInfo {
        @Expose
        public String prestigeName;

        @Expose
        public String starSymbol;

        @Expose
        public StarColours colours;
    }

    public static class StarColours {
        @Expose
        public String leftBracket;

        @Expose
        public String rightBracket;

        @Expose
        public String starColour;

        @Expose
        public List<String> numberColours;
    }
}