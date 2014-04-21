package com.example.wmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import models.ContactSummary;
import models.GraphViewData;
import models.Iou;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * This class represents the statistics page
 * Graph with loans made over the past month
 * Bar chart with loans per contact
 * @author Galen
 *
 */
@SuppressLint("NewApi")
public class StatisticsFragment extends Fragment {
	
	private ArrayList<Iou> iou_list;
	private ArrayList<ContactSummary> contact_list;
	private ArrayList<Double> money_list;
	private double moneys[];
	private Date cutoffDate;
	private GraphView graphView;
	private GraphView barGraphView;
	
	public StatisticsFragment() {}

    @SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {

    	View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
    	
    	// Query database
    	iou_list = models.Global.iou_db_mgr.get_ious_ordered_by_earliest_loan_date();
    	contact_list = models.Global.iou_db_mgr.get_contact_summaries();
    	money_list = new ArrayList<Double>();
    	for(int i=0; i<contact_list.size(); i++) {
    		money_list.add(contact_list.get(i).total_val());
    	}
    	
    	// Get date of 1 month prior to today
    	Calendar c = Calendar.getInstance();  
    	c.add(Calendar.MONTH, -1);
    	cutoffDate = c.getTime();
    	
    	// Fill array with loan values
    	moneys = new double[31];
    	Arrays.fill(moneys,0);
    	for(int i=0; i<iou_list.size(); i++) {
    		if(iou_list.get(i).item_type().equals("Money")) {
    			Date date = iou_list.get(i).date_borrowed();
    			
    			if(date.after(cutoffDate))
    				moneys[date.getDate()] += iou_list.get(i).value();
    		}
    	}
    	
    	// init series data
    	GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
    	    new GraphViewData(1, moneys[1])
    	    , new GraphViewData(2, moneys[2])
    	    , new GraphViewData(3, moneys[3])
    	    , new GraphViewData(4, moneys[4])
    	    , new GraphViewData(5, moneys[5])
    	    , new GraphViewData(6, moneys[6])
    	    , new GraphViewData(7, moneys[7])
    	    , new GraphViewData(8, moneys[8])
    	    , new GraphViewData(9, moneys[9])
    	    , new GraphViewData(10, moneys[10])
    	    , new GraphViewData(11, moneys[11])
    	    , new GraphViewData(12, moneys[12])
    	    , new GraphViewData(13, moneys[13])
    	    , new GraphViewData(14, moneys[14])
    	    , new GraphViewData(15, moneys[15])
    	    , new GraphViewData(16, moneys[16])
    	    , new GraphViewData(17, moneys[17])
    	    , new GraphViewData(18, moneys[18])
    	    , new GraphViewData(19, moneys[19])
    	    , new GraphViewData(20, moneys[20])
    	    , new GraphViewData(21, moneys[21])
    	    , new GraphViewData(22, moneys[22])
    	    , new GraphViewData(23, moneys[23])
    	    , new GraphViewData(24, moneys[24])
    	    , new GraphViewData(25, moneys[25])
    	    , new GraphViewData(26, moneys[26])
    	    , new GraphViewData(27, moneys[27])
    	    , new GraphViewData(28, moneys[28])
    	    , new GraphViewData(29, moneys[29])
    	    , new GraphViewData(30, moneys[30])
    	});

    	GraphViewData[] viewData;
    	String contactArray[];
    	int numLabels = 4;
    	if(money_list.size() > 4) {
    		viewData = new GraphViewData[4];
    		contactArray = new String[4];
        	for(int i=0; i<4; i++) {
        		viewData[i] = new GraphViewData(i+1, money_list.get(i));
        		contactArray[i] = contact_list.get(i).contact();
        	}
    	}
    	else {
    		viewData = new GraphViewData[money_list.size()];
    		contactArray = new String[money_list.size()];
    		numLabels = money_list.size();
        	for(int i=0; i<money_list.size(); i++) {
        		viewData[i] = new GraphViewData(i+1, money_list.get(i));
        		contactArray[i] = contact_list.get(i).contact();
        	}
    	}
    	
    	
    	GraphViewSeries barSeries = new GraphViewSeries(viewData);
    	
    	// Create graph view and set display to be in integers
    	barGraphView = new BarGraphView (getActivity(), "Loans By Contact");
    	barGraphView.setHorizontalLabels(contactArray);
    	barGraphView.getGraphViewStyle().setNumHorizontalLabels(4);
    	barGraphView.getGraphViewStyle().setTextSize(11);
    	barGraphView.setScalable(true);
    	barGraphView.addSeries(barSeries); // data
    	
    	// Create graph view and set display to be in integers
    	graphView = new LineGraphView(getActivity(), "Loans Over Past Month") {
    		   @Override
    		   protected String formatLabel(double value, boolean isValueX) {
    		      return ""+((int) value);
    		   }
    		};
    		
    	// Set styles
    	graphView.getGraphViewStyle().setNumHorizontalLabels(15);
    	graphView.getGraphViewStyle().setTextSize(12);
    	graphView.setScrollable(false);
    	graphView.setScalable(true);
    	graphView.addSeries(exampleSeries); // data
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.statisticsLayout);
		layout.addView(graphView);
		layout.addView(barGraphView);

    	return rootView;
    }
    
    public void setMonthChart() {
    	graphView.setVisibility(View.VISIBLE);
    	barGraphView.setVisibility(View.GONE);
    }
    public void setContactChart() {
    	graphView.setVisibility(View.GONE);
    	barGraphView.setVisibility(View.VISIBLE);
    }
	
}
