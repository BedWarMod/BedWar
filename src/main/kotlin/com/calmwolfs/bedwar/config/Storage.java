package com.calmwolfs.bedwar.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Storage {
    @Expose
    public Map<UUID, PlayerSpecific> players = new HashMap<>();

    public static class PlayerSpecific {

    }
}