package com.calmwolfs.bedwar.config.features;

import com.calmwolfs.bedwar.utils.computer.WebUtils;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.*;
import io.github.moulberry.moulconfig.observer.Property;

public class AboutConfig {

    @ConfigOption(name = "Auto Updates", desc = "Automatically check for updates on each startup")
    @Expose
    @ConfigEditorBoolean
    public boolean autoUpdates = true;

    @ConfigOption(name = "Update Stream", desc = "How frequently do you want updates for BedWar Mod")
    @Expose
    @ConfigEditorDropdown
    public Property<UpdateStream> updateStream = Property.of(UpdateStream.RELEASES);

    @ConfigOption(name = "Used Software", desc = "Information about used software and licenses")
    @Accordion
    @Expose
    public Licenses licenses = new Licenses();

    public enum UpdateStream {
        NONE("None", "none"),
        BETA("Beta", "pre"),
        RELEASES("Full", "full");

        private final String label;
        private final String stream;

        UpdateStream(String label, String stream) {
            this.label = label;
            this.stream = stream;
        }

        public String getStream() {
            return stream;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public static class Licenses {

        @ConfigOption(name = "MoulConfig", desc = "MoulConfig is available under the LGPL 3.0 License or later version")
        @ConfigEditorButton(buttonText = "Source")
        public Runnable moulConfig = () -> WebUtils.openBrowser("https://github.com/NotEnoughUpdates/MoulConfig");

        @ConfigOption(name = "NotEnoughUpdates", desc = "NotEnoughUpdates is available under the LGPL 3.0 License or later version")
        @ConfigEditorButton(buttonText = "Source")
        public Runnable notEnoughUpdates = () -> WebUtils.openBrowser("https://github.com/NotEnoughUpdates/NotEnoughUpdates");

        @ConfigOption(name = "SkyHanni", desc = "SkyHanni is available under the GNU Lesser General Public License v2.1")
        @ConfigEditorButton(buttonText = "Source")
        public Runnable skyhanni = () -> WebUtils.openBrowser("https://github.com/hannibal002/SkyHanni");

        @ConfigOption(name = "Forge", desc = "Forge is available under the LGPL 3.0 license")
        @ConfigEditorButton(buttonText = "Source")
        public Runnable forge = () -> WebUtils.openBrowser("https://github.com/MinecraftForge/MinecraftForge");

        @ConfigOption(name = "LibAutoUpdate", desc = "LibAutoUpdate is available under the BSD 2 Clause License")
        @ConfigEditorButton(buttonText = "Source")
        public Runnable libAutoUpdate = () -> WebUtils.openBrowser("https://git.nea.moe/nea/libautoupdate/");

        @ConfigOption(name = "Mixin", desc = "Mixin is available under the MIT License")
        @ConfigEditorButton(buttonText = "Source")
        public Runnable mixin = () -> WebUtils.openBrowser("https://github.com/SpongePowered/Mixin/");
    }
}