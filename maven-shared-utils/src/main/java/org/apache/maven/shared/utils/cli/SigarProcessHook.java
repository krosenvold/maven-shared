package org.apache.maven.shared.utils.cli;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

class SigarProcessHook
    extends ProcessHook {

    private final Sigar sigar = new Sigar();
    private int killSignal;
    
    SigarProcessHook(Process p, int killSignal)
    {
        super(p);
        this.killSignal = killSignal;
    }
  
    @Override
    public void run() 
    {
        long pid = getPid(process);
        try {
            killChildren(pid, getAllProcesses());
        } catch (SigarException e) {
        }
    }

    private long getPid(Process process)  // non portable
    {
        try 
        {
            Class<?> java_lang_UnixProcessClass = Class.forName("java.lang.UNIXProcess");
            if (java_lang_UnixProcessClass.isInstance(process)) 
            {
                Field pidField = java_lang_UnixProcessClass.getDeclaredField("pid");
                pidField.setAccessible(true);
                Object x = pidField.get(process);
                int pidInt = ((Integer)x).intValue();
                return pidInt;
            } 
            else 
            {
                System.out.println("ERROR: process is not an instance of UNIXProcess, cannot get pid.");
                return -1L;
            }
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR: cannot get pid of the process:");
            e.printStackTrace(System.out);
            return -1L;
        }
    }
    
    private Map<Long, ProcState> getAllProcesses() throws SigarException 
    {
        long[] pids = sigar.getProcList();
        Map<Long, ProcState> processes = new HashMap<Long, ProcState>(pids.length);
        for (int i = 0; i < pids.length; i++) 
            processes.put(pids[i], sigar.getProcState(pids[i]));
        return processes;
    }

    private void killChildren(long ppid, Map<Long, ProcState> processes) throws SigarException 
    {
        for (long pid : findChildren(ppid, processes))
        {
            killChildren(pid, processes);
        }
        sigar.kill(String.valueOf(ppid), killSignal);
    }

    private Iterable<Long> findChildren(long ppid, Map<Long, ProcState> processes) throws SigarException 
    {
        List<Long> children = new ArrayList<Long>();
        for (Map.Entry<Long, ProcState> e : processes.entrySet()) 
        {
            if (e.getValue().getPpid() == ppid) 
            {
                children.add(e.getKey());
            }
        }
        return children;
    }

}
