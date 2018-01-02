package com.nate.chatham;

///////////////////////////////////////////////////////////////////////////////////////
//                                                                                   //
//  Complete to a certain extent. I believe that functionallity is all here finally  //
//  The only thing left to do is draw labels for the images, increase the number of  //
//  triangles, and output the fitness and generation # into a plot to determine if   //
//  the program has plateaued.                                                       //
//                                                                                   //
//  I would also like to output the generation #, generation time, and fitness to a  //
//  CSV document. Information about this here:                                       //
//  http://stackoverflow.com/questions/30073980/java-writing-strings-to-a-csv-file   //
//                                                                                   //
///////////////////////////////////////////////////////////////////////////////////////

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;


public class Chatham extends ApplicationAdapter {
	// ALL THESE VARIABLES!!!!!!!11

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture img;
	private ShapeRenderer shape1;
	private final Gene[] ParentalDNA = new Gene[ 100 ];		// This should probably be higher
	private double ParentalDNAFitness = 9999999999.999;
	private final Gene[] ChildDNA = new Gene[ 100 ];		// This should probably be higher
	private double ChildDNAFitness;
	private double fitness;
	private Pixmap sourceImage;
	private Pixmap testImage;
	private Color colorSource = new Color();
	private Color colorTest = new Color();
	private double deltaRed;
	private double deltaBlue;
	private double deltaGreen;
	private double pixelFitness;
	private final MutationManager mutationManager = new MutationManager();
	private int generationTime = 0;				// So I want to add functionality to get out of local minima. After say
												// 500 mutations that don't result in the child replacing the parent,
												// the next mutation to occur will be accepted.

	private final int parentalOffset = 571;
	private final int childOffset = 1092;
	private int generation = 0;

	
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho( false, 1663, 930 );
		batch = new SpriteBatch();
		img = new Texture("FitnessImage.jpg");
		shape1 = new ShapeRenderer();

		// Creates a new pixmap with the given width, height, and format
		sourceImage = new Pixmap( 656, 768, Pixmap.Format.RGBA8888 );
		testImage = new Pixmap( 656, 768, Pixmap.Format.RGBA8888 );

		// Initiallizes both parent and child strands. Oddly, child needs to be initiated as well.
		for( int i = 0; i < ParentalDNA.length; i += 1 ) {
			ParentalDNA[ i ] = new Gene();
			ChildDNA[ i ] = new Gene();
		}

		// The gene objects will be initiated with an alpha of 0 in all cases to emulate junk DNA. Because we want
		// something to be visible at first. The first gene will be given a random alpha.
		ParentalDNA[ 0 ].setFirstGene();

