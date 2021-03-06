package es.pryades.imedig.android.utils;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.security.MessageDigest;

/**
 * Created by dismer on 10/03/17.
 */

public class Utils
{
    private final static String TAG = Utils.class.getSimpleName();

    private static final int[] crc16_table = {
            0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
            0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
            0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
            0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
            0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
            0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
            0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
            0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
            0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
            0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
            0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
            0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
            0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
            0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
            0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
            0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
            0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
            0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
            0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
            0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
            0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
            0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
            0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
            0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
            0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
            0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
            0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
            0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
            0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
            0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
            0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
            0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,
    };

    public static String convertToHex( byte value )
    {
        String table = "0123456789ABCDEF";

        byte lo = (byte)(value & 0x0F);
        byte hi = (byte)((value & 0xF0) >> 4);

        return "" + table.charAt( hi ) + table.charAt( lo );
    }

    public static String convertToHex( byte[] data )
    {
        StringBuffer buf = new StringBuffer();

        for ( int i = 0; i < data.length; i++ )
            buf.append( convertToHex( data[i] ) );

        return buf.toString();
    }

    public static byte convertFromHex( byte hi, byte lo )
    {
        String table = "0123456789ABCDEF";

        return (byte)(table.indexOf( lo ) | (table.indexOf( hi ) << 4));
    }

    public static byte[] convertFromHex( byte[] data )
    {
        byte buffer[] = new byte[data.length / 2];

        for ( int i = 0; i < data.length; i += 2 )
            buffer[i / 2] = convertFromHex( data[i], data[i + 1] );

        return buffer;
    }

