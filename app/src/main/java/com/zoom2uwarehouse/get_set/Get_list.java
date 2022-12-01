package com.zoom2uwarehouse.get_set;

/**@author avadhesh mourya
 * Created by ubuntu on 18/1/18.
 */

public class Get_list {

    private String name;
    private String value;

    public Get_list(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
