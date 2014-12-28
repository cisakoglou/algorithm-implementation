

/*-------------------------------------------------------------------
  Class for computing the orthogonal convex hull in 2D
------------------------------------------------------------------*/

import java.awt.*;
import java.lang.Math;

class ConvexHull2Dorth{
  
  private cVertexList list,hull;
  private cVertex newpoint;
  private int ndelete = 0;
  private int i = 0;
  private int mode;
  
  private cVertex left,top,right,bottom;
  
  public ConvexHull2Dorth( cVertexList list) 
    {
      this.list = list;
	  hull = new cVertexList();
    }
  
  public void ClearHull()
    {
      
    }
  
  public void RunHull()
    {
      //initialization:
      int i;
      cVertex v = new cVertex();
      v=list.head;
      for (i=0;i<list.n;i++)
	{
	  v.vnum=i;
	  v=v.next;
	}
		findCriticalVertices();

		hull = new cVertexList();
		v = new cVertex(left.v.x,left.v.y);
		hull.InsertBeforeHead(v);
		
		//1st part[left to top]
		mode = 1;
		Sort(list,left.vnum,top.vnum-1);
		for(i=left.vnum+1;i<=top.vnum;i++){
			if ((list.GetElement(i).v.y > v.v.y)
				||(list.GetElement(i).v.x < v.v.x)
				||(list.GetElement(i).v.x > top.v.x)){
				// do nothing
			}
			else if (v.v.x != list.GetElement(i).v.x){
				hull.InsertBeforeHead(new cVertex(list.GetElement(i).v.x,v.v.y));
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
			else {
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
		}
		
		//2nd part[top to right]
		mode = 2;
		Sort(list,top.vnum,right.vnum-1);
		for(i=top.vnum+1;i<=right.vnum;i++){
			if ((list.GetElement(i).v.y < v.v.y)
				||(list.GetElement(i).v.x < v.v.x)
				||(list.GetElement(i).v.y > right.v.y)){
				// do nothing
				
			}
			else if (v.v.y != list.GetElement(i).v.y){
				hull.InsertBeforeHead(new cVertex(v.v.x,list.GetElement(i).v.y));
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
			else {
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
		}
		
		//3rd part[right to bottom]
		mode = 3;
		Sort(list,right.vnum,bottom.vnum-1);
		for(i=right.vnum+1;i<=bottom.vnum;i++){
			if ((list.GetElement(i).v.y < v.v.y)
				||(list.GetElement(i).v.x > v.v.x)
				||(list.GetElement(i).v.x < bottom.v.x)){
				// do nothing
			}
			else if (v.v.x != list.GetElement(i).v.x){
				hull.InsertBeforeHead(new cVertex(list.GetElement(i).v.x,v.v.y));
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
			else {
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
		}
		
		//4th part[bottom to left]
		mode = 4;
		Sort(list,bottom.vnum,list.n-1);
		for(i=bottom.vnum+1;i<=list.n-1;i++){
			if ((list.GetElement(i).v.y > v.v.y)
				||(list.GetElement(i).v.x > v.v.x)
				||(list.GetElement(i).v.y < left.v.y)){
				// do nothing
			}
			else if (v.v.y != list.GetElement(i).v.y){
				hull.InsertBeforeHead(new cVertex(v.v.x,list.GetElement(i).v.y));
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
			else {
				v = new cVertex(list.GetElement(i).v.x,list.GetElement(i).v.y);
				hull.InsertBeforeHead(v);
			}
		}
		
		System.out.println("------hull-------");
		hull.PrintVertices();
		
    }
	
	private void Sort(cVertexList a, int lo0, int hi0){
		if (lo0 >= hi0) {
	return;
      }
      cVertex mid = new cVertex();
      mid = a.GetElement(hi0);
      int lo = lo0;
      int hi = hi0-1;
      while (lo <= hi) 
	{
	  while (lo<=hi && (Compare(a.GetElement(lo), mid)==1)) {
	    lo++;
	  }
	  
	  while (lo<=hi && (Compare(a.GetElement(hi), mid)==-1)) {
	    hi--;
	  }
	  
	  if (lo < hi)
	    {
	      Swap(a.GetElement(lo),a.GetElement(hi));
	    }
	  
	}
      Swap(a.GetElement(lo),a.GetElement(hi0));
      Sort(a, lo0, lo-1);
      Sort(a, lo+1, hi0);
	}
	
	private int Compare(  cVertex tpi, cVertex tpj ){
		switch(mode){
			case 1:
				if ((tpi.v.x < tpj.v.x)||((tpi.v.x == tpj.v.x)&&(tpi.v.y > tpj.v.y))){
					return 1;
				}
				else {
					return -1;
				}
		
			case 2:
				if ((tpi.v.y < tpj.v.y)||((tpi.v.y ==tpj.v.y)&&(tpi.v.x < tpj.v.x))){
					return 1;
				}
				else {
					return -1;
				}
		
			case 3:
				if ((tpi.v.x > tpj.v.x)||((tpi.v.x == tpj.v.x)&&(tpi.v.y < tpj.v.y))){
					return 1;
				}
				else {
					return -1;
				}
		
			case 4:
				if ((tpi.v.y > tpj.v.y)||((tpi.v.y == tpj.v.y)&&(tpi.v.x > tpj.v.x))){
					return 1;
				}
				else {
					return -1;
				}
		
		}
		return 0;
	}
	
	private void Swap (cVertex first, cVertex second )
    {
      cVertex temp=new cVertex();
      
      temp=new cVertex(first.v.x, first.v.y);
      temp.vnum = first.vnum;
      temp.mark = first.mark;
      
      list.ResetVertex(first, second.v.x, second.v.y, second.vnum, second.mark);
      list.ResetVertex(second, temp.v.x, temp.v.y, temp.vnum, temp.mark);
      
    }
	
	private void findCriticalVertices(){
		
		int i;
		cVertex v1 = list.head.next;
		left = top = right = bottom = list.head;
		for (i=1;i<list.n;i++){
			if ((v1.v.x < left.v.x) || ((v1.v.x == left.v.x)&&(v1.v.y > left.v.y))){
				left = v1;
			}
			else if ((v1.v.y < top.v.y) || ((v1.v.y == top.v.y)&&(v1.v.x < top.v.x))){
				top = v1;
			}
			else if ((v1.v.x > right.v.x) || ((v1.v.x == right.v.x)&&(v1.v.y < right.v.y))){
				right = v1;
			}
			else if ((v1.v.y > bottom.v.y) || ((v1.v.y == bottom.v.y)&&(v1.v.x > bottom.v.x))){
				bottom = v1;
			}
			v1 = v1.next;
		}
	
	}
	
	private void printCriticalVertices(){
		System.out.println("left");
		left.PrintVertex(left.vnum+1);
		System.out.println("top");
		top.PrintVertex(top.vnum+1);
		System.out.println("right");
		right.PrintVertex(right.vnum+1);
		System.out.println("bottom");
		bottom.PrintVertex(bottom.vnum+1);
	}
	
	public void DrawHull(Graphics gContext, int w, int h)
    {
      
      if (list.head!=null){
		
		list.DrawPoints(gContext, w, h);
	  }
	
      
      if (hull.n == 0 || hull.head==null)
	System.out.println("No drawing is possible.");
      else {
	cVertex v1 = hull.head;
	
      	if(hull.n>2)  
	  {
		gContext.setColor(Color.red);
	    do {
		
	      gContext.drawLine(v1.v.x, v1.v.y, v1.next.v.x, v1.next.v.y);
	      v1 = v1.next;
	    } while (v1 != hull.head);
	    gContext.setColor(Color.blue);
	  }
	
      }//end else     
    }//end draw
	
  
}//end class 
