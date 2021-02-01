package net.raster.grid.ascii.header;


import net.raster.grid.ascii.header.value.RasterTokenValue;

import java.io.IOException;
import java.io.Writer;


/**
 * Stores all header of ASCII Grid file.
 * Moreover, it checks the header correctness;
 *
 * Created by MASCHERPA on 20/11/2020.
 */
public class RasterHeader
{
    /* --- properties --- */
    private RasterHeaderToken   ncols;
    private RasterHeaderToken   nrows;
    private RasterHeaderToken   xllcorner;
    private RasterHeaderToken   yllcorner;
    private RasterHeaderToken   xllcenter;
    private RasterHeaderToken   yllcenter;
    private RasterHeaderToken   cellsize;
    private RasterHeaderToken   dx;
    private RasterHeaderToken   dy;
    private RasterHeaderToken   nodata;

    /* --- properties for data handling --- */
    /**
     * current state of parser;
     */
    private int                 parseState          = 0;
    /**
     * if the change of the NODATA_VALUE has been requested, this field
     * stores the very first NODATA_VALUE, if it was defined;
     */
    private String              nodataOriginal;

    /* --- constructors --- */
    /* none */

    /* --- setters'n'getters --- */
    /**
     * stores a token header and it attempts to keep the header consistency;
     *
     * @param token    header token to collect;
     * @throws GridHeaderInvalidException if header is not syntactically correct;
     */
    public void store( RasterHeaderToken token ) throws GridHeaderInvalidException
    {
        if ( token == null )
        {
            throw new IllegalArgumentException
                    (
                            "The token was not defined."
                    );
        }
        switch ( this.parseState )
        {
            case 0:
                switch ( token )
                {
                    case N_COLLS:
                        this.ncols      = token;
                        this.parseState = 1;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 1:
                switch ( token )
                {
                    case N_ROWNS:
                        this.nrows      = token;
                        this.parseState = 2;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 2:
                switch ( token )
                {
                    case X_LL_CORNER:
                        this.xllcorner  = token;
                        this.parseState = 3;
                        break;

                    case X_LL_CENTER:
                        this.xllcenter  = token;
                        this.parseState = 4;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 3:
                switch ( token )
                {
                    case Y_LL_CORNER:
                        this.yllcorner  = token;
                        this.parseState = 5;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 4:
                switch ( token )
                {
                    case Y_LL_CENTER:
                        this.yllcenter  = token;
                        this.parseState = 5;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 5:
                switch ( token )
                {
                    case CELL_SIZE:
                        this.cellsize   = token;
                        this.parseState = 7;
                        break;

                    case DX:
                        this.dx         = token;
                        this.parseState = 6;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 6:
                switch ( token )
                {
                    case DY:
                        this.dy         = token;
                        this.parseState = 7;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 7:
                switch ( token )
                {
                    case NODATA:
                        this.nodata     = token;
                        this.parseState = 8;
                        break;

                    default:
                        syntaxError( token );
                        break;
                }
                break;

            case 8:
                break;

            default:
                throw new GridHeaderInvalidException
                        (
                                "Internal parser error. Reached the handled state: " + this.parseState
                        );
        }
    }

    /* --- checkers --- */
    /**
     * checks whether the header is correctly formatted;
     *
     * @throws GridHeaderInvalidException if the header is not well formed;
     */
    public void isHeaderWellFormed() throws GridHeaderInvalidException
    {
        if ( ( this.parseState != 7 ) && ( this.parseState != 8 ) )
        {
            if ( ( this.ncols == null ) || ( this.nrows == null ) )
            {
                throw
                        new GridHeaderInvalidException( "The grid size was not defined." );
            }
            if ( ! this.isOriginDefined() )
            {
                throw
                        new GridHeaderInvalidException( "Grid position not defined." );
            }
            if ( ! this.isCellSizeDefined() )
            {
                throw
                        new GridHeaderInvalidException( "Cell size not defined." );
            }
        }
    }

    private static void syntaxError( RasterHeaderToken token ) throws GridHeaderInvalidException
    {
        throw new GridHeaderInvalidException
                (
                        "Syntax error. Unexpected token '" + token.getName() + "'."
                );
    }
    private void assertDxDyDefined() throws GridHeaderInvalidException
    {
        if ( ( this.dx == null ) || ( this.dy == null ) )
        {
            throw new GridHeaderInvalidException
                    (
                            "The rectangular cell size was partially defined (only DX or only DY)."
                    );
        }
    }

    /**
     * checks if coordinate are defined at lower-left corner;
     *
     * @return true, if coordinates are lower-left corner; false, otherwise;
     */
    public boolean isCorner()
    {
        return
                ( ( this.xllcorner != null ) && ( this.yllcorner != null ) ) ;
    }
    /**
     * checks if coordinate are defined at cell center;
     *
     * @return true, if coordinates are cell centered; false, otherwise;
     */
    public boolean isCenter()
    {
        return
                ( ( this.xllcenter != null ) && ( this.yllcenter != null ) );
    }

    /**
     * checks if cells are defines as square;
     *
     * @return true; if cells are square; false, otherwise;
     */
    public boolean isCellSquare()
    {
        return
                ( this.cellsize != null );
    }
    /**
     * checks if cells are rectangular;
     *
     * @return true, if cells are rectangular; false, otherwise;
     */
    public boolean isCellRectangular()
    {
        return
                ( ( this.dx != null ) && ( this.dy != null ) );
    }

    /* --- header handling --- */
    /**
     * forces the use of square cell size instead of rectangular cell size;
     *
     * @throws GridHeaderInvalidException if grid position is not correctly defined;
     */
    public void forceCellSquare() throws GridHeaderInvalidException
    {
        if ( ! this.isOriginDefined() )
        {
            throw
                    new GridHeaderInvalidException( "Grid position not correctly defined." );
        }
        if ( this.cellsize == null )
        {
            this.assertDxDyDefined();

            Double dx = (Double) ( this.dx.getValue().getValueAsNumber() );
            Double dy = (Double) ( this.dy.getValue().getValueAsNumber() );
            if ( dx > dy )
            {
                double corner
                        = ( (Double) ( this.yllcorner.getValue().getValueAsNumber() ) )
                        -
                        (
                                ( dx - dy )  * ( (Integer) ( this.nrows.getValue().getValueAsNumber() ) )
                        )
                        ;

                this.yllcorner = RasterHeaderToken.parse
                        (
                                RasterHeaderToken.Y_LL_CORNER.getName(),
                                String.valueOf( corner )
                        );
                this.cellsize = RasterHeaderToken.parse
                        (
                                RasterHeaderToken.CELL_SIZE.getName(),
                                this.dx.getValue().getValueAsText()
                        );
                this.dx       = null;
                this.dy       = null;
            }
            else if ( dx < dy )
            {
                double corner
                        = ( (Double) ( this.xllcorner.getValue().getValueAsNumber() ) )
                        -
                        (
                                ( dy - dx )  * ( (Integer) ( this.ncols.getValue().getValueAsNumber() ) )
                        )
                        ;

                this.xllcorner = RasterHeaderToken.parse
                        (
                                RasterHeaderToken.X_LL_CORNER.getName(),
                                String.valueOf( corner )
                        );
                this.cellsize = RasterHeaderToken.parse
                        (
                                RasterHeaderToken.CELL_SIZE.getName(),
                                this.dy.getValue().getValueAsText()
                        );
                this.dx       = null;
                this.dy       = null;
            }
            else
            {
                this.cellsize = RasterHeaderToken.parse
                        (
                                RasterHeaderToken.CELL_SIZE.getName(),
                                this.dx.getValue().getValueAsText()
                        );
                this.dx       = null;
                this.dy       = null;
            }
        }
    }

    /* --- getters'n'setters --- */
    /**
     * gets the grid rows number;
     *
     * @return he number of rows;
     */
    public RasterTokenValue getNRows()
    {
        return
                this.nrows.getValue();
    }
    /**
     * gets the grid columns number;
     *
     * @return the number of columns;
     */
    public RasterTokenValue getNCols()
    {
        return
                this.ncols.getValue();
    }

    /**
     * gets the lower left corner position;
     *
     * @return the x-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getXllCorner()
    {
        return
                RasterHeader.getValue( this.xllcorner );
    }
    /**
     * gets the lower left corner position;
     *
     * @return the y-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getYllCorner()
    {
        return
                RasterHeader.getValue( this.yllcorner );
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
        RasterHeaderToken   backUpXllCorner = this.xllcorner;
        RasterHeaderToken   backUpYllCorner = this.yllcorner;
        RasterHeaderToken   backUpXllCenter = this.xllcenter;
        RasterHeaderToken   backUpYllCenter = this.yllcenter;
        try
        {
            this.xllcorner =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.X_LL_CORNER.getName(),
                                    x
                            );
            this.yllcorner =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.Y_LL_CORNER.getName(),
                                    y
                            );
            this.xllcenter = null;
            this.yllcenter = null;
        }
        catch ( IllegalArgumentException e )
        {
            this.xllcorner = backUpXllCorner;
            this.yllcorner = backUpYllCorner;
            this.xllcenter = backUpXllCenter;
            this.yllcenter = backUpYllCenter;

            throw
                    e;
        }
    }

    /**
     * gets the lower left center position;
     *
     * @return the x-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getXllCenter()
    {
        return
                RasterHeader.getValue( this.xllcenter );
    }
    /**
     * gets the lower left center position;
     *
     * @return the y-ll position; or {@code null if not defined;}
     */
    public RasterTokenValue getYllCenter()
    {
        return
                RasterHeader.getValue( this.yllcenter );
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
        RasterHeaderToken   backUpXllCorner = this.xllcorner;
        RasterHeaderToken   backUpYllCorner = this.yllcorner;
        RasterHeaderToken   backUpXllCenter = this.xllcenter;
        RasterHeaderToken   backUpYllCenter = this.yllcenter;
        try
        {
            this.xllcenter =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.X_LL_CENTER.getName(),
                                    x
                            );
            this.yllcenter =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.Y_LL_CENTER.getName(),
                                    y
                            );
            this.xllcorner = null;
            this.yllcorner = null;
        }
        catch ( IllegalArgumentException e )
        {
            this.xllcorner = backUpXllCorner;
            this.yllcorner = backUpYllCorner;
            this.xllcenter = backUpXllCenter;
            this.yllcenter = backUpYllCenter;

            throw
                    e;
        }
    }

    /**
     * get the cell size;
     *
     * @return the cell size; or {@code null if not defined;}
     */
    public RasterTokenValue getCellSize()
    {
        return
                RasterHeader.getValue( this.cellsize );
    }
    /**
     * set the size of square cell;
     * it replaces previous size, either square or rectangular;
     *
     * @param size    the cell size;
     * @throws IllegalArgumentException if size is not valid;
     */
    public void setCellSize( String size ) throws IllegalArgumentException
    {
        RasterHeaderToken backUpDx         = this.dx;
        RasterHeaderToken backUpDy         = this.dy;
        RasterHeaderToken backUpCellSize   = this.cellsize;
        try
        {
            this.cellsize =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.CELL_SIZE.getName(),
                                    size
                            );
            this.dx = null;
            this.dy = null;
        }
        catch ( IllegalArgumentException e )
        {
            this.dx         = backUpDx;
            this.dy         = backUpDy;
            this.cellsize   = backUpCellSize;

            throw
                    e;
        }
    }

    /**
     * gets the cell width;
     *
     * @return the cell width; or {@code null if not defined;}
     */
    public RasterTokenValue getDX()
    {
        return
                RasterHeader.getValue( this.dx );
    }
    /**
     * gets the cell height;
     *
     * @return the cell height; or {@code null if not defined;}
     */
    public RasterTokenValue getDY()
    {
        return
                RasterHeader.getValue( this.dy );
    }
    /**
     * set the size of rectangular cell;
     * it replaces previous size, either square or rectangular;
     *
     * @param dx      the cell width;
     * @param dy      the cell height;
     * @throws IllegalArgumentException if size is not valid;
     */
    public void setCellSize( String dx, String dy ) throws IllegalArgumentException
    {
        RasterHeaderToken backUpDx         = this.dx;
        RasterHeaderToken backUpDy         = this.dy;
        RasterHeaderToken backUpCellSize   = this.cellsize;
        try
        {
            this.dx         =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.DX.getName(),
                                    dx
                            );
            this.dy         =
                    RasterHeaderToken.parse
                            (
                                    RasterHeaderToken.DY.getName(),
                                    dy
                            );
            this.cellsize   = null;
        }
        catch ( IllegalArgumentException e )
        {
            this.dx         = backUpDx;
            this.dy         = backUpDy;
            this.cellsize   = backUpCellSize;

            throw
                    e;
        }
    }

    /**
     * get the NODATA value;
     *
     * @return the NODATA value; or {@code null if not defined;}
     */
    public RasterTokenValue getNoDataValue()
    {
        return
                RasterHeader.getValue( this.nodata );
    }
    /**
     * replaces the NODATA_VALUE with a new one;
     * as side effect, the original value is kept and used be replace method;
     *
     * @param value    the new NODATA_VALUE;
     */
    public void setNoDataValue( String value )
    {
        if ( ( value == null ) || ( value.length() == 0 ) )
        {
            this.nodata = null;
        }
        else
        {
            if ( ( this.nodataOriginal == null ) && ( this.nodata != null ) )
            {
                this.nodataOriginal = this.nodata.getValue().getValueAsText();
            }
            this.nodata = RasterHeaderToken.parse
                    (
                            RasterHeaderToken.NODATA.getName(),
                            value
                    );
        }
    }

    /**
     * gets the class that perform the old NODATA items
     * with the new one;
     *
     * @return the class for replace;
     */
    public NoDataReplace getNoDataReplacer()
    {
        return
                ( ( this.nodata != null ) && ( this.nodataOriginal != null ) )
                ? new ReplaceValues( this.nodataOriginal, this.nodata.getValue().getValueAsText() )
                : new NothingToReplace()
                ;
    }


    /* --- writers --- */
    /**
     * writes the header;
     *
     * @param writer    where the header will be written;
     * @throws IOException if writing problem arise;
     * @throws GridHeaderInvalidException if the header is not correctly defined;
     */
    public void write( Writer writer )
            throws
                IOException
    {
        /* --- grid columns and rows --- */
        writer.write( this.ncols.write() );
        writer.write( '\n' );
        writer.write( this.nrows.write() );
        writer.write( '\n' );

        /* --- grid position --- */
        if ( this.isCorner() )
        {
            writer.write( this.xllcorner.write() );
            writer.write( '\n' );
            writer.write( this.yllcorner.write() );
            writer.write( '\n' );
        }
        else if ( this.isCenter() )
        {
            writer.write( this.xllcenter.write() );
            writer.write( '\n' );
            writer.write( this.yllcenter.write() );
            writer.write( '\n' );
        }
        else
        {
            throw
                    new GridHeaderInvalidException( "Gird position undefined." );
        }

        /* --- cell size --- */
        if ( this.cellsize != null )
        {
            writer.write( this.cellsize.write() );
            writer.write( '\n' );
        }
        else if ( this.isCellRectangular() )
        {
            writer.write( this.dx.write() );
            writer.write( '\n' );
            writer.write( this.dy.write() );
            writer.write( '\n' );
        }
        else
        {
            throw
                    new GridHeaderInvalidException( "Cell size undefined." );
        }

        /* --- not data value (optional) --- */
        if ( this.nodata != null )
        {
            writer.write( this.nodata.write() );
            writer.write( '\n' );
        }
    }

    /* --- internal methods --- */
    /**
     * checks if an origin is full defined;
     * where origin is either hhe bottom-left corner or cell center;
     *
     * @return true an origin is defined; false, otherwise;
     */
    private boolean isOriginDefined()
    {
        return
                (
                        ( this.isCorner() )
                        ^
                        ( this.isCenter() )
                )
                ;
    }

    /**
     * cecks if the cell size is defined;
     * where the latter is either the cell size or dx/dy pair;
     *
     * @return true, if the cell size is defined; false, otherwise;
     */
    private boolean isCellSizeDefined()
    {
        return
                (
                        ( this.isCellSquare() )
                        ^
                        ( this.isCellRectangular() )
                )
                ;
    }

    private static RasterTokenValue getValue( RasterHeaderToken token )
    {
        return
                ( token != null ) ? token.getValue() : null
                ;
    }


    /* --- internal classes --- */
    public interface NoDataReplace
    {
        String replace( String data );
    }
    public class NothingToReplace implements NoDataReplace
    {
        @Override
        public String replace( String data )
        {
            return
                    data;
        }
    }
    public class ReplaceValues implements NoDataReplace
    {
        /* --- properties --- */
        private String old;
        private String replace;

        /* --- constructor --- */
        public ReplaceValues( String old, String replace )
        {
            this.old        = old;
            this.replace    = replace;
        }

        /* --- replacing method --- */
        @Override
        public String replace( String data )
        {
            return
                    this.old.equals( data )
                    ? this.replace
                    : data
                    ;
        }
    }

}
