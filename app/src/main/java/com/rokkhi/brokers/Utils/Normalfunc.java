package com.rokkhi.brokers.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.brokers.MainActivity;
import com.rokkhi.brokers.Model.LogSession;
import com.rokkhi.brokers.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class Normalfunc {

   // FirebaseFunctions firebaseFunctions;
    FirebaseFirestore firebaseFirestore;
    Context context;
    private static final String TAG = "Normalfunc";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public Normalfunc() {
        //firebaseFunctions = FirebaseFunctions.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public Normalfunc(Context context) {
        //firebaseFunctions = FirebaseFunctions.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();

    }

    public String makephone11(String phoneno){
        if(isvalidphone14(phoneno)){
            phoneno=phoneno.replace("+88","");
            return  phoneno;
        }
        if(isvalidphone11(phoneno))return phoneno;
        else return "error";
    }


    public void removeTokenId() {

//        if(signoutintent.getExtras().getBoolean("signout",false)) FirebaseAuth.getInstance().signOut();
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String token= sharedPref.getString("token","none");
        String userid = FirebaseAuth.getInstance().getUid();
        String logID = firebaseFirestore.collection(context.getString(R.string.col_logoutsession)).document().getId();
        LogSession logSession = new LogSession(logID, userid, token,"FieldWork", Calendar.getInstance().getTime());

        firebaseFirestore.collection(context.getString(R.string.col_logoutsession)).document(logID).set(logSession)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Have a good time!", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("TAG", TAG);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                        Log.d(TAG, "onComplete: tttt3");
                    }
                });


    }

    public String getchatID(String u1, String u2) {
        String xx = "";
        if (u1.compareTo(u2) > 0) {
            xx = u1.substring(0, 15) + u2.substring(0, 15);
        } else {
            xx = u2.substring(0, 15) + u1.substring(0, 15);
        }
        return xx;
    }

    public String getPassForGuards5(String phoneno) {
        String number = phoneno.substring(6);
        return number;
    }


    public List<String> splitstring(String ss) {

        String[] array = ss.trim().split(" +");

        List<String> xx = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (i > 0) xx.addAll(splitchar(array[i].toLowerCase()));
        }

        xx.addAll(splitchar(ss.toLowerCase()));

        return xx;
    }

    public List<String> splitchar(String ss) {

        List<String> xx = new ArrayList<>();
        String yy = "";

        if (ss.length() > 0 && ss.charAt(0) == '+') {
            xx.add(ss);
            ss = ss.substring(3);
        }

        for (int j = 0; j < ss.length(); j++) {
            yy = yy + ss.charAt(j);
            xx.add(yy.toLowerCase());
        }
        return xx;
    }


    public long getTimestampFromDate(Date date) {
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }


    public  boolean isValidEmail(String target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public String getRandomNumberString4() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);

    }

    public boolean isvalidphone(String phoneno) {


        if (phoneno.charAt(0) != '0') return false;

        if (phoneno.length() != 11) return false;
        for (int i = 1; i < 11; i++) {
            char xx = phoneno.charAt(i);
            if (xx < '0' || xx > '9') return false;
        }
        return true;
    }

    public String getvalidphone(String phoneno) {

        if (phoneno.charAt(0) == '8') {
            phoneno = phoneno.substring(2);
        }
        phoneno = phoneno.replace("-", "");
        phoneno = phoneno.replace("+88", "");
        phoneno = phoneno.replace(" ", "");
        return phoneno;
    }

    public String getTimeAgo(Date date1, Date date2) {

        long time = Math.abs(date1.getTime() - date2.getTime());

        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public static String convertDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    public static boolean isvalidphone14(String phoneno) {
        if (phoneno.isEmpty()) return false;

        if (phoneno.charAt(0) != '+') {
            return false;

        }

        if (phoneno.length() != 14) {
            return false;

        }
        if (phoneno.charAt(0) == '+') {
            phoneno = phoneno.substring(3);
        }
        if (phoneno.charAt(0) != '0') return false;
        phoneno = phoneno.replace("+88", "");

        Log.d(TAG, "isvalidphone: bb " + phoneno + " " + phoneno.length());
        if (phoneno.length() != 11) return false;
        for (int i = 0; i < 11; i++) {
            char xx = phoneno.charAt(i);
            if (xx < '0' || xx > '9') return false;
        }

        return true;
    }

    public String makephone14(String phoneno) {
        if (isvalidphone14(phoneno)) return phoneno;
        if (isvalidphone11(phoneno)) return "+88" + phoneno;
        else return "error";
    }

    public static boolean isvalidphone11(String phoneno) {
        if (phoneno.isEmpty()) return false;
        if (phoneno.charAt(0) != '0') return false;

        Log.d(TAG, "isvalidphone: bb " + phoneno + " " + phoneno.length());
        if (phoneno.length() != 11) return false;
        for (int i = 0; i < 11; i++) {
            char xx = phoneno.charAt(i);
            if (xx < '0' || xx > '9') return false;
        }

        return true;
    }


    public String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getDateMMMyyyy(Date c) {

        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getDateMMMddhhmm(Date c) {

        SimpleDateFormat df = new SimpleDateFormat("hh:mm  MMM d", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getDateMMMddhhmma(Date c){
        SimpleDateFormat df = new SimpleDateFormat("MMM d , hh:mm a ", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getDateMMMdd(Date c) {

        SimpleDateFormat df = new SimpleDateFormat("MMM d", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getDatehhmmdMMMMyyyy(Date c) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm   MMM d, yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    //MMMM d, yyyy
    public String getDateMMMMdyyyy(Date c) {

        SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }


    public Bitmap generateCircleBitmap(Context context, int circleColor, float diameterDP, String text) {
        final int textColor = 0xffffffff;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float diameterPixels = diameterDP * (metrics.densityDpi / 160f);
        float radiusPixels = diameterPixels / 2;

        // Create the bitmap
        Bitmap output = Bitmap.createBitmap((int) diameterPixels, (int) diameterPixels,
                Bitmap.Config.ARGB_8888);

        // Create the canvas to draw on
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);

        // Draw the circle
        final Paint paintC = new Paint();
        paintC.setAntiAlias(true);
        paintC.setColor(circleColor);
        canvas.drawCircle(radiusPixels, radiusPixels, radiusPixels, paintC);

        // Draw the text
        if (text != null && text.length() > 0) {
            final Paint paintT = new Paint();
            paintT.setColor(textColor);
            paintT.setAntiAlias(true);
            paintT.setTextSize(radiusPixels);
//            Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Thin.ttf");
//            paintT.setTypeface(typeFace);
            final Rect textBounds = new Rect();
            paintT.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, radiusPixels - textBounds.exactCenterX(), radiusPixels - textBounds.exactCenterY(), paintT);
        }

        return output;
    }


    private static List<Integer> materialColors = Arrays.asList(
            0xffe57373,
            0xfff06292,
            0xffba68c8,
            0xff9575cd,
            0xff7986cb,
            0xff64b5f6,
            0xff4fc3f7,
            0xff4dd0e1,
            0xff4db6ac,
            0xff81c784,
            0xffaed581,
            0xffff8a65,
            0xffd4e157,
            0xffffd54f,
            0xffffb74d,
            0xffa1887f,
            0xff90a4ae
    );

    public int getMaterialColor(int position) {
        // int random = new Random().nextInt(1000);
        return materialColors.get(position % materialColors.size());
    }


    public String getDateMMMdyyyy(Date c) {

        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public Date getMonthLastDate() {
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return cal.getTime();
    }


    public ArrayList<String> fetchMonthDataBackground(Date joiningDate) {
        ArrayList<String> retData = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 6; i++) {
            SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            String formattedDate = df.format(calendar.getTime());
            if (calendar.getTime().compareTo(joiningDate) < 1) break;
            retData.add(formattedDate);

            calendar.add(Calendar.MONTH, -1);
        }
        return retData;
    }

    //get Number Without Country Code
    public static String getNumberWithoutCountryCode(String st) {
        String number = "";
        if (st.charAt(0) == '+' && st.charAt(1) == '8' && st.charAt(2) == '8') {

            for (int i = 3; i < st.length(); i++) {
                number += st.charAt(i);
            }
        } else {
            number = st;
        }


        return number;
    }


}
