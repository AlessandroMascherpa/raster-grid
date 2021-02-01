package net.raster.grid.ascii.header.value;



/**
 * The classes inlementing this interface has to store the token value,
 * both as text string and its numerical value;
 *
 * Created by MASCHERPA on 26/01/2021.
 */
public interface RasterTokenValue
{
    /**
     * gets the token value as numerical;
     *
     * @return the value in its proper number format;
     */
    Number getValueAsNumber();

    /**
     * gets the token value as text string;
     *
     * @return the value as text string;
     */
    String getValueAsText();

}
