package org.apache.maven.shared.jar;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

import java.io.File;

/**
 * DefaultJarAnalyzerFactory 
 *
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 * @plexus.component role="org.apache.maven.shared.jar.JarAnalyzerFactory"
 *                   role-hint="default"
 */
public class DefaultJarAnalyzerFactory
    extends AbstractLogEnabled
    implements JarAnalyzerFactory, Contextualizable
{
    private PlexusContainer container;

    public JarAnalyzer getJarAnalyzer( File file )
        throws JarException
    {
        try
        {
            JarAnalyzer jaranalyzer = (JarAnalyzer) container.lookup( JarAnalyzer.ROLE );
            jaranalyzer.setFile( file );
            return jaranalyzer;
        }
        catch ( ComponentLookupException e )
        {
            String emsg = "Unable to load Jar Analyzer for file " + file.getAbsolutePath();
            getLogger().warn( emsg, e );
            throw new JarException( emsg );
        }
    }

    public void contextualize( Context context )
        throws ContextException
    {
        container = (PlexusContainer) context.get( PlexusConstants.PLEXUS_KEY );
    }
}
