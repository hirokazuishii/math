package com.expmath;

/**
 * 
 * 実験数学を行う際に実験させる
 * 
 * @author hirokazu.ishii
 *
 */
public interface MathExperimentable {
	public String explain();
	public void execute();
	public void executeGui();
	public void test();
}
