package com.nate.chatham;

import com.badlogic.gdx.math.MathUtils;


///////////////////////////////////////////////////////////////////////////////////////
//                                                                                   //
//  So we need a parental DNA object and a child DNA object which is a mutate form   //
//  of the parental. The DNA object is basically an array of, at most, 50 polygons   //
//  which we will call "genes." They will have the following characteristicts:       //
//  - XY location of the three verticies of the polygon: float Array[ 6 ]            //
//  - RGBA values for the color of the polygon: float Array[ 4 ]                     //
//  - Mutate function which mutates a single random value                            //
//                                                                                   //
///////////////////////////////////////////////////////////////////////////////////////

public class Gene {

    // Generates the arrays for both the verticies and color of the polygon
    private float[] verticies = new float[ 6 ];
    private float[] geneColor = new float[ 4 ];
    private final int height;
    private final int width;

    public Gene() {

        //Dimensions of the source image
        height = 768;
        width = 521;

        // Fills the vertex array with random ints within the bounds of the screen. Even indexes of the array are the x
        // coordinates and odd indexes are the y coordinates.
        for( int i = 0; i < verticies.length; i += 1 ) {
            if( i % 2 == 0 ) {
                verticies[ i ] = MathUtils.round( MathUtils.random( width ) );
            } else {
                verticies[ i ] = MathUtils.round( MathUtils.random( height ) + 112 );
            }
        }

        // Fills the color array with a random float between 0 and 1. The alpha variable is always initiated as 0 though.
        for( int i = 0; i < geneColor.length; i += 1 ) {
            if( i != 3  ) {
                geneColor[i] = MathUtils.random();
            } else {
                geneColor[ 3 ] = 0;
            }
        }
    }

    // Getter functions
    public float[] getVerticies() {
        return verticies;
    }
    public float[] getGeneColor() {
        return geneColor;
    }
    public float getGeneRColor() {
        return geneColor[ 0 ];
    }
    public float getGeneGColor() {
        return geneColor[ 1 ];
    }
    public float getGeneBColor() {
        return geneColor[ 2 ];
    }
    public float getGeneAColor() {
        return geneColor[ 3 ];
    }

    // Setter functions. clone() is required because we don't want this objects verticies array to references the same
    // area of memory as the array coming in.
    public void setVerticies( float[] verticies ) {
        this.verticies = verticies.clone();
    }
    public void setGeneColor( float[] color ) {
        geneColor = color.clone();
    }

    // Mutator functions
    public void mutateVerticies( int index ) {
        if( index % 2 == 0 ) {
            verticies[ index ] = MathUtils.round( MathUtils.random( width ) );
        } else {
            verticies[ index ] = MathUtils.round( MathUtils.random( height ) ) + 112;
        }
    }
    public void mutateGeneColor( int index ) {
        geneColor[ index ] = MathUtils.random();
    }

    public void setFirstGene() {
        mutateGeneColor( 3 );
    }
}
