package blobExtract;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

public class Main
{
    private static Connection connection;

    //db2
//    private static String username = "db2inst5";
//    private static String password = "db2Admin";
//    private static String host = "localhost";
//    private static String port = "51000";
//    private static String dbName = "lunosdem";

    //postgres
    private static String username = "postgres";
    private static String password = "password";
    private static String host = "localhost";
    private static String port = "5432";
    private static String dbName = "lunosdem";

    private static String workingDir = "output";
    private static Mode mode;

    private static boolean readArguments(String[] args)
    {
        String tag = null;
        for(int i = 0; i < args.length; i++)
        {
            if(tag == null)
            {
                tag = args[i];
                if(!tag.startsWith("-"))
                {
                    System.out.println("Error: unknown argument '" + tag + "'");
                    return false;
                }
                switch(tag)
                {
                    case "-help":
                    case "-h":
                        printUsage();
                        return false;
                    case "-extract":
                        mode = Mode.extract;
                        tag = null;
                        break;
                    case "-insert":
                        mode = Mode.insert;
                        tag = null;
                        break;
                }

            }
            else
            {
                switch(tag)
                {
                    case "-u":
                        username = args[i];
                        break;
                    case "-P":
                        password = args[i];
                        break;
                    case "-H":
                        host = args[i];
                        break;
                    case "-d":
                        dbName = args[i];
                        break;
                    case "-p":
                        port = args[i];
                        break;
                    case "-w":
                        workingDir = args[i];
                        break;
                    default:
                        System.out.println("Error: unknown argument '" + tag + "'");
                        return false;
                }
                tag = null;
            }

        }
        if(tag != null)
        {
            System.out.println("Error: tag " + tag + " missing argument");
            return false;
        }
        return true;
    }

    public static void main(String[] args)throws Exception
    {
        if(!readArguments(args))
            return;

        loadConns();
        if(mode == null)
        {
            System.out.println("Error: no mode specified.");
            printUsage();
            return;
        }
        switch(mode)
        {
            case extract:
                runExtract();
                break;
            case insert:
                runInsertOid();
                break;
        }
        System.out.println("Done.");
    }

    private static void runExtract() throws Exception
    {
        new File(workingDir).mkdirs();//ensure output directory exists

        //update table set DOCUMENT_BINARY_CTNT = [PDF data] WHERE ID = [PDF name]
        ResultSet results = connection.createStatement().executeQuery("SELECT ID, DOCUMENT_BINARY_CTNT FROM iaacommu.TBL_DOCUMENT");
        while(results.next())
        {
            int id = results.getInt(1);
            System.out.println("transferring " + id + "...");

            File outputFile = new File(workingDir + File.separator + id + ".pdf");
            OutputStream fileOut = new FileOutputStream(outputFile);
            transfer(results.getBlob(2).getBinaryStream(), fileOut);
        }
        results.close();
    }

    private static void runInsertImmediate()throws Exception
    {
        File fileList[] = new File(workingDir).listFiles();
        if(fileList == null)
            throw new IOException("Not a directory");
        for(File file:fileList)
        {
            int id = Integer.parseInt(file.getName().split("\\.")[0]);
            System.out.println("transferring " + id + "...");

            InputStream fileReader = new FileInputStream(file);
            StringBuilder sql = new StringBuilder("UPDATE iaacommu.TBL_DOCUMENT SET DOCUMENT_BINARY_CTNT = '\\x");
            transferHexString(fileReader, sql);
            sql.append("' WHERE ID = ").append(id);
            connection.createStatement().execute(sql.toString());
        }
    }

    private static void runInsertOid()throws Exception
    {
        //internal table system: https://www.postgresql.org/docs/9.0/static/catalog-pg-largeobject.html
        //code adapted from:     https://jdbc.postgresql.org/documentation/80/binary-data.html

        // All LargeObject API calls must be within a transaction block
        connection.setAutoCommit(false);

        File fileList[] = new File(workingDir).listFiles();
        if(fileList == null)
            throw new IOException("Not a directory");
        for(File file : fileList)
        {
            int fileID = Integer.parseInt(file.getName().split("\\.")[0]);
            System.out.println("transferring " + fileID + "...");

            long oid = transferOid(new FileInputStream(file));
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE iaacommu.TBL_DOCUMENT SET DOCUMENT_BINARY_CTNT = ? WHERE ID = ?");
            ps.setLong(1, oid);
            ps.setLong(2, fileID);
            ps.executeUpdate();
            ps.close();
            //commit this file
            connection.commit();
        }
        connection.setAutoCommit(true);
    }

    private static void loadConns()
    {
        try
        {
            if(mode == Mode.extract)
            {
                Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
                connection = DriverManager.getConnection("jdbc:db2://" + host + ":" + port + "/" + dbName + ":user=" + username + ";password=" + password + ";");
            }
            else if(mode == Mode.insert)
            {
                Class.forName("org.postgresql.Driver").newInstance();
                //connection = DriverManager.getConnection("jdbc:postgresql://localhost/test?user=iaauser&password=password&ssl=true");
                connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password + "&ssl=false");
            }
        } catch(Exception ex)
        {
            System.err.println("could not load driver(s): " + ex);
        }

    }

    private static long transferOid(InputStream in)throws Exception
    {
        LargeObjectManager lobj = ((org.postgresql.PGConnection)connection).getLargeObjectAPI();
        long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

        LargeObject out = lobj.open(oid, LargeObjectManager.WRITE);
        byte buffer[] = new byte[2048];
        int bytesRead;
        while((bytesRead = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();

        return oid;
    }

    private static void transferHexString(InputStream in, StringBuilder out)throws IOException
    {
        BufferedInputStream bin = new BufferedInputStream(in);
        while(true)
        {
            int nextVal = bin.read();
            if(nextVal == -1)
            {
                bin.close();
                return;//end of file
            }
            out.append(Character.forDigit((nextVal >> 4) & 0xF, 16));//upper
            out.append(Character.forDigit(nextVal & 0xF, 16));//lower
        }
    }

    private static void transfer(InputStream in, OutputStream out)throws IOException
    {
        byte[] buffer = new byte[2048];
        int bytesRead;
        while((bytesRead = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.flush();
        out.close();
    }

    private static void printUsage()
    {
        System.out.println("Usage:\n" +
                                   "-extract: extract PDFs from DB2\n" +
                                   "-insert: insert PDFs into Postgres\n" +
                                   "-u: username (default 'db2admin')\n" +
                                   "-P: password (default 'password')\n" +
                                   "-H: IP of database (default 'localhost')\n" +
                                   "-p: port of database (default '50000')\n" +
                                   "-d: name of database (default 'IAALUNOS')\n" +
                                   "-w: working directory (default 'output')\n" +
                                   "-help or -h: displays this message");
    }

    private enum Mode
    {
        extract,
        insert
    }
}
