package net.raster.grid.ascii;



/**
 * Methods of this interface will be called while writing the grid;
 *
 * Created by MASCHERPA on 10/12/2020.
 */
public interface GridScanListener
{
    /**
     * called before the grid is written;
     */
    void gridBegin();

    /**
     * called just after the grid has been written;
     */
    void gridEnd();

    /**
     * called at begin of each row;
     */
    void rowBegin();

    /**
     * called at end of each row;
     */
    void rowEnd();

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
    String cell( String value );

}
