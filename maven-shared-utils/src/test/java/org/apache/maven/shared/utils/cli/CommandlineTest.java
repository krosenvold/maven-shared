package org.apache.maven.shared.utils.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandlineTest
{

    @Test
    public void testGetCommandline()
        throws Exception
    {

        Commandline cmd = new Commandline(  );
        cmd.setExecutable( "word.exe" );
        cmd.addArguments( "123.txt", "fzz.txt" );
        final String[] commandline = cmd.getCommandline();
        assertEquals(3, commandline.length);
        assertEquals( "word.exe", commandline[0] );
        assertEquals( "123.txt", commandline[1] );
        assertEquals( "fzz.txt", commandline[2] );
    }
}