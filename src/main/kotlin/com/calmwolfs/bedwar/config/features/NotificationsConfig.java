package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigEditorSlider;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class NotificationsConfig {
    @Expose
    @ConfigOption(name = "Notification Corner", desc = "What corner do you want the notification to display in?")
    @ConfigEditorDropdown(values = {"Top Left", "Top Right"})
    public int corner = 0;

    @Expose
    @ConfigOption(name = "Animation Direction", desc = "What direction do you want it to appear from?")
    @ConfigEditorDropdown(values = {"Horizontal", "Vertical"})
    public int direction = 0;

    @ConfigOption(name = "Enable Animations", desc = "Animates the notifications appearing and disappearing")
    @Expose
    @ConfigEditorBoolean
    public boolean animations = true;

    @ConfigOption(name = "Show Items", desc = "Will attempt to show an item relevant to the notification")
    @Expose
    @ConfigEditorBoolean
    public boolean items = true;

    @Expose
    @ConfigOption(name = "Party Notifications", desc = "")
    @Accordion
    public NotificationsNoSound partyNotifications = new NotificationsNoSound();

    @Expose
    @ConfigOption(name = "Game Notifications", desc = "")
    @Accordion
    public NotificationsNoSound gameNotifications = new NotificationsNoSound();

    @Expose
    @ConfigOption(name = "Team Death Notifications", desc = "")
    @Accordion
    public NotificationsNoSound deathNotifications = new NotificationsNoSound();

    @Expose
    @ConfigOption(name = "Mod Errors", desc = "")
    @Accordion
    public NotificationsSound modErrors = new NotificationsSound();

    public static class NotificationsSound {
        @ConfigOption(name = "Enabled", desc = "Shows notifications for this category")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Sounds", desc = "Enables sounds for when this notification is triggered")
        @Expose
        @ConfigEditorBoolean
        public boolean sound = true;

        @Expose
        @ConfigOption(name = "Display Time", desc = "The amount of time in seconds that this notification shows for")
        @ConfigEditorSlider(minValue = 1.5f, maxValue = 10, minStep = .5f)
        public float displayLength = 5;
    }

    public static class NotificationsNoSound {
        @ConfigOption(name = "Enabled", desc = "Shows notifications for this category")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Sounds", desc = "Enables sounds for when this notification is triggered")
        @Expose
        @ConfigEditorBoolean
        public boolean sound = false;

        @Expose
        @ConfigOption(name = "Display Time", desc = "The amount of time in seconds that this notification shows for")
        @ConfigEditorSlider(minValue = 1.5f, maxValue = 10, minStep = .5f)
        public float displayLength = 5;
    }

    public static class NotificationsNoShow {
        @ConfigOption(name = "Enabled", desc = "Shows notifications for this category")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = false;

        @ConfigOption(name = "Sounds", desc = "Enables sounds for when this notification is triggered")
        @Expose
        @ConfigEditorBoolean
        public boolean sound = false;

        @Expose
        @ConfigOption(name = "Display Time", desc = "The amount of time in seconds that this notification shows for")
        @ConfigEditorSlider(minValue = 1.5f, maxValue = 10, minStep = .5f)
        public float displayLength = 5;
    }
}