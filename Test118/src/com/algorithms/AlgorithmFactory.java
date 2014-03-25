package com.algorithms;

/**
 * This is an abstract factory class for generating specific steganography
 * algorithms.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */
public abstract class AlgorithmFactory {

	/**
	 * To generate a concrete algorithm class
	 * 
	 * @return the algorithm class
	 */
	public Algorithm generateAlgorithm() {
		return null;
	};
}