		copyDNAStrand( ParentalDNA, ChildDNA );

	}

	// Have to remember that the screen is cleared every frame. So fitness has to be checked after drawing has occured.
	@Override
	public void render () {
		// Wipes the screen at the begining of every frame.
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		camera.update();
		batch.setProjectionMatrix( camera.combined );
		shape1.setProjectionMatrix( camera.combined );

		// Makes a childDNA a copy of ParentalDNA and then mutates childDNA in a random fashion.
		copyDNAStrand( ParentalDNA, ChildDNA );
		mutationManager.mutateStrand( ChildDNA );

		// Draws the source image.
		batch.begin();
		batch.draw( img, 50, 112 );
		batch.end();

		// Cycles through each gene in a DNA strand and draws what it encodes
		for( int i = 0; i < ParentalDNA.length; i += 1 ) {

			// Blending bullshit. Unclear what the benefits of this blending function are, but everyone online seems
			// to use it.
			Gdx.gl.glEnable( GL20.GL_BLEND );
			Gdx.gl.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );

			// Draws the triangles.
			shape1.begin( ShapeRenderer.ShapeType.Filled );
			// Parental triangles
			shape1.setColor( ParentalDNA[ i ].getGeneRColor(), ParentalDNA[ i ].getGeneGColor(), ParentalDNA[ i ].getGeneBColor(), ParentalDNA[ i ].getGeneAColor() );
			shape1.triangle( ParentalDNA[ i ].getVerticies()[0] + parentalOffset, ParentalDNA[ i ].getVerticies()[1],
							 ParentalDNA[ i ].getVerticies()[2] + parentalOffset, ParentalDNA[ i ].getVerticies()[3],
							 ParentalDNA[ i ].getVerticies()[4] + parentalOffset, ParentalDNA[ i ].getVerticies()[5] );
			// Child triangles.
			shape1.setColor( ChildDNA[ i ].getGeneRColor(), ChildDNA[ i ].getGeneGColor(), ChildDNA[ i ].getGeneBColor(), ChildDNA[ i ].getGeneAColor() );
			shape1.triangle( ChildDNA[ i ].getVerticies()[0] + childOffset, ChildDNA[ i ].getVerticies()[1],
							 ChildDNA[ i ].getVerticies()[2] + childOffset, ChildDNA[ i ].getVerticies()[3],
							 ChildDNA[ i ].getVerticies()[4] + childOffset, ChildDNA[ i ].getVerticies()[5] );
			shape1.end();

			Gdx.gl.glDisable( GL20.GL_BLEND );

		}

		// Checks fitness of child
		ChildDNAFitness = checkFitness();

		// Determines if child fitness is better than parent's. If it is, then parent is replaced by child.
		if( ChildDNAFitness < ParentalDNAFitness || generationTime > 300 ) {
			copyDNAStrand( ChildDNA, ParentalDNA );
			ParentalDNAFitness = ChildDNAFitness;
			generation += 1;
			System.out.println( generation + "," + ParentalDNAFitness );
			generationTime = 0;
		} else {
			generationTime += 1;
		}

	}
	
	@Override
	public void dispose () {
		shape1.dispose();
		batch.dispose();
		img.dispose();
	}

	// Checks the fitness of a image produced from a DNA stand.
	private double checkFitness() {
		fitness = 0;
		pixelFitness = 0;

		// Gets a pixmap of both the source image and the test image. ( x, y, w, h ).
		sourceImage = ScreenUtils.getFrameBufferPixmap( 50, 112, 521, 768 );
		testImage = ScreenUtils.getFrameBufferPixmap( 1092, 112, 521, 768 );

		// Iterates through every pixel of both the source image.
		for( int row = 0; row < 521; row += 1 ) {
			for( int column = 0; column < 768; column += 1 ) {

				// Converts the 8 digit RGBA code into a Color object.
				Color.rgba8888ToColor( colorSource, sourceImage.getPixel( row, column ) );
				Color.rgba8888ToColor( colorTest, testImage.getPixel( row, column ) );

				// RGB colors can be thought of as a point in 3D space. This code finds the distance between the point
				// for the source image and the point for the test image.
				deltaRed = colorSource.r - colorTest.r;
				deltaGreen = colorSource.g - colorTest.g;
				deltaBlue = colorSource.b - colorTest.b;
				pixelFitness = deltaRed * deltaRed + deltaBlue * deltaBlue + deltaGreen * deltaGreen;

				// Distance between the source image pixel and the test image pixel is then added to the overall fitness
				// of the image.
				fitness += pixelFitness;

			}
		}

		// This is really the most critical part of the entire program. Without disposing them here, they collect
		// somewhere, not memory apparently, and bind up the entire computer.
		sourceImage.dispose();
		testImage.dispose();

		return fitness;
	}

	// Used to copy Gene[] in such a way that both Gene[] don't point at the same memory location.
	private void copyDNAStrand( Gene[] source, Gene[] destination) {
		for( int i = 0; i < source.length; i += 1 ) {
			destination[ i ].setGeneColor( source[ i ].getGeneColor() );
			destination[ i ].setVerticies( source[ i ].getVerticies() );
		}
	}
}
