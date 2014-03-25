package com.algorithms;

/**
 * This is a concrete class for generating LSB algorithm.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * @see com.algorithms.AlgorithmFactory
 * 
 */
public class LSBFactory extends AlgorithmFactory {

	public Algorithm generateAlgorithm() {
		// TODO Auto-generated method stub
		return new LSBAlgorithm();
	}

}
