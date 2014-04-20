package com.example.wmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import models.GraphViewData;
import models.Iou;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class StatisticsFragment extends Fragment {
	
	private ArrayList<Iou> iou_list;
	public StatisticsFragment() {}

    @SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {

    	View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
    	
    	iou_list = models.Global.iou_db_mgr.get_ious_ordered_by_earliest_loan_date();
    	double moneys[] = new double[30];
    	Arrays.fill(moneys,0);
    	
    	for(int i=0; i<iou_list.size(); i++) {
    		if(iou_list.get(i).item_type().equals("Money")) {
    			Date date = iou_list.get(i).date_borrowed();
    			Log.e("TAG", Integer.toString(date.getDate()));
    			moneys[date.getDate()] += iou_list.get(i).value();
    		}
    	}
    	
    	for(int j=0; j<30; j++) {
        	Log.e("6", Double.toString(moneys[j]));
    	}
    	// init example series data
    	GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
    	    new GraphViewData(1, moneys[0])
    	    , new GraphViewData(2, moneys[1])
    	    , new GraphViewData(3, moneys[2])
    	    , new GraphViewData(4, moneys[3])
    	    , new GraphViewData(5, moneys[4])
    	    , new GraphViewData(6, moneys[5])
    	    , new GraphViewData(7, moneys[6])
    	    , new GraphViewData(8, moneys[7])
    	    , new GraphViewData(9, moneys[8])
    	    , new GraphViewData(10, moneys[9])
    	    , new GraphViewData(11, moneys[10])
    	    , new GraphViewData(12, moneys[11])
    	    , new GraphViewData(13, moneys[12])
    	    , new GraphViewData(14, moneys[13])
    	    , new GraphViewData(15, moneys[14])
    	    , new GraphViewData(16, moneys[15])
    	    , new GraphViewData(17, moneys[16])
    	    , new GraphViewData(18, moneys[17])
    	    , new GraphViewData(19, moneys[18])
    	    , new GraphViewData(20, moneys[19])
    	    , new GraphViewData(21, moneys[20])
    	    , new GraphViewData(22, moneys[21])
    	    , new GraphViewData(23, moneys[22])
    	    , new GraphViewData(24, moneys[23])
    	    , new GraphViewData(25, moneys[24])
    	    , new GraphViewData(26, moneys[25])
    	    , new GraphViewData(27, moneys[26])
    	    , new GraphViewData(28, moneys[27])
    	    , new GraphViewData(29, moneys[28])
    	    , new GraphViewData(30, moneys[29])
    	});
    	
    	GraphView graphView = new LineGraphView(getActivity(), "Loans Over Past Month") {
    		   @Override
    		   protected String formatLabel(double value, boolean isValueX) {
    		      // return as Integer
    		      return ""+((int) value);
    		   }
    		};
    	graphView.getGraphViewStyle().setNumHorizontalLabels(15);
    	graphView.getGraphViewStyle().setTextSize(12);
    	//graphView.setViewPort(1, 29);
    	graphView.setScrollable(false);
    	graphView.setScalable(true);
    	graphView.addSeries(exampleSeries); // data
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.statisticsLayout);
		layout.addView(graphView);

    	return rootView;
    }
	
}
