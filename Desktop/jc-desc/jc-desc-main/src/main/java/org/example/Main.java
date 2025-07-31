package org.example;
public class Main {
    int instanceBinary = 45;
    int instanceOctal = 0x14;
    double instanceDouble = 1234.95;


    static int staticHex = 0x1A2B;
    static byte staticByte = 1;
    static String staticString = "Progress, not perfection.";



    public static void main(String[] args) {
        int localDecimal = 100;
        int localHex = 0xFF;
        int localBinary = 0b1101;
        int localOctal = 12;
        int localLong = 10_000_000;
        String localString = "Every step I take gets me closer to my goal.";

        System.out.println("Local Variables: ");
        System.out.println(" localDecimal = " + localDecimal);
        System.out.println(" localHex = " + localHex);
        System.out.println(" localBinary = " + localBinary);
        System.out.println(" localOctal = " + localOctal);
        System.out.println(" localLong = " +localLong);
        System.out.println(" localString = " +localString);

        System.out.println("\nInstance Variables: ");
        Main instanceVariables = new Main();
        System.out.println(" instanceBinary = " + instanceVariables.instanceBinary);
        System.out.println(" instanceOctal = " + instanceVariables.instanceOctal);
        System.out.println(" instanceDouble = " + instanceVariables.instanceDouble);

        System.out.println("\nStatic Varible: ");
        System.out.println(" staticHex = " + staticHex);
        System.out.println(" staticByte = " + staticByte);
        System.out.println(" staticString = " + staticString);
    }


}