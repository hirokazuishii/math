package com.magicsquare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.expmath.MathExperimentable;
 
public class MagicSquare extends AbstractMathExperiment {

	@Override
	public String explain() {
		return "[TITLE]n x nの魔法陣の確認を行う";
	}
	
	@Override
	public void execute() {
		
		info(this.explain());
		
    List<Square<Integer>> squares = new ArrayList<Square<Integer>>();
    
    final Integer LEN_PIECE = 3;
    
    info("n = " + LEN_PIECE);
    
    //n x n の 魔法陣のリストを作る、全部の数列のパターンを列挙した魔法陣リストを返す
    squares = this.genSquares(LEN_PIECE);
    squares.stream().limit(3).forEach(x->x.display());

    //分析する
    SquareAnalyzer analyzer = new SquareAnalyzer(squares);
    analyzer.diagnotics();
    
	}


  public static void main(String[] args) {
    MathExperimentable me = new MagicSquare();
    me.execute();
    //me.executeGui();
    //me.test();
  }
  
  /*
   * n x n の 魔法陣のリストを作る
   * n x n に生成される魔法陣それぞれの数字の並びはUniqueとなる用に生成する
   * 
   */
  public List<Square<Integer>> genSquares(Integer LEN_PIECE) {
    List<Integer> numset = IntStream.rangeClosed(1, LEN_PIECE*LEN_PIECE).boxed().collect(Collectors.toList());
    //info("numset = " + numset);
    List<List<Integer>> squarebase_numsets = NumTreeGen(numset);
    //info("subsquarebase_numsets = " + squarebase_numsets);
    return new ArrayList<Square<Integer>>(){
      {
        for(List<Integer> squarebase_numset:squarebase_numsets) {
          add(new Square<Integer>(LEN_PIECE, squarebase_numset));
        }
      }
    };
  }
  
  //ユニークな数列を生成する
  private  List<List<Integer>> NumTreeGen(List<Integer> numlist) {

    List<List<Integer>> result = new ArrayList<List<Integer>>();

    
    //停止条件
    if(numlist.size() == 1) {
      return new ArrayList<List<Integer>>(){
        {
          add(
              new ArrayList<Integer>() {
                {
                   add(numlist.get(0));
                }
              }
          );
        }
      };
    }
    else {

      for(Integer num:numlist) {
        
        List<Integer> templist = new ArrayList<Integer>(numlist);

        templist.remove(num);
        
        List<List<Integer>> templist_base = NumTreeGen(templist);
        for(List<Integer> templist_e:templist_base) {
          templist_e.add(num);
          result.add(templist_e);
        }
        
      }
  }
  return result;
}

  
  @Override
  public void executeGui() {
    // TODO 自動生成されたメソッド・スタブ
  }

  @Override
  public void test() {
    List<Integer> numlist = IntStream.range(0, 16).boxed().collect(Collectors.toList());

    Integer lenpiece = 4;
    List<List<Integer>> square = new ArrayList<List<Integer>>();
    
    IntStream.range(0, (numlist.size()/lenpiece)).forEach(
        e->square.add(numlist.subList(e*lenpiece,lenpiece+(e*lenpiece)))
    );
  }

  /**
   * 
   * Square 
   * n x nの魔法陣
   * 各セルに保持する値はIntegerとする
   * 
   * @author hirokazu.ishii
   *
   */
  private class Square <V> {
    
    Integer lenpiece;
    List<V> numlist;
    List<List<V>> square;
    
    //具体的な型が入るため?としておく
    List<?> rowsumlist;
    List<?> colsumlist;
    List<Integer> diagsumlist = new ArrayList<Integer>();
    
    Square(Integer lenpiece, List<V> numlist){
      
      this.numlist = numlist;
      this.lenpiece = lenpiece;
      this.square = new ArrayList<List<V>>();

      
      //numlistをlenpiece毎にわける
      IntStream.range(0, (this.numlist.size()/this.lenpiece)).forEach(
          e->this.square.add(this.numlist.subList(e*this.lenpiece,this.lenpiece+(e*this.lenpiece)))
      );

      
      //横合計を求めておく
      this.rowsumlist = this.square.stream().map(row->
        row.stream().mapToInt(r -> Integer.parseInt(r.toString())).reduce((r1,r2)->r1+r2).getAsInt()).collect(Collectors.toList());

      //縦合計を求めておく
      this.colsumlist = this.transpose().stream().map(col->
        col.stream().mapToInt(c -> Integer.parseInt(c.toString())).reduce((c1,c2)->c1+c2).getAsInt()).collect(Collectors.toList());
      
      //斜め合計を求めておく
      this.diagsumlist.add(
          IntStream.range(0, this.lenpiece-1).boxed().collect(Collectors.toList())
          .stream().map(i->this.square.get(i).get(i)).collect(Collectors.toList())
          .stream().mapToInt(i->Integer.parseInt(i.toString())).reduce((j,k)->j+k).getAsInt()
      );
      this.diagsumlist.add(
          IntStream.range(0, this.lenpiece-1).boxed().collect(Collectors.toList())
          .stream().map(i->this.square.get((this.lenpiece-1)-i).get((this.lenpiece-1)-i)).collect(Collectors.toList())
          .stream().mapToInt(i->Integer.parseInt(i.toString())).reduce((j,k)->j+k).getAsInt()
      );
      
      info(IntStream.range(0, this.lenpiece-1).boxed().collect(Collectors.toList())
          .stream().map(i->this.square.get(i).get(i)).collect(Collectors.toList()));
  }

