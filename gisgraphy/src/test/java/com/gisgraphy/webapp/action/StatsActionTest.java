package com.gisgraphy.webapp.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.repository.IStatsUsageDao;
import com.gisgraphy.service.IStatsUsageService;
import com.gisgraphy.stats.StatsUsage;
import com.gisgraphy.stats.StatsUsageType;
import com.opensymphony.xwork2.Action;

public class StatsActionTest {

    @Test
    public void testExecute() throws Exception {
	List<StatsUsage> statsUsageList = new ArrayList<StatsUsage>();
	StatsUsage statsUsage1 = new StatsUsage(StatsUsageType.FULLTEXT);
	StatsUsage statsUsage2 = new StatsUsage(StatsUsageType.GEOLOC);
	StatsUsage statsUsage3 = new StatsUsage(StatsUsageType.STREET);
	statsUsage1.setUsage(10L);
	statsUsage2.setUsage(20L);
	statsUsage3.setUsage(30L);
	statsUsageList.add(statsUsage1);
	statsUsageList.add(statsUsage2);
	statsUsageList.add(statsUsage3);
	
	StatsAction statsAction = new StatsAction();
	IStatsUsageDao mockStatsUsageDao = EasyMock.createMock(IStatsUsageDao.class);
	EasyMock.expect(mockStatsUsageDao.getAll()).andReturn(statsUsageList).times(2);
	EasyMock.replay(mockStatsUsageDao);
	statsAction.setStatsUsageDao(mockStatsUsageDao);
	String returnString = statsAction.execute();
	Assert.assertEquals("statsusage should be loaded when execute is called",statsUsageList,statsAction.getStatsUsages());
	Assert.assertEquals(Action.SUCCESS, returnString);
	Assert.assertEquals(60L, statsAction.getTotalUsage().longValue());
	returnString = statsAction.execute();
	Assert.assertEquals("totalusage should not be recursively added for each call to execute", 60L, statsAction.getTotalUsage().longValue());
	EasyMock.verify(mockStatsUsageDao);
    }
    
    @Test
    public void testGetFlushFrequency(){
	StatsAction statsAction = new StatsAction();
	assertEquals("getFlushFrequecy should return the flush threshold",IStatsUsageService.FLUSH_THRESHOLD, statsAction.getFlushFrequency());
    }

   
}
