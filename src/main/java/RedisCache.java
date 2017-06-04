import org.apache.commons.io.IOUtils;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.sql.*;
import java.util.Base64;


/**
 * Created by lokisneha on 5/12/17.
 */
public class RedisCache {
    public static void main(String[] args) {

        //checkDatabaseConnection();
        //insertImageDB();
        //getImageFromDB();
        //checkRedisConnection();
        String st = encodeFileToBase64Binary(new File("test0.jpg"));
        decodeImage(st);
    }

    static void checkRedisConnection(){
        //Connecting to Redis server on localhost
        BinaryJedis binaryJedis = new BinaryJedis("localhost");
        System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        System.out.println("Server is running: "+binaryJedis.ping());

    }

    private static void decodeImage(String base64ImageSring){
        byte[] data = Base64.getDecoder().decode(base64ImageSring);
        try (OutputStream stream = new FileOutputStream("abc.png")) {
            stream.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }

    static void checkDatabaseConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/redis_cache","root","lokipoki123");

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT CAST(image AS CHAR(10000) CHARACTER SET utf8) as image, id FROM ImageStore;");
            /*while(rs.next()) {
                System.out.println(rs.getString(1) +"  "+rs.getString(2));
            }*/

                Blob imageBlob = rs.getBlob(2);

            InputStream binaryStream = imageBlob.getBinaryStream(0, imageBlob.length());
            File imageFromDbFile = new File("/Users/lokisneha/Downloads/from_db_image.png");

            OutputStream outputStream = new FileOutputStream(imageFromDbFile);
            IOUtils.copy(binaryStream, outputStream);
            outputStream.close();
            con.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    static void insertImageDB(){
        System.out.println("Insert Image Example!");
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "redis_cache";
        String userName = "root";
        String password = "lokipoki123";
        Connection con = null;
        try{
            Class.forName(driverName);
            con = DriverManager.getConnection(url+dbName,userName,password);
            Statement st = con.createStatement();
            File imgfile = new File("/Users/lokisneha/Downloads/image_sjsu.png");

            FileInputStream fin = new FileInputStream(imgfile);

            PreparedStatement pre =
                    con.prepareStatement("insert into Image values(?,?,?)");

            pre.setString(1,"test");
            pre.setInt(2,3);
            pre.setBinaryStream(3,(InputStream)fin,(int)imgfile.length());
            pre.executeUpdate();
            System.out.println("Successfully inserted the file into the database!");

            pre.close();
            con.close();
        }catch (Exception e1){
            System.out.println(e1.getMessage());
        }
    }

    static void getImageFromDB(){
        System.out.println("Retrive Image Example!");
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "redis_cache";
        String userName = "root";
        String password = "lokipoki123";
        Connection con = null;
        try{
            Class.forName(driverName);
            con = DriverManager.getConnection(url+dbName,userName,password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select image from image");
            int i = 0;
            while (rs.next()) {
                InputStream in = rs.getBinaryStream(1);
                OutputStream f = new FileOutputStream(new File("test"+i+".jpg"));
                i++;
                int c = 0;
                while ((c = in.read()) > -1) {
                    f.write(c);
                }
                f.close();
                in.close();
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

}

