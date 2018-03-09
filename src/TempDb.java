import dk_util.*;

public class TempDb{
  public static void main(String[] args){
    H2DB.init();
    H2DB.finish();
  }
}
