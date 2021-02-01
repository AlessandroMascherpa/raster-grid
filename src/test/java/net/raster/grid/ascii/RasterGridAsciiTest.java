package net.raster.grid.ascii;

import net.raster.grid.ascii.header.GridHeaderInvalidException;
import net.raster.grid.ascii.header.value.RasterTokenValue;
import net.raster.grid.ascii.header.value.TokenValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;


/**
 * Test of library interface;
 *
 * Created by MASCHERPA on 26/11/2020.
 */
public class RasterGridAsciiTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void GivenGridWhenParsedThenEqual() throws Exception
    {
        this.exception = ExpectedException.none();

        this.compareIdenticalGrids( "/grids/idem/basic.asc" );
        this.compareIdenticalGrids( "/grids/idem/basic-nodata.asc" );

        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces.asc",        "/grids/valid/expected/spaces.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc", "/grids/valid/expected/spaces-nodata.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/dxdy.asc",          "/grids/valid/expected/dxdy.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/cellsize.asc",      "/grids/valid/expected/cellsize.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/kernel.asc",        "/grids/valid/expected/kernel.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/conc.asc",          "/grids/valid/expected/conc.asc" );
    }
    private void compareIdenticalGrids( String asc ) throws IOException
    {
        this.compareSourceGridExpectedGrid( asc, asc );
    }
    private void compareSourceGridExpectedGrid( String source, String expected ) throws IOException
    {
        this.compareSourceGridExpectedGrid( source, false, null, expected );
    }

    @Test
    public void GivenValidGridWhenForceCellSizeThenValidGrid() throws Exception
    {
        this.exception = ExpectedException.none();

        this.compareSourceGridExpectedGrid( "/grids/valid/source/cellsize.asc", true, null, "/grids/valid/expected/cellsize.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/dxdy.asc",     true, null, "/grids/valid/expected/cellsize-forced-1.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/dxdy-2.asc",   true, null, "/grids/valid/expected/cellsize.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/dxdy-3.asc",   true, null, "/grids/valid/expected/cellsize-forced-2.asc" );
    }

    @Test
    public void GivenValidGridWhenChangeNoDataThenValidGrid() throws Exception
    {
        this.exception = ExpectedException.none();

        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces.asc",           false, "0", "/grids/valid/expected/spaces-nodata-0-9999.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc",    false, "0", "/grids/valid/expected/spaces-nodata-0.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces.asc",           true,  "0", "/grids/valid/expected/spaces-nodata-0-9999.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc",    true,  "0", "/grids/valid/expected/spaces-nodata-0.asc" );
    }
    private void compareSourceGridExpectedGrid( String source, boolean force, String nodata, String expected ) throws IOException
    {
        this.compareSourceGridExpectedGrid( source, force, nodata, null, expected );
    }
    @Test
    public void GivenValidGridWhenAddCellsThenValidGrid() throws Exception
    {
        this.exception = ExpectedException.none();

        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc",    false, "0", 10D, "/grids/valid/expected/spaces-nodata-10.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc",    true,  "0", 10D, "/grids/valid/expected/spaces-nodata-10.asc" );
    }
    private void compareSourceGridExpectedGrid( String source, boolean force, String nodata, Double value, String expected ) throws IOException
    {
        Reader          reader  = this.getReader( source );
        RasterGridAscii raster  = RasterGridAscii.parse( reader );
        StringWriter    sw      = new StringWriter();
        BufferedWriter  writer  = new BufferedWriter( sw );

        assertNotNull( raster.getNCols() );
        assertNotNull( raster.getNRows() );

        if ( force )
        {
            raster.setForceCellSquare();

            assertTrue( raster.isCellSquare() );
            assertFalse( raster.isCellRectangular() );
            assertNotNull( raster.getCellSize() );
            assertNull( raster.getDX() );
            assertNull( raster.getDY() );
        }
        if ( nodata != null )
        {
            raster.setNoDataValue( nodata );

            assertEquals( nodata, raster.getNoDataValue().getValueAsText() );
        }

        // --- attempt to inject an unknown value
        RasterTokenValue noDataValue = raster.getNoDataValue();
        if ( noDataValue != null )
        {
            noDataValue = TokenValue.parseDouble( "11" );
        }

        raster.write
                (
                        writer,
                        ( value != null ) ? new GridScanListenerTest( value ) : null
                );

        assertGrid( expected, sw.toString() );

        writer.close();
        sw.close();
        reader.close();
    }

    @Test
    public void GivenValidGridWhenChangeHeaderThenValidGrid() throws Exception
    {
        this.exception = ExpectedException.none();

        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc", null,   null,  "20.0", "20.0", null,  "50.0", "49.8", "/grids/valid/expected/spaces-nodata-center.asc" );
        this.compareSourceGridExpectedGrid( "/grids/valid/source/spaces-nodata.asc", "20.0", "20.0", null,  null,   "52.0", null,  null,   "/grids/valid/expected/spaces-nodata-corner.asc" );
    }
    private void compareSourceGridExpectedGrid
            (
                    String source,
                    String xCorner,
                    String yCorner,
                    String xCenter,
                    String yCenter,
                    String cellsize,
                    String dx,
                    String dy,
                    String expected
            )
            throws
                IOException
    {
        Reader          reader  = this.getReader( source );
        RasterGridAscii raster  = RasterGridAscii.parse( reader );
        StringWriter    sw      = new StringWriter();
        BufferedWriter  writer  = new BufferedWriter( sw );

        assertNotNull( raster.getNCols() );
        assertNotNull( raster.getNRows() );

        if ( ( xCorner != null ) && ( yCorner != null ) )
        {
            raster.setCorner( xCorner, yCorner );

            assertTrue( raster.isCorner() );
            assertFalse( raster.isCenter() );
            assertEquals( xCorner, raster.getXllCorner().getValueAsText() );
            assertEquals( yCorner, raster.getYllCorner().getValueAsText() );
            assertNull( raster.getXllCenter() );
            assertNull( raster.getYllCenter() );
        }
        if ( ( xCenter != null ) && ( yCenter != null ) )
        {
            raster.setCenter( xCenter, yCenter );

            assertTrue( raster.isCenter() );
            assertFalse( raster.isCorner() );
            assertEquals( xCenter, raster.getXllCenter().getValueAsText() );
            assertEquals( yCenter, raster.getYllCenter().getValueAsText() );
            assertNull( raster.getXllCorner() );
            assertNull( raster.getYllCorner() );
        }
        if ( cellsize != null )
        {
            raster.setCellSize( cellsize );

            assertTrue( raster.isCellSquare() );
            assertFalse( raster.isCellRectangular() );
            assertEquals( cellsize, raster.getCellSize().getValueAsText() );
            assertNull( raster.getDX() );
            assertNull( raster.getDY() );
        }
        if ( ( dx != null ) && ( dy != null ) )
        {
            raster.setCellSize( dx, dy );

            assertTrue( raster.isCellRectangular() );
            assertFalse( raster.isCellSquare() );
            assertEquals( dx, raster.getDX().getValueAsText() );
            assertEquals( dy, raster.getDY().getValueAsText() );
            assertNull( raster.getCellSize() );
        }
        raster.write( writer );

        assertGrid( expected, sw.toString() );

        writer.close();
        sw.close();
        reader.close();
    }


    @Test
    public void GivenWrongGridWhenParsingAndWritingThenException() throws Exception
    {
        this.exception.expect( GridInvalidException.class );

        RasterGridAscii raster;
        StringWriter    sw      = new StringWriter();
        BufferedWriter  writer  = new BufferedWriter( sw );

        raster = RasterGridAscii.parse( this.getReader( "/grids/wrong/empty-line.asc" ) );
        raster.write( writer );

        raster = RasterGridAscii.parse( this.getReader( "/grids/wrong/size-wrong-col.asc" ) );
        raster.write( writer );

        raster = RasterGridAscii.parse( this.getReader( "/grids/wrong/size-wrong-row.asc" ) );
        raster.write( writer );

        raster = RasterGridAscii.parse( this.getReader( "/grids/wrong/size-too-col.asc" ) );
        raster.write( writer );

        raster = RasterGridAscii.parse( this.getReader( "/grids/wrong/size-too-row.asc" ) );
        raster.write( writer );
    }
    @Test
    public void GivenWrongGridWhenParsingThenException() throws Exception
    {
        this.exception.expect( GridHeaderInvalidException.class );

        RasterGridAscii.parse( this.getReader( "/grids/wrong/no-cell-size.asc" ) );
        RasterGridAscii.parse( this.getReader( "/grids/wrong/no-token-value.asc" ) );
        RasterGridAscii.parse( this.getReader( "/grids/wrong/wrong-token.asc" ) );
    }

    /* --- internal methods --- */
    private Reader  getReader( String resource )
    {
        return
                new InputStreamReader
                        (
                                this.getClass().getResourceAsStream( resource ),
                                StandardCharsets.UTF_8
                        )
                ;
    }

    private void assertGrid( String source, String dest ) throws IOException
    {
        int             line    = 1;
        BufferedReader  rs      = new BufferedReader( this.getReader( source ) );
        BufferedReader  rd      = new BufferedReader( new StringReader( dest ) );

        while ( rs.ready() && rd.ready() )
        {
            String expect   = rs.readLine();
            String value    = rd.readLine();

            assertEquals( "At line: " + line + " - expected >" + expect + "< but found >" + value + '<', expect, value );

            line ++ ;
        }

        rd.close();
        rs.close();
    }

}