    public static String loadTextFileFromAssets(AssetManager assets, String fileName )
    {
        BufferedReader reader = null;
        String ret = "";
        try {
            reader = new BufferedReader(new InputStreamReader( assets.open( fileName ), "UTF-8" ) );

            String line;
            while ((line = reader.readLine()) != null)
                ret += line;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    public static List<String> splitStringInParts( String str, int size )
    {
        List<String> parts = new ArrayList<String>();

        int pos = 0;
        while ( pos < str.length() )
        {
            if ( pos + size > str.length() )
                parts.add( str.substring( pos ) );
            else
                parts.add( str.substring( pos, pos + size ) );
            pos += size;
        }

        return parts;
    }

    public static int getCRC16( String buffer ) {
        byte[] bytes = buffer.getBytes();
        int crc = 0x0000;

        for (byte b : bytes)
            crc = (crc >>> 8) ^ crc16_table[(crc ^ b) & 0xff];

        return crc;
    }

    public static byte lobyte( int value )
    {
        return (byte)(value & 0x00FF);
    }

    public static byte hibyte( int value )
    {
        return (byte)((value & 0xFF00) >> 8);
    }

    public static byte lobyte( short value )
    {
        return (byte)(value & 0x00FF);
    }

    public static byte hibyte( short value )
    {
        return (byte)((value & 0xFF00) >> 8);
    }

    public static int loshort( int value )
    {
        return (short)(value & 0x0000FFFF);
    }

    public static int hishort( int value )
    {
        return (short)((value & 0xFFFF0000) >> 16);
    }

    public static int loint( long value )
    {
        return (int)(value & 0x00000000FFFFFFFFL);
    }

    public static int hiint( long value )
    {
        return (int)((value & 0xFFFFFFFF00000000L) >> 32);
    }

    public static short getShort( byte hi, byte lo )
    {
        return (short)(((hi & 0xff) << 8) | (lo & 0xff));
    }

    public static short getShort( short hi, short lo )
    {
        return (short) (((hi & 0xff) << 8) | (lo & 0xff));
    }

    public static int getInt( int hi, int lo )
    {
        return ((hi & 0xffff) << 16) | (lo & 0xffff);
    }

    public static int getInt( short hi, short lo )
    {
        return ((hi & 0xffff) << 16) | (lo & 0xffff);
    }

    public static String platformCall(String serviceUrl, String method, int timeout, String body, String[] headers, String tokenPrefix) {
        long ts = new Date().getTime();
        String extra = "ts=" + ts;
        String token = "token=" + Utils.getTokenString( tokenPrefix+ts, Constants.TrustKey );
        String code = "code=" + Utils.encrypt( extra, Constants.TrustKey ) ;
        String tokenUrl = serviceUrl + "?" + token + "&" + code;

        //Log.i(TAG, "platformCall " + tokenUrl + "\nmethod " + method + "\nbody " + body );

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(tokenUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            /*SSLContext sc;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());

            urlConnection.setSSLSocketFactory(sc.getSocketFactory());*/

            urlConnection.setRequestMethod( method );
            urlConnection.setReadTimeout(timeout);

            for ( String header : headers ) {
                String parts[] = header.split( ":" );
                if ( parts.length == 2 )
                    urlConnection.setRequestProperty( parts[0], parts[1] );
            }

            if ( body != null ) {
                OutputStream output = urlConnection.getOutputStream();
                output.write( body.getBytes("UTF8") );
            }

            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                out.append(line);

            return out.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static ByteArrayOutputStream platformCallBinary(String serviceUrl, String method, int timeout, String body, String[] headers, String tokenPrefix) {
        long ts = new Date().getTime();
        String extra = "ts=" + ts;
        String token = "token=" + Utils.getTokenString( tokenPrefix+ts, Constants.TrustKey );
        String code = "code=" + Utils.encrypt( extra, Constants.TrustKey ) ;
        String tokenUrl = serviceUrl + "?" + token + "&" + code;

        //Log.i(TAG, "platformCall " + tokenUrl + "\nmethod " + method + "\nbody " + body );

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(tokenUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            /*SSLContext sc;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());

            urlConnection.setSSLSocketFactory(sc.getSocketFactory());*/

            urlConnection.setRequestMethod( method );
            urlConnection.setReadTimeout(timeout);

            for ( String header : headers ) {
                String parts[] = header.split( ":" );
                if ( parts.length == 2 )
                    urlConnection.setRequestProperty( parts[0], parts[1] );
            }

            if ( body != null ) {
                OutputStream output = urlConnection.getOutputStream();
                output.write( body.getBytes("UTF8") );
            }

            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            copyStream(in, os);

            return os;
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    public static Usuario login(String hospitalUrl, String codigo, String password, int timeout) {
        try {
            Usuario user = new Usuario();
            user.setLogin(codigo);
            user.setPwd(password);

            String[] headers = {"Content-Type:application/json", "Accept:application/json"};

            String response = Utils.platformCall(hospitalUrl + "/imedig-services/login", "POST", timeout, JsonSerializer.toJson(user), headers, "");

            //Log.i(TAG, "login " + response);

            return (Usuario) JsonSerializer.toPojo(response, Usuario.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DetalleInforme> getInformes(String hospitalUrl, InformeQuery query, int timeout) {
        try {
            String[] headers = {"Content-Type:application/json", "Accept:application/json"};
            String response = Utils.platformCall(hospitalUrl + "/imedig-services/reports", "POST", timeout, JsonSerializer.toJson(query), headers, "");
            //Log.i(TAG, "getInformes " + response);

            return (List<DetalleInforme>) JsonSerializer.toArrayList(response, new TypeToken<ArrayList<DetalleInforme>>() {
            }.getType());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ByteArrayOutputStream getInforme(String hospitalUrl, int id, int timeout) {
        try {
            String[] headers = {"Content-Type:application/json", "Accept:application/json"};
            ByteArrayOutputStream os = Utils.platformCallBinary(hospitalUrl + "/imedig-services/report/"+id, "GET", timeout, null, headers, Integer.toString(id));

            //Log.i(TAG, "getInforme " + response);

            return os;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getEmptyIfNull(String value) {
        return value == null ? "" : value;
    }

    public static String getDateFromLongAsString( long date, String format )
    {
        try
        {
            Date input = new SimpleDateFormat( "yyyyMMddHHmmss" ).parse( Long.toString( date ) );

            return new SimpleDateFormat( format ).format( input );
        }
        catch ( Throwable e )
        {
            return "";
        }
    }

    public static String SHA1( String text ) throws Exception
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance( "SHA1" );

            byte[] md5hash = new byte[32];

            md.update( text.getBytes( "iso-8859-1" ), 0, text.length() );

            md5hash = md.digest();

            return convertToHex( md5hash );
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getTrustedToken( String data, String pwd ) throws Exception
    {
        return Utils.SHA1( pwd + data );
    }

    public static boolean isTrustedToken( String token, String data, String pwd ) throws Exception
    {
        return token.equalsIgnoreCase( getTrustedToken( data, pwd ) );
    }

    public static String getTokenString( String data, String key )
    {
        try
        {
            return Utils.getTrustedToken( data, key );
        }
        catch ( Throwable e )
        {
        }

        return "";
    }

    public static String encrypt( String data, String pwd )
    {
        try
        {
            return convertToHex( Cryptor.encrypt( data.getBytes(), pwd ) );
        }
        catch ( Throwable e )
        {
            return data;
        }
    }

    public static String decrypt( String data, String pwd )
    {
        try
        {
            return new String( Cryptor.decrypt( convertFromHex( data.getBytes() ), pwd ) );
        }
        catch ( Throwable e )
        {
            return data;
        }
    }
}
