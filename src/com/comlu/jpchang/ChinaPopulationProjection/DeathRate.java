package com.comlu.jpchang.ChinaPopulationProjection;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import jxl.*;
import jxl.read.biff.BiffException;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;

class DeathRate {
    
    Workbook workbook;
    Sheet sheet;
    Cell cell;
    double birthRate;
    
    public DeathRate(int y) throws BiffException, IOException{
        workbook = Workbook.getWorkbook(new File("c:/temp/chinapopulation.xls"));
        sheet = workbook.getSheet(0);
        cell = sheet.getCell(2, y);
    }
    
    public double getVal(){
        if (cell.getType() == CellType.NUMBER) 
        { 
          NumberCell nc = (NumberCell) cell; 
          birthRate = nc.getValue(); 
          return birthRate;
        }
        else { return 0.0; }
    }
    
}