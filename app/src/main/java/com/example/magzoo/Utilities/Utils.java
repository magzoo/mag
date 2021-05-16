package com.example.magzoo.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.util.Base64;

public class Utils {

    public static Connection getConnection()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        Log.d("bajoraz","sql1");
        StrictMode.setThreadPolicy(policy);
        Log.d("bajoraz","sql2");
        Connection conn = null;
        String connectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Log.d("bajoraz","sql3");
            connectionURL = "jdbc:jtds:sqlserver://" + SqlConstants.SERVER + ";databaseName=" + SqlConstants.DATABASE + ";user=" + SqlConstants.USERNAME + ";password=" + SqlConstants.PASSWORD + ";";
            Log.d("bajoraz","ConnectionString = " + connectionURL);
            Log.d("bajoraz","sql4");
            conn = DriverManager.getConnection(connectionURL);
            Log.d("bajoraz","sql5");
        }
        catch (SQLException se)
        {
            Log.d("bajoraz", "SQLException: " + se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.d("bajoraz", "ClassNotFoundException: " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.d("bajoraz", "Exception: " + e.getMessage());
        }
        return conn;
    }

    public static void toast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static ResultSet getUserInfo(String email) {
        String msg = "";
        try {
            Connection connection = getConnection();
            if (connection == null) {
                msg = "Verifique a sua ligação à Internet!";
            } else {

                // Change below query according to your own database.
                String query = "exec dbo.spGetUserInfo '" + email + "'";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                return rs;

            }
        } catch (Exception ex) {
            msg = ex.getMessage();
        }

        return null;
    }

    public static String imgToBase64(Bitmap bmp)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToImg(String str)
    {
        byte[] byteArray = Base64.decode(str, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    public static float convertDpToPixel(Context context, float dp){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(Context context, float px){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int convertToDps(Context context, int value)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
