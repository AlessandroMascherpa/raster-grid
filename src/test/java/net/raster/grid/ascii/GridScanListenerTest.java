package net.raster.grid.ascii;


/**
 * Test the grid listener;
 *
 * Created by MASCHERPA on 11/12/2020.
 */
public class GridScanListenerTest implements GridScanListener
{
    /* --- properties --- */
    private final Double    adder;

    /* --- constructor --- */
    public GridScanListenerTest( Double value )
    {
        this.adder = value;
    }

    /* --- interface methods --- */
    @Override
    public void gridBegin()
    {
        /* do nothing */
    }

    @Override
    public void gridEnd()
    {
        /* do nothing */
    }

    @Override
    public void rowBegin()
    {
        /* do nothing */
    }

    @Override
    public void rowEnd()
    {
        /* do nothing */
    }

    @Override
    public String cell( String value )
    {
        return
                String.valueOf
                        (
                                this.adder + Double.valueOf( value )
                        );
    }
}
