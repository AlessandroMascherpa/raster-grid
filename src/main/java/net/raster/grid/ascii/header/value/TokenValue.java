package net.raster.grid.ascii.header.value;



public class TokenValue implements RasterTokenValue
{
    /* --- properties --- */
    /**
     * the token value as text string;
     */
    private String          text;
    /**
     * the value not as string;
     */
    private Number          value;

    /* --- constructors --- */
    /**
     * handles the token value as integer;
     *
     * @param value    the value to handle as text string;
     * @return the token value;
     */
    public static RasterTokenValue parseInteger( String value )
    {
        return
                new TokenValue
                        (
                                value,
                                Integer.parseInt( value )
                        )
                ;
    }
    /**
     * handles the token value as double;
     *
     * @param value    the value to handle as text string;
     * @return the token value;
     */
    public static RasterTokenValue parseDouble( String value )
    {
        return
                new TokenValue
                        (
                                value,
                                Double.parseDouble( value )
                        )
                ;
    }
    private TokenValue( String valueString, Number valueNumber )
    {
        this.text   = valueString;
        this.value  = valueNumber;
    }

    /**
     * clones a given token value;
     *
     * @param that    the token value to clone;
     */
    public TokenValue( RasterTokenValue that )
    {
        this.text   = that.getValueAsText();
        this.value  = that.getValueAsNumber();
    }

    /* --- interface methods --- */
    @Override
    public Number getValueAsNumber()
    {
        return this.value;
    }

    @Override
    public String getValueAsText()
    {
        return this.text;
    }
}
