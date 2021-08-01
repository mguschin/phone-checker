package ru.mguschin.phonenumberchecker.service;

public enum ListTable {
    LIST1("TABLE1"),
    LIST2("TABLE2");

    private final String tableName;

    private ListTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }
}