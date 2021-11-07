package gradingTools.fileTree.diff.implementedTreeDiffs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import gradingTools.fileTree.nodes.FileNode;

public class ImageFileTreeDifference  extends NameComparingFileTreeDifference{

	
	private double [] weighting;
	private double adjustedFileThreshold;
	
	public ImageFileTreeDifference() {
		setWeighting(0.5,0.25,0.25);
	}
	
	public ImageFileTreeDifference(double fileName, double imageSize, double numberOfImages) {
		setWeighting(fileName,imageSize, numberOfImages);
	}
	
	public void setWeighting(double fileName, double imageSize, double numberOfImages) {
		double [] newWeighting = {
				fileName,
				imageSize,
				numberOfImages
		};
		adjustedFileThreshold=requiredFileSimilarityPercent*(fileName+imageSize);
		weighting=newWeighting;
	}
	
	
	@Override
	protected DataHolder calculateFileSimilarities(FileNode[] subdir0, FileNode[] subdir1) {
		
		if(subdir0.length<subdir1.length) {
			FileNode [] temp = subdir0;
			subdir0=subdir1;
			subdir1=temp;
		}
		
		int size = subdir0.length;
		double [][] input = new double [size][size];
		DataHolder [][] results = new DataHolder[size][size];
		 
		
		for(int i=0;i<size;i++) 
			for(int j=0;j<size;j++) {
				if(j>=subdir1.length) {
					input[i][j]=0;
					results[i][j]=new DataHolder();
					continue;
				}
				double value = getImageComparison(subdir0[i],subdir1[j]);
				results[i][j] = new DataHolder(value,value>=adjustedFileThreshold?1:0);
				input[i][j] = -value;
			}
		
		
		DataHolder retVal=getHighestSimilarity(results, input);
		retVal.addToRoughMatches(weighting[2]*subdir1.length);
		
		return retVal; 
	}

	private Map<FileNode, BufferedImage> images = new HashMap<FileNode,BufferedImage>();
	
	
	private double getImageComparison(FileNode image0, FileNode image1) {
		double namePercent = compareStringPercent(image0.getFile().getName(), image1.getFile().getName()) * weighting[0];
		double imageSizePercent=0;
		
		try {
			BufferedImage bimage0,bimage1;
			
			if(images.containsKey(image0)) 
				bimage0=images.get(image0);
			else {
				bimage0=ImageIO.read(image0.getFile());
				images.put(image0, bimage0);
			}
			
			if(images.containsKey(image1)) 
				bimage1=images.get(image1);
			else {
				bimage1=ImageIO.read(image1.getFile());
				images.put(image1, bimage1);
			}
			
			if(!(bimage0==null || bimage1==null)) {
				imageSizePercent=(weighting[1] * getPercentSimilarity(bimage0.getHeight(), bimage1.getHeight())) / 2.0;
				imageSizePercent+=(weighting[1] * getPercentSimilarity(bimage0.getWidth(), bimage1.getWidth())) / 2.0;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return namePercent+imageSizePercent;
	}
	

	public void endOfComparison() {
		images = new HashMap<FileNode,BufferedImage>();
	}

}
