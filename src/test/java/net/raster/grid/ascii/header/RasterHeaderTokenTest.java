package net.raster.grid.ascii.header;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;



/**
 * Test the correct token parsing;
 *
 * Created by MASCHERPA on 26/11/2020.
 */
public class RasterHeaderTokenTest
{
    @Rule
    public ExpectedException    exception = ExpectedException.none();

    @Test
    public void testParse() throws Exception
    {
        RasterHeaderToken   token;
        String              value;
        RasterHeaderToken   parsed;

        token   = RasterHeaderToken.N_COLLS;
        value   = "2";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );
        assertEquals( "Expected token >" + token.getName() + "< but found: >" + parsed.name() + '<', token, parsed );
        assertEquals( parsed.getValue().getValueAsText(), value );
        assertEquals( parsed.getValue().getValueAsNumber(), Integer.valueOf( value ) );

        token   = RasterHeaderToken.Y_LL_CENTER;
        value   = "-23.8989";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );
        assertEquals( "Expected token >" + token.getName() + "< but found: >" + parsed.name() + '<', token, parsed );
        assertEquals( parsed.getValue().getValueAsText(), value );
        assertEquals( parsed.getValue().getValueAsNumber(), Double.valueOf( value ) );

        token   = RasterHeaderToken.CELL_SIZE;
        value   = "-23.8989";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName().toUpperCase(),
                        value
                );
        assertEquals( "Expected token >" + token.getName() + "< but found: >" + parsed.name() + '<', token, parsed );
        assertEquals( parsed.getValue().getValueAsText(), value );
        assertEquals( parsed.getValue().getValueAsNumber(), Double.valueOf( value ) );

        token   = RasterHeaderToken.NODATA;
        value   = "-23.8989";
        parsed  = RasterHeaderToken.parse
                (
                        "nodata_VALUE",
                        value
                );
        assertEquals( "Expected token >" + token.getName() + "< but found: >" + parsed.name() + '<', token, parsed );
        assertEquals( parsed.getValue().getValueAsText(), value );
        assertEquals( parsed.getValue().getValueAsNumber(), Double.valueOf( value ) );

        this.exception.expect( NumberFormatException.class );
        token   = RasterHeaderToken.N_COLLS;
        value   = "-2.56";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );

        this.exception.expect( IllegalArgumentException.class );
        token   = RasterHeaderToken.Y_LL_CENTER;
        value   = null;
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );

        this.exception.expect( IllegalArgumentException.class );
        token   = RasterHeaderToken.Y_LL_CENTER;
        value   = "abc";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );

        this.exception.expect( IllegalArgumentException.class );
        token   = null;
        value   = null;
        parsed  = RasterHeaderToken.parse
                (
                        null,
                        value
                );

        this.exception.expect( IllegalArgumentException.class );
        token   = null;
        value   = null;
        parsed  = RasterHeaderToken.parse
                (
                        "",
                        value
                );

        this.exception.expect( IllegalArgumentException.class );
        token   = null;
        value   = null;
        parsed  = RasterHeaderToken.parse
                (
                        "not-exists",
                        value
                );
    }

    @Test
    public void testIsToken() throws Exception
    {
        String  name;
        boolean valid;

        name    = RasterHeaderToken.Y_LL_CENTER.getName();
        valid   = RasterHeaderToken.isToken( name );
        assertTrue( "Token name: >" + name + "< is valid", valid );

        name    = RasterHeaderToken.DX.getName();
        valid   = RasterHeaderToken.isToken( name );
        assertTrue( "Token name: >" + name + "< is valid", valid );

        name    = "not-existing";
        valid   = RasterHeaderToken.isToken( name );
        assertFalse( "Token name: >" + name + "< is NOT valid", valid );

        this.exception.expect( IllegalArgumentException.class );
        name    = "";
        valid   = RasterHeaderToken.isToken( name );
        assertFalse( "Token name: >" + name + "< is NOT valid", valid );

        this.exception.expect( IllegalArgumentException.class );
        name    = null;
        valid   = RasterHeaderToken.isToken( name );
        assertFalse( "Token name: >" + name + "< is NOT valid", valid );
    }

    @Test
    public void testWrite() throws Exception
    {
        RasterHeaderToken   token;
        String              value;
        RasterHeaderToken   parsed;
        String              wrote;

        token   = RasterHeaderToken.N_COLLS;
        value   = "2";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );
        wrote   = parsed.write();
        assertEquals( token.getName() + "         " + value, wrote );

        token   = RasterHeaderToken.NODATA;
        value   = "-9999";
        parsed  = RasterHeaderToken.parse
                (
                        token.getName(),
                        value
                );
        wrote   = parsed.write();
        assertEquals( token.getName() + "  " + value, wrote );
    }

}
