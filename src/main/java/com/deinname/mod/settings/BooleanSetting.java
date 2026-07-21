package com.deinname.mod.settings;

public class BooleanSetting extends Setting {
    private boolean enabled;
    public BooleanSetting(String name, boolean defaultVal) {
        super(name);
        this.enabled = defaultVal;
    }
    public boolean isEnabled() { return enabled; }
    public void toggle() { enabled = !enabled; }
}
