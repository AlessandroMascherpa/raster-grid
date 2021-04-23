package net.raster.grid.ascii;


import net.raster.grid.ascii.header.GridHeaderInvalidException;
import net.raster.grid.ascii.header.RasterHeader;
import net.raster.grid.ascii.header.RasterHeaderToken;
import net.raster.grid.ascii.header.value.RasterTokenValue;
import net.raster.grid.ascii.writer.GridWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Pattern;


/**
 * The class handles a raster grid in ASCII format.
 *
 * Ascii grid files can be read via the static 'parse(...)' function,
 * updated with setters methods and written via the 'write(...)' method.
 * All values set with below functions will be applied at writing time
 * via 'write(...)' method;
 *
 * File definition can be found at address:
 * https://en.wikipedia.org/wiki/Esri_grid
 *
 * Created by MASCHERPA on 18/02/2020.
 */
public class RasterGridAscii
{
    /* --- properties --- */
    /**
     * the grid header;
     */
    private final RasterHeader    header;
    /**
     * the source grid reader;
     */
    private final BufferedReader  reader;
    /**
     * a single line of source grid;
     */
    private String                line;

    /* --- static properties --- */
    /**
     * regular expression to remove white spaces;
     */
    private static final String   SPACES      = "\\s+";
    /**
     * pattern used to split items in both grid header and body;
     */
    public static final Pattern   PATTERN     = Pattern.compile( SPACES );

    /* --- constructors --- */
    /**
     * parses ascii grid header;
     *
     * @param reader    the source grid reader;
     * @return object of this class;
     * @throws IOException if source reader failed to read;
     * @throws IllegalArgumentException if the source reader was undefined;
     */
    public static RasterGridAscii parse( Reader reader )
            throws
                IOException,
                IllegalArgumentException
    {
        if ( reader == null )
        {
            throw new IllegalAccessError( "Input reader is null." );
        }
        return
                new RasterGridAscii
                        (
                                new BufferedReader( reader )
                        );
    }
    private RasterGridAscii( BufferedReader reader )
            throws
                IOException
    {
        this.reader =  reader;
        this.header =  new RasterHeader();

        /* --- loop on grid header --- */
        RasterHeaderToken token;
        do
        {
            token       = null;
            this.line   = this.reader.readLine();
            if ( this.line != null )
            {
                String[] split = this.line.trim().split( SPACES );
                if ( split.length > 1 )
                {
                    token = RasterHeaderToken.parse( split[ 0 ], split[ 1 ] );
                    if ( token != null )
                    {
                        this.header.store( token );
                    }
                }
            }
        }
        while ( token != null )
            ;

        /* --- check if the header is well formed --- */
        this.header.isHeaderWellFormed();
    }

    /* --- checkers --- */
    /**
     * checks if coordinate are defined at lower-left corner;
     *
     * @return true, if coordinates are lower-left corner; false, otherwise;
     */
    public boolean isCorner()
    {
        return
                this.header.isCorner();
    }
    /**
     * checks if coordinate are defined at cell center;
     *
     * @return true, if coordinates are cell centered; false, otherwise;
     */
    public boolean isCenter()
    {
        return
                this.header.isCenter();
    }

    /**
     * checks if cells are defines as square;
     *
     * @return true; if cells are square; false, otherwise;
     */
    public boolean isCellSquare()
    {
        return
                this.header.isCellSquare();
    }
    /**
     * checks if cells are rectangular;
     *
     * @return true, if cells are rectangular; false, otherwise;
     */
    public boolean isCellRectangular()
    {
        return
                this.header.isCellRectangular();
    }

    /* --- getters'n'setters --- */
    /**
     * gets the grid number of rows;
     *
     * @return the token about the number of rows;
     */
    public RasterTokenValue getNRows()
    {
        return
                this.header.getNRows();
    }
    /**
     * gets the grid number of columns;
     *
     * @return the token about the number of columns;
     */
    public RasterTokenValue getNCols()
    {
        return
                this.header.getNRows();
    }

    /**
     * gets the lower left corner position;
     *
     * @return the x-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getXllCorner()
    {
        return
                this.header.getXllCorner();
    }
    /**
     * gets the lower left corner position;
     *
     * @return the y-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getYllCorner()
    {
        return
                this.header.getYllCorner();
    }
    /**
     * sets the lower-left position;
     * it replaces previous position, both lower-left or center position;
     *
     * @param x    the x position;
     * @param y    the y position;
     * @throws IllegalArgumentException if a coordinate is not valid;
     */
    public void setCorner( String x, String y ) throws IllegalArgumentException
    {
        this.header.setCorner( x, y );
    }
    /**
     * sets the lower-left position;
     * it replaces previous position, both lower-left or center position;
     *
     * @param x    the x position;
     * @param y    the y position;
     * @throws IllegalArgumentException if a coordinate is not valid;
     */
    public void setCorner( double x, double y ) throws IllegalArgumentException
    {
        this.header.setCorner
                (
                        Double.toString( x ),
                        Double.toString( y )
                );
    }

