package com.magicsquare;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
  public static void main(String[] args) {
    Test test = new Test();
    test.main();
  }
  
  public void main() {

    List<Bean<Integer>> list = new ArrayList<Bean<Integer>>();
    
    list.add(new Bean<Integer>(1));
    list.add(new Bean<Integer>(2));
    list.add(new Bean<Integer>(3));
    list.add(new Bean<Integer>(4));
    list.add(new Bean<Integer>(5));
    
    System.out.println(list.stream().mapToInt(i->i.get()).sum());
    System.out.println(list);

    System.out.println(IntStream.range(0,10).boxed().collect(Collectors.toList()));
  }
  
}

interface Operator<T>{
  public T add(T x,T y);
}

class IntOperator<Integer>{
  
}
class Bean <T>{
  private T e;
  public Bean(T e) {
    this.e = e;
  }
  public T get(){
    return this.e;
  }
  public void set(T s) {
   this.e =  s;
  }
  
  public String toString() {
    return e.toString();
  }
}
