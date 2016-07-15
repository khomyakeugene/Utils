package com.company.data.model.proto;

/**
 * Created by Yevhen on 13.07.2016.
 */
public class DataItem implements Comparable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataItem)) return false;

        DataItem dataItem = (DataItem) o;

        return id == dataItem.id && (name != null ? name.equals(dataItem.name) : dataItem.name == null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        DataItem dataItem = (DataItem) o;

        return id - dataItem.id;
    }
}