    /**
     * gets the lower left center position;
     *
     * @return the x-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getXllCenter()
    {
        return
                this.header.getXllCenter();
    }
    /**
     * gets the lower left center position;
     *
     * @return the y-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getYllCenter()
    {
        return
                this.header.getYllCenter();
    }
    /**
     * sets the cell center position;
     * it replaces previous position, both lower-left or center position;
     *
     * @param x    the x position;
     * @param y    the y position;
     * @throws IllegalArgumentException if a coordinate is not valid;
     */
    public void setCenter( String x, String y ) throws IllegalArgumentException
    {
        this.header.setCenter( x, y );
    }
    /**
     * sets the cell center position;
     * it replaces previous position, both lower-left or center position;
     *
     * @param x    the x position;
     * @param y    the y position;
     * @throws IllegalArgumentException if a coordinate is not valid;
     */
    public void setCenter( double x, double y ) throws IllegalArgumentException
    {
        this.header.setCenter
                (
                        Double.toString( x ),
                        Double.toString( y )
                );
    }

    /**
     * gets the cell width;
     *
     * @return the cell width; or {@code null if not defined;}
     */
    public RasterTokenValue getCellSize()
    {
        return
                this.header.getCellSize();
    }
    /**
     * set the size of square cell;
     * it replaces previous size, either square or rectangular;
     *
     * Notice: this method does not change the grid;
     *
     * @param size    the cell size;
     * @throws IllegalArgumentException if size is not valid;
     */
    public void setCellSize( String size ) throws IllegalArgumentException
    {
        this.header.setCellSize( size );
    }
    /**
     * set the size of square cell;
     * it replaces previous size, either square or rectangular;
     *
     * Notice: this method does not change the grid;
     *
     * @param size    the cell size;
     * @throws IllegalArgumentException if size is not valid;
     */
    public void setCellSize( double size ) throws IllegalArgumentException
    {
        this.header.setCellSize
                (
                        Double.toString( size )
                );
    }

    /**
     * gets the cell width;
     *
     * @return the cell width; or {@code null if not defined;}
     */
    public RasterTokenValue getDX()
    {
        return
                this.header.getDX();
    }
    /**
     * gets the cell height;
     *
     * @return the cell height; or {@code null if not defined;}
     */
    public RasterTokenValue getDY()
    {
        return
                this.header.getDY();
    }
    /**
     * set the size of rectangular cell;
     * it replaces previous size, either square or rectangular;
     *
     * Notice: this method does not change the grid;
     *
     * @param dx      the cell width;
     * @param dy      the cell height;
     * @throws IllegalArgumentException if size is not valid;
     */
    public void setCellSize( String dx, String dy ) throws IllegalArgumentException
    {
        this.header.setCellSize( dx, dy );
    }
    /**
     * set the size of rectangular cell;
     * it replaces previous size, either square or rectangular;
     *
     * Notice: this method does not change the grid;
     *
     * @param dx      the cell width;
     * @param dy      the cell height;
     * @throws IllegalArgumentException if size is not valid;
     */
    public void setCellSize( double dx, double dy ) throws IllegalArgumentException
    {
        this.header.setCellSize
                (
                        Double.toString( dx ),
                        Double.toString( dy )
                );
    }

    /**
     * gets the cell used used to define a no values cell;
     *
     * @return the no data value;
     */
    public RasterTokenValue getNoDataValue()
    {
        return
                this.header.getNoDataValue();
    }
    /**
     * defines the new NODATA value;
     * at grid writing time, the old value will be replaced with the new one
     * defined with this method;
     *
     * @param value    the new NODATA value;
     */
    public void setNoDataValue( String value )
    {
        this.header.setNoDataValue( value );
    }
    /**
     * defines the new NODATA value;
     * at grid writing time, the old value will be replaced with the new one
     * defined with this method;
     *
     * @param value    the new NODATA value;
     */
    public void setNoDataValue( double value )
    {
        this.header.setNoDataValue
                (
                        Double.toString( value )
                );
    }

    /* --- handling --- */
    /**
     * force the use of square cell instead of rectangular one;
     *
     * @throws GridHeaderInvalidException if header was not well formed;
     */
    public void setForceCellSquare() throws GridHeaderInvalidException
    {
        this.header.forceCellSquare();
    }

    /* --- writers --- */
    /**
     * writes the whole grid and performs the following:
     * - the old NODATA value will be replaced with the new one, if defined;
     * - grid size consistency check;
     *
     * @param writer    grid destination;
     * @throws IOException if the grid cannot be read or written;
     */
    public void write( Writer writer )
            throws
                IOException
    {
        this.write( writer, null );
    }
    /**
     * writes the whole grid and performs the following:
     * - the old NODATA value will be replaced with the new one, if defined;
     * - grid size consistency check;
     *
     * At write end, the writer is flushed.
     *
     * @param writer    grid destination;
     * @param listener  call back methods;
     * @throws IOException if the grid cannot be read or written;
     */
    public void write
    (
            Writer              writer,
            GridScanListener    listener
    )
            throws
                IOException,
                IllegalArgumentException
    {
        /* --- grid size --- */
        Integer  rows    = (Integer) ( this.header.getNRows().getValueAsNumber() );
        Integer  cols    = (Integer) ( this.header.getNCols().getValueAsNumber() );

        /* --- write the header --- */
        this.header.write( writer );

        /* --- write the grid --- */
        GridWriter
                .build
                        (
                                rows, cols, this.reader, this.line, this.header.getNoDataReplacer(), listener
                        )
                .write
                        (
                                writer
                        )
        ;

        /* --- flush buffers --- */
        writer.flush();
    }

}
