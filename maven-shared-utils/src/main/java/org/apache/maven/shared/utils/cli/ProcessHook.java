package org.apache.maven.shared.utils.cli;

abstract class ProcessHook
    extends Thread
{
    protected final Process process;

    ProcessHook( Process process )
    {
        super( "CommandlineUtils process shutdown hook" );
        this.process = process;
        this.setContextClassLoader( null );
    }

    abstract public void run();
}