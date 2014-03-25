package com.algorithms;

/**
 * This is a concrete class for generating F5 algorithm.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * @see com.algorithms.AlgorithmFactory
 * 
 */
public class F5Factory extends AlgorithmFactory {

	@Override
	public Algorithm generateAlgorithm() {
		// TODO Auto-generated method stub
		return new F5Algorithm();
	}

}
