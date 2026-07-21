package com.deinname.mod.settings;

public class SliderSetting extends Setting {
    private double value, min, max;
    public SliderSetting(String name, double defaultVal, double min, double max) {
        super(name);
        this.value = defaultVal;
        this.min = min;
        this.max = max;
    }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public double getMin() { return min; }
    public double getMax() { return max; }
}
