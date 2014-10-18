package edu.illinois.cs.cogcomp.semeval.utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Timer for progressive tasks
 * @author cheng88
 *
 */
public class Reporter{

    private final long interval;
    private final String message;
    private long startTime;
    private long lastReport;
    private AtomicInteger counter = new AtomicInteger(0);
    private boolean disabled = false;
    
    public Reporter(long reporteringInterval){
        this(reporteringInterval,"");
    }
    /**
     * 
     * @param reportingInterval in milliseconds
     */
    public Reporter(long reportingInterval,String message){
        this.interval = reportingInterval;
        this.message = message;
    }
    
    public Reporter disable(){
        disabled = true;
        return this;
    }
    
    /**
     * Report relevant information regarding current looping speed
     * Reports are internally synchronized, thus parallel looping
     * is also ok.
     * @param loopCount
     * @return true if reported looping speed, false otherwise
     */
    public boolean reportIfNeeded(){
        if (disabled)
            return false;
        if(counter.get() == 0){
            synchronized(this){
                if (counter.get() == 0) {
                    startTime = System.currentTimeMillis();
                    lastReport = startTime;
                }
            }
        }
        counter.incrementAndGet();
        long curTime = System.currentTimeMillis();
        if (curTime - lastReport > interval) {
            synchronized(this){
                if (curTime - lastReport > interval) {
                    lastReport = curTime;
                    long lapsedSeconds = (curTime - startTime) / 1000;
                    System.out.printf("%s %d at %.2f/s\n",message,counter.get(),counter.doubleValue()/ lapsedSeconds);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void reportFinishTime(){
        final TimeUnit[] units = {TimeUnit.HOURS,TimeUnit.MINUTES,TimeUnit.SECONDS};
        ArrayList<Long> parts = new ArrayList<Long>();
        long remaining = System.currentTimeMillis() - startTime;
        for(TimeUnit unit:units){
            long measure = unit.convert(remaining, TimeUnit.MILLISECONDS);
            remaining = remaining - TimeUnit.MILLISECONDS.convert(measure, unit);
            parts.add(measure);
        }
        StringBuilder sb = new StringBuilder("Finished in ");
        for(int i=0;i<units.length;i++){
            sb.append(parts.get(i))
            .append(' ')
            .append(units[i])
            .append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }
    
}