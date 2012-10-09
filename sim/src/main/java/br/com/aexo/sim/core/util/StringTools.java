package br.com.aexo.sim.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * 
 * @author Cody Lerum
 * 
 */
@RequestScoped
@Named
public class StringTools {

    public static String getMD5Hex(String value) {
        return DigestUtils.md5Hex(value.toLowerCase().trim());
    }

    /**
     * Formats the given date as a string using the specified pattern
     * 
     * @param date
     * @param pattern
     * @param timeZone
     * @return Date formatted as a String
     */
    public static String getFormattedDate(Date date, String pattern, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    /**
     * Formats the given date as a string using the specified pattern
     * 
     * @param date
     * @param pattern
     * @return Date formatted as a String
     * @throws {@link IllegalArgumentException} if the pattern is invalid
     */
    public static String getFormattedDate(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * Formats the given calendar as a string using the specified pattern
     * 
     * @param calendar
     * @param pattern
     * @return Calendar formatted as a String
     * @throws {@link IllegalArgumentException} if the pattern is invalid
     */
    public static String getFormattedCalendar(GregorianCalendar gregorianCalendar, String pattern) {
        return getFormattedDate(gregorianCalendar.getTime(), pattern);
    }

    public static String getDateFormatMedium(Date date) {
        return getFormattedDate(date, "MM/dd/yyyy HH:mm z");
    }

    public static String getDateFormatMedium(Date date, TimeZone timeZone) {
        return getFormattedDate(date, "MM/dd/yyyy HH:mm z", timeZone);
    }

    /**
     * Checks is a String is null or length is ZERO with whitespace trimmed
     * 
     * @param value String to be checked
     * @return true is empty or null
     */
    public static boolean isNullOrBlank(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static String randomPassword(int length, boolean letters, boolean numbers, boolean mixedCase) {
        String password = RandomStringUtils.random(8, letters, numbers);

        if (mixedCase) {
            return password;
        } else {
            return password.toLowerCase();
        }
    }

    /**
     * Takes a String and removes whitespace. Returns null if empty.
     * 
     * @param value
     * @return
     */
    public static String normalize(String value) {
        if (StringTools.isNullOrBlank(value)) {
            return null;
        } else {
            return value.trim();
        }
    }

    /**
     * Check if a String has more than one line
     * @param str
     * @return
     */
    public static boolean containsMultipleLines(String str) {
        if (!str.trim().isEmpty()) {
            Scanner s = new Scanner(str);
            s.nextLine();
            return s.hasNextLine();
        } else {
            return false;
        }
    }
    
    /**
     * Return the first line of a String
     * @param str
     * @return
     */
    public static String firstLine(String str)
    {
       Scanner s = new Scanner(str);

       if (s.hasNext())
       {
          return s.nextLine();
       }
       else
       {
          return str;
       }
    }

    /**
     * Return all the lines after the first of a String as a list. 
     * @param str
     * @return
     */
    public static List<String> additionalLines(String str)
    {
       Scanner s = new Scanner(str);
       List<String> result = new ArrayList<String>();

       s.nextLine();

       while (s.hasNextLine())
       {
          result.add(s.nextLine());
       }

       return result;
    }
    
    public static List<String> stringFromCSV(String value)
    {
       List<String> result = new ArrayList<String>();

       for (String s : Splitter.on(',').trimResults().omitEmptyStrings().split(value))
       {
          result.add(s);
       }

       return result;
    }
    
    public static String csvFromStrings(Collection<String> strings)
    {
       return Joiner.on(',').skipNulls().join(strings);
    }
    
    public static String maskTaxId(String taxId)
    {
        if (taxId.length() == 9)
        {
            return "XXX-XX-" + taxId.substring(5);
        }
        return taxId;
    }
    
    public static String stringTrim(String s, int length, String tail)
    {
       if (s.length() > length)
       {
          return s.substring(0, (length - 1)) + tail;
       }
       else
       {
          return s;
       }
    }

}
