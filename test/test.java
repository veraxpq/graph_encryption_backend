import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class test {

    @Test
    public static void main(String arg[]){
        boolean a=true;
        boolean b=false;
        String c="a="+a;
        String d="b="+b;
        System.out.println(c);
        System.out.println(d);
        Calendar calendar=Calendar.getInstance();
        long currentTimeMillis=System.currentTimeMillis();
        String cal=calendar.getTime().toString();
        System.out.println(cal);
        System.out.println(currentTimeMillis);
    }
}
