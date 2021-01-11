package net.raster.grid.ascii.writer;


import net.raster.grid.ascii.header.RasterHeader;

import java.io.BufferedReader;


/**
 * To write grid without listener;
 *
 * Created by MASCHERPA on 11/12/2020.
 */
public class GridWriterSimple extends GridWriter
{
    /* --- constructor --- */
    /**
     * creates new grid writer;
     *
     * @param nRow   total rows in grid;
     * @param nCol   total columns in each row;
     * @param br     the source raster reader;
     * @param first  the first grid line;
     * @param nodata replacer for NODATA values;
     */
    public GridWriterSimple
        (
                Integer                     nRow,
                Integer                     nCol,
                BufferedReader              br,
                String                      first,
                RasterHeader.NoDataReplace  nodata
        )
    {
        super( nRow, nCol, br, first, nodata );
    }

    /* --- implemented methods --- */
    @Override
    protected void gridBegin()
    {
        /* do nothing */
    }

    @Override
    protected void gridEnd()
    {
        /* do nothing */
    }

    @Override
    protected void rowBegin()
    {
        /* do nothing */
    }

    @Override
    protected void rowEnd()
    {
        /* do nothing */
    }

    @Override
    protected String cell( String value )
    {
        return value;
    }

}
