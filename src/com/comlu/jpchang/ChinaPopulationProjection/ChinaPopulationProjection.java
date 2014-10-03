package com.comlu.jpchang.ChinaPopulationProjection;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import jxl.*;
import jxl.read.biff.BiffException;

class ChinaPopulationProjection {
    
    public static void main(String args[]) throws BiffException, IOException {
        
        ArrayList<Dates> dates = new ArrayList<Dates>();
        ArrayList<BirthRate> birthRates = new ArrayList<BirthRate>();
        ArrayList<BirthRate> CBRocp = new ArrayList<BirthRate>();
        ArrayList<DeathRate> deathRates = new ArrayList<DeathRate>();
        ArrayList<DeathRate> CDRocp = new ArrayList<DeathRate>();
        
        double[] CBRdif = new double[53];
        double[] CBRocpDif = new double[41];
        double[] CDRdif = new double[53];
        double[] CDRocpDif = new double[41];
        double CBRdifSum = 0,CBRdifAvg,
               CBRocpDifSum = 0,CBRocpDifAvg,
               CDRdifSum = 0,CDRdifAvg,
               CDRocpDifSum = 0,CDRocpDifAvg;
    
        double[] CBRocpExtrap = new double[96];
        double[] CDRocpExtrap = new double[96];
        double[] CBRExtrap = new double[96];
        
        double[] NIRocp = new double[149];
        double[] NIR = new double[149];
        
        for(int y = 1; y < 55; y++) {
            // add dates to array list (count: 56)
            dates.add(new Dates(y));
            // add birth rate data to array list
            birthRates.add(new BirthRate(y));
            // add death rate data to array list
            deathRates.add(new DeathRate(y));
        }
        
        // loading data into CBRocp and CDRocp
        for(int y = 15; y < 57; y++) {
            CBRocp.add(new BirthRate(y));
            CDRocp.add(new DeathRate(y));
        }
        
        // getting differences
        // cbr and cdr all
        for(int i = 0; i < 53; i++) {
            CBRdif[i] = (birthRates.get(i+1).getVal() - birthRates.get(i).getVal()) / birthRates.get(i).getVal();
            CDRdif[i] = (deathRates.get(i+1).getVal() - deathRates.get(i).getVal()) / deathRates.get(i).getVal();
        }
        // cbr and cdr ocp
        for(int i = 0; i < 41; i++) {
            CBRocpDif[i] = (CBRocp.get(i+1).getVal() - CBRocp.get(i).getVal()) / CBRocp.get(i).getVal();
            CDRocpDif[i] = (CDRocp.get(i+1).getVal() - CDRocp.get(i).getVal()) / CDRocp.get(i).getVal();
        }
        
        // adding sum of rates
        // cbr and cdr all
        for(int i = 0; i < 53; i++) {
            CBRdifSum += CBRdif[i];
            CDRdifSum += CDRdif[i];
        }
        // cbr and cdr ocp
        for(int i = 0; i < 41; i++) {
            CBRocpDifSum += CBRocpDif[i];
            CDRocpDifSum += CDRocpDif[i];
        }
        
        // calculate averages
        CBRdifAvg = CBRdifSum / 53;
        CDRdifAvg = CDRdifSum / 53;
        CBRocpDifAvg = CBRocpDifSum / 41;
        CDRocpDifAvg = CDRocpDifSum / 41;
        
        // extrapolation
        double prevNum = birthRates.get(53).getVal();
        for(int i = 0; i < 96; i++) {
            CBRocpExtrap[i] = prevNum + (prevNum * CBRocpDifAvg);
            prevNum = CBRocpExtrap[i];
        }
        prevNum = deathRates.get(53).getVal();
        for(int i = 0; i < 96; i++) {
            CDRocpExtrap[i] = prevNum + (prevNum * CDRocpDifAvg);
            prevNum = CDRocpExtrap[i];
        }
        prevNum = birthRates.get(53).getVal();
        for(int i = 0; i < 96; i++) {
            CBRExtrap[i] = prevNum + (prevNum * CBRdifAvg);
            prevNum = CBRExtrap[i];
        }
        
        // randomize extrapolation
        for(int i=0; i < 96; i++) {
            CBRocpExtrap[i] = CBRocpExtrap[i] * (Math.random() + 0.5);
            CDRocpExtrap[i] = CDRocpExtrap[i] * (Math.random() + 0.5);
            CBRExtrap[i] = CBRExtrap[i] * (Math.random() + 0.5);
        }
        
        // calculate natural increase rate
        for(int i = 0; i < 53; i++) {
            NIR[i] = birthRates.get(i).getVal() - deathRates.get(i).getVal();
        }
        for(int i = 54; i < 96; i++) {
            NIRocp[i] = CBRocpExtrap[i] - CDRocpExtrap[i];
            NIR[i] = CBRExtrap[i] - CDRocpExtrap[i];
        }
        
        System.out.println(CBRdifAvg * 100 + "% average rate of change in CBR");
        System.out.println(CDRdifAvg * 100 + "% average rate of change in CDR");
        System.out.println(CBRocpDifAvg * 100 + "% average rate of change in CBR from 1978-2004");
        System.out.println(CDRocpDifAvg * 100 + "% average rate of change in CDR from 1978-2004");
        System.out.println(birthRates.get(53).getVal() + " is the birth rate in 2004");
        System.out.println(CBRocpExtrap[95] + " is the projected CBR in 2100 with the One Child Policy");
        System.out.println("==================================================");
        System.out.println("With One Child Policy kept:");
        System.out.println("Year:\tCBR:\tCDR:\tNIR");
        int year = 1949;
        for(int i = 0; i < 53; i++) {
            System.out.println(year + "\t" + truncateDecimal(birthRates.get(i)
                    .getVal(),3) + "\t" + truncateDecimal(deathRates.get(i)
                    .getVal(),3) + "\t" + truncateDecimal(NIR[i],3));
            year++;
        }
        System.out.println("----------------------------------");
        year = 2005;
        for(int i = 0; i < 96; i++) {
            System.out.println(year + "\t" + truncateDecimal(CBRocpExtrap[i],3)
                    + "\t" + truncateDecimal(CDRocpExtrap[i],3) + "\t"
                    + truncateDecimal(NIR[i],3));
            year++;
        }
        System.out.println("==================================");
        System.out.println("Without One Child Policy kept:");
        System.out.println("Year:\tCBR:\tCDR:\tNIR");
        year = 2005;
        for(int i = 0; i < 96; i++) {
            System.out.println(year + "\t" + truncateDecimal(CBRExtrap[i],3)
                    + "\t" + truncateDecimal(CDRocpExtrap[i],3) + "\t"
                    + truncateDecimal(NIR[i],3));
            year++;
        }
        
    }
    private static BigDecimal truncateDecimal(double x,int numberofDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}