public class TernarySearch {   
   static final int UNDEF = -1;

   public static int find ( int [] a, int x) {
      return _find (a ,0,a. length -1,x);
   }

   private static int _find ( int [] a,int l,int r, int x) {
      if (l>r) return UNDEF ;
      int m=(l+r) /2;
      if (x==a[m]) return m;
      else if (x<a[m]) return _find (a,l,m -1,x);
      else return _find (a,m+1,r,x);
   }

   public static int ternaryRec(int a[], int x) {
     // TODO: Ternary search

   }
   public static void main(String[] args) {
      // TODO: test-datas

   }
}