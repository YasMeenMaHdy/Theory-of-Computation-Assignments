
package regularexpression;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegularExpression {
    
    public static void main(String[] args) {
        Scanner in= new Scanner(System.in);
        System.out.println("Enter Your Regular Expression");
        //generating randome strings that matche with given RE
        String regex=in.nextLine();
        //[0-3]([a-c]|[e-g]{1,2})
        Generex generex = new Generex(regex);
        for (int i = 0; i < 10; i++) {
            System.out.println(generex.random());
		}
        //taking a string and check matching
        System.out.println("Enter Your String to Check Maching");
       String inputTxt=in.nextLine();
        Pattern pt= Pattern.compile(regex);
        Matcher mt= pt.matcher(inputTxt);
        boolean matching= mt.matches();
        System.out.println("Matchin is "+matching);
    }
    
}
