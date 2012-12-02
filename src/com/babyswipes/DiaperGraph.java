package com.babyswipes;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
//************ Graph imports

import android.graphics.*;
import android.util.Log;
import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.androidplot.series.XYSeries;
//************* SQlite imports
 
import java.text.*;
import java.util.Arrays;
import java.util.Date;

public class DiaperGraph extends BaseActivity {
	
	private XYPlot mySimpleXYPlot;
	
	
	private BabySwipesDB mDataBase; // database object will need to be global, 
	                                // remove at integration

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_graph);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        
        
        //**************Database simulate data code******************         
        // This code adds data that will already be there.
        // REMOVE FOR INTEGRATION. 
        
        long curTime = System.currentTimeMillis();
        String s = "" + curTime; 
        Log.d("TIME", s);
        curTime = curTime/1000;
        String c = "" + curTime; 
        Log.d("TIME/1000", c);
        int times = (int) curTime; //need int for db methods
        
        mDataBase = new BabySwipesDB(getBaseContext());
        mDataBase.clearAllData();// only use if you want to wipe the data

        mDataBase.addTagType("Diaper Change");

        mDataBase.addSwipe("Diaper Change", times + 100);
        mDataBase.addSwipe("Diaper Change", times + 120);
        mDataBase.addSwipe("Diaper Change", times + 300);
        mDataBase.addSwipe("Diaper Change", times + 307);
        mDataBase.addSwipe("Diaper Change", times + 500);
        mDataBase.addSwipe("Diaper Change", times + 510);
        mDataBase.addSwipe("Diaper Change", times + 578);
        mDataBase.addSwipe("Diaper Change", times + 599);     
        // END REMOVE FOR INTEGRATION. 
       //**************Database sim code******************
       
        

        
        
        // Pull last 8 entries in the database.
        int numRecent = 8;
        
        Number[] time = new Number[numRecent];
        time = mDataBase.getRecentSwipesForTag("Diaper Change", numRecent);
        // need to reverse order of Number[] array for graph display
        for(int i = 0; i < time.length / 2; i++)
        {
            Number temp = time[i];
            time[i] = time[time.length - i - 1];
            time[time.length - i - 1] = temp;
        }
        // Graph has a bug. need to add dummy element to end. 
        //Cleanest way for now is to create a new array.
        Number[] timeGraph = new Number[numRecent+1]; //new array with extra element
        for(int i=0; i<numRecent; i++) //copy data over
        {
        	timeGraph[i] = time[i];
        	Log.d("DEBUG", "timeGraph: " + i +" = " + timeGraph[i]);
        }
        Number x = time[numRecent-1];//get last value in array
        //increment dummy value by some
        x = x.intValue()+100;
        // add dummy value to end
        timeGraph[numRecent] = x; 
        Log.d("DEBUG", "timeGraph: " + numRecent +" = " + timeGraph[numRecent]);


        // Add y values, increments of 1 (with an extra dummy value).
        Number[] numSightings = new Number[numRecent+1]; //plus 1 dummy
        for(int i = 0; i < numRecent+1; i++)
        {
        	numSightings[i] = i+1;
        	Log.d("numSightings", "nums: " + i +" = " + numSightings[i]);
        }
        // End pull last 8 entries
        
              
            
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        // create our series from our array of nums:
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(timeGraph),
                Arrays.asList(numSightings),
                "");
 
        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1,1}, 1));
        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
 
        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        mySimpleXYPlot.getBorderPaint().setStrokeWidth(1);
        mySimpleXYPlot.getBorderPaint().setAntiAlias(false);
        mySimpleXYPlot.getBorderPaint().setColor(Color.WHITE);
 
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 100, 0),                   // line color
                Color.rgb(0, 100, 0),                   // point color
                Color.rgb(100, 200, 0));                // fill color
 
        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE, Color.BLUE, Shader.TileMode.MIRROR));
 
        StepFormatter formatter  = new StepFormatter(Color.rgb(0, 0,0), Color.BLUE);
        formatter.setFillPaint(lineFill);
        mySimpleXYPlot.getGraphWidget().setPaddingRight(2);
        mySimpleXYPlot.addSeries(series2, formatter);
 
        // draw a domain tick for each: divide by 2 so labels don't mesh after 7 x-vals 
        mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, (time.length/2));
        mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
        
        // so min and max are visible
        mySimpleXYPlot.setRangeBoundaries(0, BoundaryMode.FIXED, ((numSightings.length)+1), BoundaryMode.FIXED);
        
 
        // customize our domain/range labels
        mySimpleXYPlot.setDomainLabel("Time");
        mySimpleXYPlot.setRangeLabel("# of Diapers Changed");
 
        // get rid of decimal points in our range labels:
        mySimpleXYPlot.setRangeValueFormat(new DecimalFormat("0"));
 
        mySimpleXYPlot.setDomainValueFormat(new Format() {
 
            private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a E.");
 
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
 
                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }
 
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
 
            }
        });
 
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        mySimpleXYPlot.disableAllMarkup();
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_diaper_graph, menu);
        return true;
    }

}
