package net.raster.grid.ascii.writer;

import net.raster.grid.ascii.GridInvalidException;
import net.raster.grid.ascii.GridScanListener;
import net.raster.grid.ascii.RasterGridAscii;
import net.raster.grid.ascii.header.RasterHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;



/**
 * To write the raster grid;
 *
 * Created by MASCHERPA on 11/12/2020.
 */
public abstract class GridWriter
{
    /* --- properties --- */
    /**
     * total rows in grid;
     */
    private final Integer                       rows;
    /**
     * total columns in each grid row;
     */
    private final Integer                       cols;
    /**
     * grid reader;
     */
    private final BufferedReader                reader;
    /**
     * a single line of source grid;
     */
    private String                              line;
    /**
     * used to replace old NODATA value with new one;
     */
    private final RasterHeader.NoDataReplace    replacer;
    /**
     * number of columns in row;
     */
    private int                                 col;

    /* --- static constructor --- */
    public static GridWriter build
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
        return
                ( scanner == null )
                ? new GridWriterSimple( nRow, nCol, br, first, nodata )
                : new GridWriterListener( nRow, nCol, br, first, nodata, scanner )
                ;
    }
    /* --- constructor --- */
    /**
     * creates new grid writer;
     *
     * @param nRow      total rows in grid;
     * @param nCol      total columns in each row;
     * @param br        the source raster reader;
     * @param first     the first grid line;
     * @param nodata    replacer for NODATA values;
     */
    protected GridWriter
        (
                Integer                     nRow,
                Integer                     nCol,
                BufferedReader              br,
                String                      first,
                RasterHeader.NoDataReplace  nodata
        )
    {
        this.rows       = nRow;
        this.cols       = nCol;
        this.reader     = br;
        this.line       = first;
        this.replacer   = nodata;
    }

    /* --- writer --- */
    /**
     * writes the raster grid;
     *
     * @param writer    the writer;
     * @throws IOException if write failed;
     */
    public void write( Writer writer )
            throws
                IOException
    {
        /* --- begin to write grid --- */
        this.gridBegin();

        /* --- loop on grid lines --- */
        boolean ready;
        int     row         = 0;
        do
        {
            this.rowBegin();

            this.col = 0;

            if ( row++ < rows )
            {
                String data =
                        RasterGridAscii.PATTERN
                                .splitAsStream( this.line.trim() )
                                .map
                                        (
                                                value ->
                                                {
                                                    this.col++;
                                                    return
                                                            this.cell
                                                                    (
                                                                            replacer.replace( value )
                                                                    );
                                                }
                                        )
                                .collect( Collectors.joining( " " ) );

                if ( this.col != cols )
                {
                    throw
                            new GridInvalidException
                                    (
                                            String.format( "Invalid grid. At line %d found %d columns instead %d.", row, col, cols )
                                    );
                }

                writer.write( data );
                writer.write( '\n' );
            }
            else
            {
                throw
                        new GridInvalidException
                                (
                                        String.format( "Invalid grid. Expected %d rows, but found %d rows.", rows, row )
                                );
            }
            this.rowEnd();

            ready = this.reader.ready();
            if ( ready )
            {
                this.line = this.reader.readLine();
            }

        }
        while ( ready )
                ;

        /* --- check all rows have been written --- */
        if ( row != rows )
        {
            throw
                    new GridInvalidException
                            (
                                    String.format( "Invalid grid. Expected %d rows, but found %d rows.", rows, row )
                            );
        }

        /* --- whole grid wrote --- */
        this.gridEnd();
    }

    /* --- abstract methods --- */
    /**
     * called before the grid is written;
     */
    protected abstract void gridBegin();

    /**
     * called just after the grid has been written;
     */
    protected abstract void gridEnd();

    /**
     * called at begin of each row;
     */
    protected abstract void rowBegin();

    /**
     * called at end of each row;
     */
    protected abstract void rowEnd();

    /**
     * called for each grid cell;
     * the actual parameter is cell value and
     * the returned value will be stored in the grid;
     * if the 'NODATA_value' has been changed, each cell with old value
     * will be replaced with the new one before this method is called;
     *
     * @param value    the cell value after replace;
     * @return the value to store in the grid;
     */
    protected abstract String cell( String value );

}
