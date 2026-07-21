package com.deinname.mod.modules;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc");

    private String name;
    Category(String name) { this.name = name; }
    public String getDisplayName() { return name; }
}
