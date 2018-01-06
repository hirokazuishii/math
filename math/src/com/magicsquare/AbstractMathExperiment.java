package com.magicsquare;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.expmath.MathExperimentable;

public abstract class AbstractMathExperiment implements MathExperimentable {

  Logger logger = LogManager.getLogger(AbstractMathExperiment.class);
 
  public AbstractMathExperiment() {
    PropertyConfigurator.configure("resources/test.properties");
  }
 
  public void info(String target) {
    logger.info(target);
  }
  public void info(List list) {
    logger.info(list.toString());
  }
  public void info(Integer target) {
    logger.info(target.toString());
  }
}
