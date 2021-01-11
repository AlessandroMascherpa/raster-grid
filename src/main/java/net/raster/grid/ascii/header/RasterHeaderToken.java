package net.raster.grid.ascii.header;


/**
 * Tokens used in Grid ASCII header.
 *
 * Created by MASCHERPA on 20/02/2020.
 */
public enum RasterHeaderToken
{
    N_COLLS( "ncols" )
            {
                @Override
                protected Integer parseValue( String text )
                {
                    return
                            Integer.parseInt( text );
                }
            },
    N_ROWNS( "nrows" )
            {
                @Override
                protected Integer parseValue( String text )
                {
                    return
                            Integer.parseInt( text );
                }
            },
    X_LL_CORNER( "xllcorner" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    Y_LL_CORNER( "yllcorner" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    X_LL_CENTER( "xllcenter" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    Y_LL_CENTER( "yllcenter" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    CELL_SIZE( "cellsize" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    DX( "dx" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    DY( "dy" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    NODATA( "NODATA_value" )
            {
                @Override
                protected Double parseValue( String text )
                {
                    return
                            Double.parseDouble( text );
                }
            },
    ;

    /* --- properties --- */
    /**
     * the real token name;
     */
    private final String    name;
    /**
     * the token name in lower case used only to parse incoming tokens;
     */
    private final String    lowerName;
    /**
     * the token value as text string;
     */
    private String          text;
    /**
     * the value not as string;
     */
    private Number          value;

    /**
     * used to check input numbers;
     */
    private static String   IS_NUMBER  = "^[+-]?\\d+(\\.\\d*)?$";

    /* --- constructors --- */
    RasterHeaderToken( String key )
    {
        this.name       = key;
        this.lowerName  = key.toLowerCase();
    }

    /**
     * Parses a pair name-value to get the header token with its value;
     *
     * @param name     the token name; it must be a valid text string;
     * @param value    the token value, it might be an empty/null string;
     * @return the parsed token or {@code null} if the token was inknown;
     */
    public static RasterHeaderToken parse( String name, String value ) throws IllegalArgumentException
    {
        String              tk      = assertTokenName( name );
        RasterHeaderToken   parsed  = null;
        for ( RasterHeaderToken token : RasterHeaderToken.values() )
        {
            if ( token.lowerName.equals( tk ) )
            {
                parsed = token;
                parsed.setValueAsText( value );
                break;
            }
        }
        return
                parsed;
    }

    /* --- checkers --- */
    /**
     * checks whether a given text string is a valid header token name;
     *
     * @param name    the text to check;
     * @return true, the {@code name} is valid token name; false, otherwise;
     */
    public static boolean isToken( String name ) throws IllegalArgumentException
    {
        String  tk      = assertTokenName( name );
        boolean valid   = false;
        for ( RasterHeaderToken token : RasterHeaderToken.values() )
        {
            valid = token.getName().equals( tk );
            if ( valid )
            {
                break;
            }
        }
        return
                valid;
    }

    /* --- setters'n'getters --- */
    /**
     * gets the token name;
     *
     * @return token name;
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * defines the value linked to the token;
     *
     * @param value    token value as text string;
     */
    public void setValueAsText( String value ) throws IllegalArgumentException
    {
        if ( ( value != null ) && value.matches( IS_NUMBER ) )
        {
            this.text  = value;
            this.value = this.parseValue( value );
        }
        else
        {
            throw new IllegalArgumentException( "Token value is not a valid number format." );
        }
    }
    /**
     * get the token value as text string;
     *
     * @return the token value;
     */
    public String getValueAsText()
    {
        return this.text;
    }
    /**
     * get the token value;
     *
     * @return the token value;
     */
    public Number getValue()
    {
        return value;
    }

    protected abstract Number parseValue( String text );

    /* --- writers --- */
    /**
     * writes the token and its value in formatted forma;
     *
     * @return the token name and its value;
     */
    public String write()
    {
        return
                String.format( "%-13s %s", this.name, this.text )
                ;
    }

    /* --- internal methods --- */
    private static String assertTokenName( String name )
    {
        if ( ( name == null ) || "".equals( name ) )
        {
            throw new IllegalArgumentException( "Token name must be defined." );
        }
        return
                name.toLowerCase();
    }

}
