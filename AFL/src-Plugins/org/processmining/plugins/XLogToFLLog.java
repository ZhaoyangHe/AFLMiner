package org.processmining.plugins;

import java.util.ArrayList;

import org.deckfour.xes.model.XLog;
import org.processmining.Data.MyEvent;
import org.processmining.Data.MyLog;
import org.processmining.Data.MyTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

public class XLogToFLLog {
	@Plugin(name = "XLogChange", parameterLabels = { "XLog" }, returnLabels = { "XLog" }, returnTypes = { XLog.class })
	@UITopiaVariant(affiliation = "SDUST", author = "Zhaoyang He", email = "yz1022918@163.com")
	public static XLog doMining(PluginContext context, XLog xlog) {
		XLog log = (XLog) xlog.clone();
		ArrayList<Integer> removelist = new ArrayList<Integer>();
		MyLog mylog = new MyLog(log);
		for (int tracesize = 0; tracesize < mylog.size(); tracesize++) {
			MyTrace mytrace = mylog.get(tracesize);
			System.out.println("tracesize :" + tracesize);
			System.out.println("mytracesize :" + mytrace.size());
			if(mytrace.size()>=4){
				for (int eventsize = 0; eventsize < (mytrace.size()-2); eventsize++) {
					MyEvent myevent1 = mytrace.get(eventsize);
					MyEvent myevent2 = mytrace.get(eventsize + 1);

					String s1 = myevent1.getName();
					String s2 = myevent2.getName();
					System.out.println("s1 :" + s1 + "   s2 :" + s2 );
					if (s1.equals(s2)) {
                        System.out.println("remove :" + tracesize);
							removelist.add(tracesize);
							break;
						}
					}
				
				if(mytrace.size()>6){
					for (int eventsize = 0; eventsize < (mytrace.size()-5); eventsize++) {
						MyEvent myevent1 = mytrace.get(eventsize);
						MyEvent myevent2 = mytrace.get(eventsize + 1);
						MyEvent myevent3 = mytrace.get(eventsize + 2);
						MyEvent myevent4 = mytrace.get(eventsize + 3);
						String s1 = myevent1.getName();
						String s2 = myevent2.getName();
						String s3 = myevent1.getName();
						String s4 = myevent2.getName();
						System.out.println("s1 :" + s1 + "   s2 :" + s2 );
						if (s1.equals(s2)) {
	                        System.out.println("remove :" + tracesize);
								removelist.add(tracesize);
								break;
							}
						}
				
				
				
				}


			}
			
		}
		System.out.println("removelist :" + removelist);
		for (int i = 0; i < removelist.size(); i++) {
			int removei = removelist.get(i)-i;
			
			log.remove(removei);
		}

		return log;
	}

}