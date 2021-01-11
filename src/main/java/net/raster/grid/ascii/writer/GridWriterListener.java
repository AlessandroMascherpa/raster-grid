package net.raster.grid.ascii.writer;


import net.raster.grid.ascii.GridScanListener;
import net.raster.grid.ascii.header.RasterHeader;

import java.io.BufferedReader;


/**
 * Grid writer with listener according the interface: GridScanListener;
 *
 * Created by MASCHERPA on 11/12/2020.
 */
public class GridWriterListener extends GridWriter
{
    /* --- properties --- */
    private final GridScanListener listener;

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
    protected GridWriterListener
    (
            Integer                     nRow,
            Integer                     nCol,
            BufferedReader              br,
            String                      first,
            RasterHeader.NoDataReplace  nodata,
            GridScanListener            scanner
    )
            throws
                IllegalArgumentException
    {
        super( nRow, nCol, br, first, nodata );
        if ( scanner == null )
        {
            throw
                    new IllegalArgumentException( "Listener not defined." );
        }
        this.listener = scanner;
    }

    /* --- implemented methods --- */
    @Override
    protected void gridBegin()
    {
        this.listener.gridBegin();
    }

    @Override
    protected void gridEnd()
    {
        this.listener.gridEnd();
    }

    @Override
    protected void rowBegin()
    {
        this.listener.rowBegin();
    }

    @Override
    protected void rowEnd()
    {
        this.listener.rowEnd();
    }

    @Override
    protected String cell( String value )
    {
        return
                this.listener.cell( value );
    }

}
