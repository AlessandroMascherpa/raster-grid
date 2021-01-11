package net.raster.grid.ascii;


import java.io.IOException;


/**
 * The ASCII Grid is invalid.
 *
 * Created by MASCHERPA on 26/11/2020.
 */
public class GridInvalidException extends IOException
{
    /* --- constructors --- */
    public GridInvalidException( String message )
    {
        super( message );
    }
}
