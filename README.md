# ASCII Grid handler

## Description

Library to handle [grid files in ASCII format](https://en.wikipedia.org/wiki/Esri_grid 'Wikipedia file format definition').

Library features:
 * grid header parsed by finite state automaton
 * read and update header fields
 * change the NODATA_value field will update the NODATA values in grid
 * force square cell
 * handle each single cell at grid writing time via listener class
 
## Installation
 
To use Maven, add the following lines to your `pom.xml` file:
 
   ```maven
   <project>
     <dependencies>
       <dependency>
         <groupId>net.raster.grid</groupId>
         <artifactId>grid-ascii</artifactId>
         <version>1.0.8</version>
       </dependency>
     </dependencies>
   </project>
   ```

## Usage

In the following example, we change the NODATA_value both in the header and in grid:

   ```Java
   public void setNoDatValue
            (
                Reader  reader,
                String  nodata,
                Writer  writer
            )
            throws
                IOException,
                IllegalArgumentException
   {
        RasterGridAscii raster  = RasterGridAscii.parse( reader );
        raster.setNoDataValue( nodata )
        raster.write( writer );
   }
   ```
   
In the following example we can see how to manipulate each single grid value by sum a constant:

   ```Java
   public void addConstant
            (
                Reader  reader,
                Double  k,
                Writer  writer
            )
            throws
                IOException,
                IllegalArgumentException
   {
        RasterGridAscii raster  = RasterGridAscii.parse( reader );
        raster.write
                (
                        writer,
                        new GridScanListener()
                        {
                            // ... others methods ...
                            @Override
                            public String cell( String value )
                            {
                                return
                                        String.valueOf
                                                (
                                                        k + Double.valueOf( value )
                                                );
                            }
                        } 
                );
   }
   ```


## Building library

In the library root directory:

   ```sh
   mvn  clean  install
   ```
