package org.processmining.plugins;

import java.util.ArrayList;

import org.deckfour.xes.model.XLog;
import org.processmining.Data.MyEvent;
import org.processmining.Data.MyLog;
import org.processmining.Data.MyTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

public class LogABA {
	@Plugin(name = "XLogABA", parameterLabels = { "XLog" }, returnLabels = { "XLog" }, returnTypes = { XLog.class })
	@UITopiaVariant(affiliation = "SDUST", author = "Zhaoyang He", email = "yz1022918@163.com")
	public static XLog doMining(PluginContext context, XLog xlog) {
		XLog log = (XLog) xlog.clone();
		ArrayList<Integer> removelist = new ArrayList<Integer>();
		MyLog mylog = new MyLog(log);
		for (int tracesize = 0; tracesize < mylog.size(); tracesize++) {
			MyTrace mytrace = mylog.get(tracesize);
			if(mytrace.size()>=4){
				for (int eventsize = 0; eventsize < (mytrace.size()-3); eventsize++) {
					MyEvent myevent1 = mytrace.get(eventsize);
					MyEvent myevent2 = mytrace.get(eventsize + 1);
					MyEvent myevent3 = mytrace.get(eventsize + 2);
					String s1 = myevent1.getName();
					String s2 = myevent2.getName();
					String s3 = myevent3.getName();


					if (s1.equals("N")&&s2.equals("P")&&s3.equals("N")) {
                        System.out.println("Exist NPN :" + (tracesize+1));
							break;
						}

					}
				
				


			}
			
		}

		return log;
	}

}