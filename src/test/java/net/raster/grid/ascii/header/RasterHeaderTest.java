package net.raster.grid.ascii.header;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;


/**
 * Test of header parser class;
 *
 * Created by MASCHERPA on 26/11/2020.
 */
public class RasterHeaderTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String  N_COLLS        = "53";
    private static final String  N_ROWNS        = "28";
    private static final String  X_LL_CORNER    = "-1.263438463211";
    private static final String  Y_LL_CORNER    = "51.749325071480";
    private static final String  X_LL_CENTER    = "2.0515131165158";
    private static final String  Y_LL_CENTER    = "45.564489413188";
    private static final String  CELL_SIZE      = "20.6";
    private static final String  DX             = "0.000289614900";
    private static final String  DY             = "0.000179755100";
    private static final String  NODATA         = "-9999";


    @Test
    public void testStore() throws Exception
    {
        RasterHeaderToken n_cools   = RasterHeaderToken.parse( RasterHeaderToken.N_COLLS.getName(),     N_COLLS );
        RasterHeaderToken n_rows    = RasterHeaderToken.parse( RasterHeaderToken.N_ROWNS.getName(),     N_ROWNS );
        RasterHeaderToken x_corner  = RasterHeaderToken.parse( RasterHeaderToken.X_LL_CORNER.getName(), X_LL_CORNER );
        RasterHeaderToken y_corner  = RasterHeaderToken.parse( RasterHeaderToken.Y_LL_CORNER.getName(), Y_LL_CORNER );
        RasterHeaderToken x_center  = RasterHeaderToken.parse( RasterHeaderToken.X_LL_CENTER.getName(), X_LL_CENTER );
        RasterHeaderToken y_center  = RasterHeaderToken.parse( RasterHeaderToken.Y_LL_CENTER.getName(), Y_LL_CENTER );
        RasterHeaderToken cellsize  = RasterHeaderToken.parse( RasterHeaderToken.CELL_SIZE.getName(),   CELL_SIZE );
        RasterHeaderToken dx        = RasterHeaderToken.parse( RasterHeaderToken.DX.getName(),          DX );
        RasterHeaderToken dy        = RasterHeaderToken.parse( RasterHeaderToken.DY.getName(),          DY );
        RasterHeaderToken nodata    = RasterHeaderToken.parse( RasterHeaderToken.NODATA.getName(),      NODATA );


        RasterHeader    header;

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.isHeaderWellFormed();

        assertTrue( header.isCorner() );
        assertTrue( header.isCellSquare() );
        assertFalse( header.isCenter() );
        assertFalse( header.isCellRectangular() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CORNER,  header.getXllCorner().getValueAsText() );
        assertEquals( Y_LL_CORNER,  header.getYllCorner().getValueAsText() );
        assertEquals( CELL_SIZE,    header.getCellSize().getValueAsText() );
        assertNull( header.getXllCenter() );
        assertNull( header.getYllCenter() );
        assertNull( header.getDX() );
        assertNull( header.getDY() );
        assertNull( header.getNoDataValue() );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_center );
        header.store( y_center );
        header.store( cellsize );
        header.isHeaderWellFormed();

        assertTrue( header.isCenter() );
        assertTrue( header.isCellSquare() );
        assertFalse( header.isCorner() );
        assertFalse( header.isCellRectangular() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CENTER, header.getXllCenter().getValueAsText() );
        assertEquals( Y_LL_CENTER,  header.getYllCenter().getValueAsText() );
        assertEquals( CELL_SIZE,    header.getCellSize().getValueAsText() );
        assertNull( header.getXllCorner() );
        assertNull( header.getYllCorner() );
        assertNull( header.getDX() );
        assertNull( header.getDY() );
        assertNull( header.getNoDataValue() );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.store( nodata   );
        header.isHeaderWellFormed();

        assertTrue( header.isCorner() );
        assertTrue( header.isCellSquare() );
        assertFalse( header.isCenter() );
        assertFalse( header.isCellRectangular() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CORNER,  header.getXllCorner().getValueAsText() );
        assertEquals( Y_LL_CORNER,  header.getYllCorner().getValueAsText() );
        assertEquals( CELL_SIZE,    header.getCellSize().getValueAsText() );
        assertEquals( NODATA,       header.getNoDataValue().getValueAsText() );
        assertNull( header.getXllCenter() );
        assertNull( header.getYllCenter() );
        assertNull( header.getDX() );
        assertNull( header.getDY() );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( dx       );
        header.store( dy       );
        header.isHeaderWellFormed();

        assertTrue( header.isCorner() );
        assertTrue( header.isCellRectangular() );
        assertFalse( header.isCenter() );
        assertFalse( header.isCellSquare() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CORNER,  header.getXllCorner().getValueAsText() );
        assertEquals( Y_LL_CORNER,  header.getYllCorner().getValueAsText() );
        assertEquals( DX,           header.getDX().getValueAsText() );
        assertEquals( DY,           header.getDY().getValueAsText() );
        assertNull( header.getXllCenter() );
        assertNull( header.getYllCenter() );
        assertNull( header.getCellSize() );
        assertNull( header.getNoDataValue() );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( dx       );
        header.store( dy       );
        header.store( nodata   );
        header.isHeaderWellFormed();

        assertTrue( header.isCorner() );
        assertTrue( header.isCellRectangular() );
        assertFalse( header.isCenter() );
        assertFalse( header.isCellSquare() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CORNER,  header.getXllCorner().getValueAsText() );
        assertEquals( Y_LL_CORNER,  header.getYllCorner().getValueAsText() );
        assertEquals( DX,           header.getDX().getValueAsText() );
        assertEquals( DY,           header.getDY().getValueAsText() );
        assertEquals( NODATA,       header.getNoDataValue().getValueAsText() );
        assertNull( header.getXllCenter() );
        assertNull( header.getYllCenter() );
        assertNull( header.getCellSize() );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_center );
        header.store( y_center );
        header.store( dx       );
        header.store( dy       );
        header.isHeaderWellFormed();

        assertTrue( header.isCenter() );
        assertTrue( header.isCellRectangular() );
        assertFalse( header.isCorner() );
        assertFalse( header.isCellSquare() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CENTER,  header.getXllCenter().getValueAsText() );
        assertEquals( Y_LL_CENTER,  header.getYllCenter().getValueAsText() );
        assertEquals( DX,           header.getDX().getValueAsText() );
        assertEquals( DY,           header.getDY().getValueAsText() );
        assertNull( header.getYllCorner() );
        assertNull( header.getYllCorner() );
        assertNull( header.getCellSize() );
        assertNull( header.getNoDataValue() );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_center );
        header.store( y_center );
        header.store( dx       );
        header.store( dy       );
        header.store( nodata   );
        header.isHeaderWellFormed();

        assertTrue( header.isCenter() );
        assertTrue( header.isCellRectangular() );
        assertFalse( header.isCorner() );
        assertFalse( header.isCellSquare() );
        assertEquals( N_COLLS,      header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,      header.getNRows().getValueAsText() );
        assertEquals( X_LL_CENTER,  header.getXllCenter().getValueAsText() );
        assertEquals( Y_LL_CENTER,  header.getYllCenter().getValueAsText() );
        assertEquals( DX,           header.getDX().getValueAsText() );
        assertEquals( DY,           header.getDY().getValueAsText() );
        assertEquals( NODATA,       header.getNoDataValue().getValueAsText() );
        assertNull( header.getYllCorner() );
        assertNull( header.getYllCorner() );
        assertNull( header.getCellSize() );


        this.exception.expect( GridHeaderInvalidException.class );
        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( cellsize );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_center );
        header.store( cellsize );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.store( dx       );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( dx       );
        header.store( cellsize );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( dy       );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( n_rows   );
        header.store( nodata   );
        header.isHeaderWellFormed();

        header  = new RasterHeader();
        header.store( y_corner );
        header.store( cellsize );
        header.isHeaderWellFormed();
    }

    @Test
    public void testCellRectangularForcedSqare() throws Exception
    {
        this.exception = ExpectedException.none();

        RasterHeaderToken n_cools   = RasterHeaderToken.parse( RasterHeaderToken.N_COLLS.getName(),     N_COLLS );
        RasterHeaderToken n_rows    = RasterHeaderToken.parse( RasterHeaderToken.N_ROWNS.getName(),     N_ROWNS );
        RasterHeaderToken x_corner  = RasterHeaderToken.parse( RasterHeaderToken.X_LL_CORNER.getName(), "-1.263438463211" );
        RasterHeaderToken y_corner  = RasterHeaderToken.parse( RasterHeaderToken.Y_LL_CORNER.getName(), "51.749325071480" );
        RasterHeaderToken dx        = RasterHeaderToken.parse( RasterHeaderToken.DX.getName(),          "0.000289614900" );
        RasterHeaderToken dy        = RasterHeaderToken.parse( RasterHeaderToken.DY.getName(),          "0.000179755100" );
        RasterHeaderToken nodata    = RasterHeaderToken.parse( RasterHeaderToken.NODATA.getName(),      NODATA );

        RasterHeader      header;

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( dx       );
        header.store( dy       );
        header.store( nodata   );
        header.isHeaderWellFormed();
        header.forceCellSquare();
        header.isHeaderWellFormed();

        assertTrue( header.isCorner() );
        assertTrue( header.isCellSquare() );
        assertFalse( header.isCenter() );
        assertFalse( header.isCellRectangular() );
        assertEquals( N_COLLS,              header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,              header.getNRows().getValueAsText() );
        assertEquals( "-1.263438463211",    header.getXllCorner().getValueAsText() );
        assertEquals( "51.74624899708",     header.getYllCorner().getValueAsText() );
        assertEquals( "0.000289614900",     header.getCellSize().getValueAsText() );
        assertEquals( NODATA,               header.getNoDataValue().getValueAsText() );
        assertNull( header.getXllCenter() );
        assertNull( header.getYllCenter() );
        assertNull( header.getDX() );
        assertNull( header.getDY() );
    }

    @Test
    public void testCellSquareForcedSquare() throws Exception
    {
        this.exception = ExpectedException.none();

        RasterHeaderToken n_cools   = RasterHeaderToken.parse( RasterHeaderToken.N_COLLS.getName(),     N_COLLS );
        RasterHeaderToken n_rows    = RasterHeaderToken.parse( RasterHeaderToken.N_ROWNS.getName(),     N_ROWNS );
        RasterHeaderToken x_corner  = RasterHeaderToken.parse( RasterHeaderToken.X_LL_CORNER.getName(), "-1.263438463211" );
        RasterHeaderToken y_corner  = RasterHeaderToken.parse( RasterHeaderToken.Y_LL_CORNER.getName(), "51.749325071480" );
        RasterHeaderToken cellsize  = RasterHeaderToken.parse( RasterHeaderToken.CELL_SIZE.getName(),   CELL_SIZE );

        RasterHeader      header;

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.isHeaderWellFormed();
        header.forceCellSquare();
        header.isHeaderWellFormed();

        assertTrue( header.isCorner() );
        assertTrue( header.isCellSquare() );
        assertFalse( header.isCenter() );
        assertFalse( header.isCellRectangular() );
        assertEquals( N_COLLS,              header.getNCols().getValueAsText() );
        assertEquals( N_ROWNS,              header.getNRows().getValueAsText() );
        assertEquals( "-1.263438463211",    header.getXllCorner().getValueAsText() );
        assertEquals( "51.749325071480",    header.getYllCorner().getValueAsText() );
        assertEquals( CELL_SIZE,            header.getCellSize().getValueAsText() );
        assertNull( header.getXllCenter() );
        assertNull( header.getYllCenter() );
        assertNull( header.getDX() );
        assertNull( header.getDY() );
        assertNull( header.getNoDataValue() );
    }

    @Test
    public void testSetFields() throws Exception
    {
        this.exception = ExpectedException.none();

        RasterHeaderToken           n_cools   = RasterHeaderToken.parse( RasterHeaderToken.N_COLLS.getName(),     N_ROWNS );
        RasterHeaderToken           n_rows    = RasterHeaderToken.parse( RasterHeaderToken.N_ROWNS.getName(),     N_COLLS );
        RasterHeaderToken           x_corner  = RasterHeaderToken.parse( RasterHeaderToken.X_LL_CORNER.getName(), X_LL_CORNER );
        RasterHeaderToken           y_corner  = RasterHeaderToken.parse( RasterHeaderToken.Y_LL_CORNER.getName(), X_LL_CORNER );
        RasterHeaderToken           cellsize  = RasterHeaderToken.parse( RasterHeaderToken.CELL_SIZE.getName(),   CELL_SIZE );

        RasterHeader                header;
        RasterHeader.NoDataReplace  replacer;


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.store( RasterHeaderToken.parse( RasterHeaderToken.NODATA.getName(), NODATA ) );
        header.isHeaderWellFormed();
        header.setNoDataValue( null );

        replacer = header.getNoDataReplacer();
        assertEquals( NODATA,   replacer.replace( NODATA ) );
        assertEquals( "0",      replacer.replace( "0" ) );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.isHeaderWellFormed();
        header.setNoDataValue( null );

        replacer = header.getNoDataReplacer();
        assertEquals( NODATA,   replacer.replace( NODATA ) );
        assertEquals( "0",      replacer.replace( "0" ) );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.isHeaderWellFormed();
        header.setNoDataValue( "0" );

        replacer = header.getNoDataReplacer();
        assertEquals( NODATA,   replacer.replace( NODATA ) );
        assertEquals( "0",      replacer.replace( "0" ) );

        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.store( RasterHeaderToken.parse( RasterHeaderToken.NODATA.getName(), NODATA ) );
        header.isHeaderWellFormed();
        header.setNoDataValue( "0" );

        replacer = header.getNoDataReplacer();
        assertEquals( "0",      replacer.replace( NODATA ) );
        assertEquals( "0",      replacer.replace( "0" ) );


        header  = new RasterHeader();
        header.store( n_cools  );
        header.store( n_rows   );
        header.store( x_corner );
        header.store( y_corner );
        header.store( cellsize );
        header.isHeaderWellFormed();

        header.setCenter( X_LL_CENTER, Y_LL_CENTER );
        header.isHeaderWellFormed();
        assertTrue( header.isCenter() );
        assertFalse( header.isCorner() );
        assertEquals( X_LL_CENTER,  header.getXllCenter().getValueAsText() );
        assertEquals( Y_LL_CENTER,  header.getYllCenter().getValueAsText() );
        assertNull( header.getYllCorner() );
        assertNull( header.getYllCorner() );


        header.setCellSize( DX, DY );
        header.isHeaderWellFormed();
        assertTrue( header.isCellRectangular() );
        assertFalse( header.isCellSquare() );
        assertEquals( DX,   header.getDX().getValueAsText() );
        assertEquals( DY,   header.getDY().getValueAsText() );
        assertNull( header.getCellSize() );
    }

    @Test
    public void testSetFieldsWithFailures() throws Exception
    {
        this.exception.expect( IllegalArgumentException.class );

        RasterHeader                header;

        header  = new RasterHeader();

        header.setCenter( X_LL_CENTER,  null );
        header.setCenter( null,         Y_LL_CENTER );

        header.setCorner( X_LL_CORNER,  null );
        header.setCorner( null,         Y_LL_CORNER );

        header.setCellSize( DX,         null );
        header.setCellSize( null,       DY );
    }

}
