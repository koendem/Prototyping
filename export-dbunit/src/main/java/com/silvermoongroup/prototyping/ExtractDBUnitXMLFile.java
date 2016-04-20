package com.silvermoongroup.prototyping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.XmlDataSet;

/**
 * This application will export a dbunit xml file for the tables in the testTableNames array.
 *
 * Note: The dbunit DriverManager class loads a suitable driver for the database based on what it finds in the
 *       jdbcConnectionURL.
 *
 * Further, this class can't deal with tables that have the same name. We can remove the tables with duplicate names
 * from the database by using the following script:
 *
 *   select 'DROP TABLE ' || trim(tabschema) ||'.'|| tabname ||';' from syscat.tables
 *   where tabname in
 *       (select tabname from syscat.tables where tabschema like 'IAA%' group by tabname having count(*) > 1);
 *
 */
public class ExtractDBUnitXMLFile
{

    private static String dbDriver        = "com.ibm.db2.jcc.DB2Driver";
    private static String jdbcConnectionURL = "jdbc:db2://localhost:50000/LUNOSDEM:currentSchema=IAARATE\\;";
    private static String[] testTableNames = {"TBL_TRAVEL_INSURANCE_PREMSEL", "TBL_TRAVEL_INSURANCE_RTNTBL",
            "TBL_ACTUARIAL_STAT"};


    public static void main( String[] args )
            throws DatabaseUnitException, SQLException, ClassNotFoundException, IOException {

        IDatabaseConnection connection = getConnection();

        ITableFilter filter = new DatabaseSequenceFilter(connection, testTableNames);
        IDataSet dataset    = new FilteredDataSet(filter, connection.createDataSet());
        XmlDataSet.write(dataset, new FileOutputStream(new File("test.xml")));
    }


    public static IDatabaseConnection getConnection() throws ClassNotFoundException, DatabaseUnitException,
            SQLException {

        //  unnecessary : Class driverClass = Class.forName(dbDriver);
        Connection jdbcConnection = DriverManager.getConnection(jdbcConnectionURL, "db2inst1", "db2Admin1");
        jdbcConnection.setSchema("IAARATE");
        return new DatabaseConnection(jdbcConnection);
    }
}
