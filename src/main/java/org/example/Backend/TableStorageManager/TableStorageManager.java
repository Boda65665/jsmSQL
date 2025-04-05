//package org.example.Backend.TableStorageManager;
//
//import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterInterface;
//import org.example.Backend.Models.TabularData;
//import org.example.Backend.TableStorageManager.TableCreater.TableCrate;
//import org.example.Backend.TableStorageManager.TableDeleater.TableDeleater;
//import org.example.Backend.TableStorageManager.TableReader.TableReader;
//import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
//
//public class TableStorageManager {
//    private final TableCrate tableCrate;
//    private final TableDeleater tableDeleater;
//    private final TableReader tableReader;
//    private final TableWriter tableWriter;
//    private final BinaryConverterInterface binaryConverterInterface;
//
//    public TableStorageManager(TableCrate tableCrate, TableDeleater tableDeleater, TableReader tableReader, TableWriter tableWriter, BinaryConverterInterface binaryConverterInterface) {
//        this.tableCrate = tableCrate;
//        this.tableDeleater = tableDeleater;
//        this.tableReader = tableReader;
//        this.tableWriter = tableWriter;
//        this.binaryConverterInterface = binaryConverterInterface;
//    }
//
//    public void write(String tableName, TabularData tabularData) {
//        byte[] byteData = binaryConverterInterface.toByte(tabularData);
//        tableWriter.write(tableName, byteData);
//    }
//}
