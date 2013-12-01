package org.apache.maven.shared.utils.cli;

class StandardProcessHook
    extends ProcessHook 
{

    StandardProcessHook(Process p)
    {
        super(p);
    }
  
    @Override
    public void run() 
    {
        process.destroy();
    }

}
