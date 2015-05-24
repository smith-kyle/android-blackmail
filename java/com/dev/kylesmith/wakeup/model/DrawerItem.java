package com.dev.kylesmith.wakeup.model;

import android.widget.ArrayAdapter;

/**
 * Created by kylesmith on 1/24/15.
 */
public class DrawerItem{
    private int _icon;
    private String _label;
    public DrawerItem(int icon, String label){
        this._icon = icon;
        this._label = label;
    }

    public int get_icon() {
        return _icon;
    }

    public String get_label(){
        return _label;
    }
}