    //魔法陣allデバッグ表示
    public void display() {

      info("--------------");
      this.square.stream().forEach(row->{
        info(
            row.stream().map(i->String.format("%2s",i.toString())).collect(Collectors.joining(" "))
            +" "
            +String.format("%2s",row.stream().mapToInt(i->Integer.parseInt(i.toString())).reduce((i,j)->i+j).getAsInt())
        );
      });
      
      //col数分合計値を取り出す
      //col -> row 変換して処理するのが綺麗？
      info(
        this
          .transpose().stream()
          .map(col->col.stream().mapToInt(i->Integer.parseInt(i.toString())).reduce((i,j)->i+j).getAsInt())
          .map(x->String.format("%2s",x.toString()))
          .collect(Collectors.joining(" "))
       );
      
      info(this.diagsumlist);

    }
    
    /**
     * 
     * 行列変換をする
     * 
     * @return List<List<V>> tlist
     */
    public List<List<V>> transpose(){
      
      List<List<V>> tlist = new ArrayList<List<V>>();
      for(Integer idx=0;idx<this.lenpiece;idx++) {
        List<V> collist = new ArrayList<V>();
        for(List<V>  rowlist : this.square) {
          collist.add(rowlist.get(idx));
        }
        tlist.add(collist);
      }
      return tlist;
    }
    

    public Integer getLenpiece() {
      return lenpiece;
    }

    public void setLenpiece(Integer lenpiece) {
      this.lenpiece = lenpiece;
    }

    public List<V> getNumlist() {
      return numlist;
    }

    public void setNumlist(List<V> numlist) {
      this.numlist = numlist;
    }

    public List<List<V>> getSquare() {
      return square;
    }

    public void setSquare(List<List<V>> square) {
      this.square = square;
    }


    public void setRowsumlist(List<V> rowsumlist) {
      this.rowsumlist = rowsumlist;
    }


    public void setColsumlist(List<V> colsumlist) {
      this.colsumlist = colsumlist;
    }
    
  }
  
  /***
   * 
   * n x nの魔法陣にある特徴をチェックする
   * @author hirokazu.ishii
   *
   */
  private class SquareAnalyzer {
    
    //魔法陣リスト
    List<Square<Integer>> squares;
    
    //診断名,診断結果に合致した魔法陣リスト
    Map<String, List<Square<Integer>>> result = new HashMap<String,List<Square<Integer>>>();
    
    public SquareAnalyzer(List<Square<Integer>> squares) {
      this.squares = squares;
    }
    
    /**
     * 全部のチェックを行う
     **/
    
    public void diagnotics() {
      
      this.analyzeofAllSameRowAndColSum();
      this.analyzeofAllSameRowAndColAndDiagonalSum();
      info("==============結果==============");
      this.displayCUI();
    }
    
    public void displayCUI() {
      
      this.result.keySet()
      .stream().forEach(e->{
        
        info("diagnotics type:" + e);
        info("result count:" + this.result.get(e).size());
        this.result.get(e).stream().forEach(s->{
          s.display();
        });
      });
    }
    
    /**
     * 
     * Rowの合算ががすべて同じ かつ Colの合算がすべて同じである魔法陣をピックアップする
     * 
     */
    private void analyzeofAllSameRowAndColSum() {
      
      List<Square<Integer>> matchedsquares = this.squares.stream().filter(s -> {
        Integer base = (Integer)s.rowsumlist.get(0);
        return (s.rowsumlist.stream().allMatch(r->(base==r))) && (s.colsumlist.stream().allMatch(c->(base==c)));
      }).collect(Collectors.toList());
      this.result.put("Rowの合算ががすべて同じ かつ Colの合算がすべて同じである魔法陣", matchedsquares);
    }
    
    private void analyzeofAllSameRowAndColAndDiagonalSum() {


      List<Square<Integer>> matchedsquares = this.squares.stream().filter(s -> {
        Integer base = (Integer)s.rowsumlist.get(0);
        return (s.rowsumlist.stream().allMatch(r->(base==r))) && (s.colsumlist.stream().allMatch(c->(base==c)) && (s.diagsumlist.stream().allMatch(d->(base==d))));
      }).collect(Collectors.toList());
      this.result.put("Rowの合算ががすべて同じ かつ Colの合算がすべて同じ かつ Trace（斜め）の合算がすべて同じである魔法陣", matchedsquares);
      

    }
  }

}