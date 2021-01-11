package net.raster.grid.ascii.header;

import net.raster.grid.ascii.GridInvalidException;


/**
 * The ASCII Grid header was not correctly formatted, or fields are missing.
 *
 * Created by MASCHERPA on 20/11/2020.
 */
public class GridHeaderInvalidException extends GridInvalidException
{
    /* --- constructors --- */
    public GridHeaderInvalidException( String message )
    {
        super( message );
    }
}
