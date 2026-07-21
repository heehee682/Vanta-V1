package com.deinname.mod.modules;

import com.deinname.mod.settings.Setting;
import java.util.ArrayList;
import java.util.List;

public class Module {
    private String name;
    private Category category;
    private boolean toggled;
    private List<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.toggled = false;
    }

    public void addSetting(Setting setting) { settings.add(setting); }
    public List<Setting> getSettings() { return settings; }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}

    public void toggle() {
        toggled = !toggled;
        if (toggled) onEnable();
        else onDisable();
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isToggled() { return toggled; }
}
