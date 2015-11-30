package org.apache.maven.shared.dependency.analyzer.asm;


import org.objectweb.asm.commons.Remapper;

public class Collector
    extends Remapper
{

    private final ResultCollector classNames;

    private final String prefix;

    public Collector( final ResultCollector resultCollector, final String prefix )
    {
        this.classNames = resultCollector;
        this.prefix = prefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String mapDesc( final String desc )
    {
        if ( desc.startsWith( "L" ) )
        {
            this.addType( desc.substring( 1, desc.length() - 1 ) );
        }
        return super.mapDesc( desc );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] mapTypes( final String[] types )
    {
        for ( final String type : types )
        {
            this.addType( type );
        }
        return super.mapTypes( types );
    }

    private void addType( final String type )
    {
        final String className = type.replace( '/', '.' );
        if ( className.startsWith( this.prefix ) )
        {
            this.classNames.add( className );
        }
    }

    @Override
    public String mapType( final String type )
    {
        this.addType( type );
        return type;
    }

}