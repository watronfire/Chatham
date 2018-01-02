package com.nate.chatham;

import com.badlogic.gdx.math.MathUtils;

///////////////////////////////////////////////////////////////////////////////////////
//                                                                                   //
//  So the mutate manager basically decides which gene and which aspect of the gene  //
//  to mutate. It takes a Gene array. Pretty simple I think.                         //
//                                                                                   //
///////////////////////////////////////////////////////////////////////////////////////

class MutationManager {
    private int geneToMutate;
    private int mutationToPerform;


    public void mutateStrand( Gene[] DNA ) {
        geneToMutate = MathUtils.round( MathUtils.random( DNA.length - 1 ) );
        mutationToPerform = MathUtils.round( MathUtils.random( 10 ) );

        switch( mutationToPerform ) {
            case 0:
                DNA[ geneToMutate ].mutateVerticies( 0 );
                break;
            case 1:
                DNA[ geneToMutate ].mutateVerticies( 1 );
                break;
            case 2:
                DNA[ geneToMutate ].mutateVerticies( 2 );
                break;
            case 3:
                DNA[ geneToMutate ].mutateVerticies( 3 );
                break;
            case 4:
                DNA[ geneToMutate ].mutateVerticies( 4 );
                break;
            case 5:
                DNA[ geneToMutate ].mutateVerticies( 5 );
                break;
            case 6:
                DNA[ geneToMutate ].mutateGeneColor( 0 );
                break;
            case 7:
                DNA[ geneToMutate ].mutateGeneColor( 1 );
                break;
            case 8:
                DNA[ geneToMutate ].mutateGeneColor( 2 );
                break;
            case 9:
                DNA[ geneToMutate ].mutateGeneColor( 3 );
                break;
        }
    }
}
