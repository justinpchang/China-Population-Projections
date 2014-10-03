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

class Dates {
    
    Workbook workbook;
    Sheet sheet;
    Cell cell;
    int date;
    
    public Dates(int y) throws BiffException, IOException{
        workbook = Workbook.getWorkbook(new File("c:/temp/chinapopulation.xls"));
        sheet = workbook.getSheet(0);
        cell = sheet.getCell(0, y);
    }
    
    public int getVal(){
        if (cell.getType() == CellType.NUMBER) 
        { 
          NumberCell nc = (NumberCell) cell; 
          date = (int) nc.getValue(); 
          return date;
        }
        else { return 0; }
    }
    
}