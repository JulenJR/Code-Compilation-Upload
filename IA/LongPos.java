package com.mygdx.safe.IA;

/**
 * Created by Boris.InspiratGames on 21/02/18.
 */

public class LongPos{

    static public final String TAG=LongPos.class.getSimpleName();
    static public final long MAX_VALUE=Long.MAX_VALUE;
    static public final long MIN_VALUE=Long.MIN_VALUE;
    static public final int MAX_ARRAY_SIZE=64;
    static private final int MAX_ARRAY_SIZE_COMPARE=MAX_ARRAY_SIZE+1;
    public long l;
    public int[] positionArray;

    public int size=0;

    public LongPos(){
        clearAll();
    }

    public void setLong(long ln){
        if(ln==0) clearAll();
        else{
            if (ln <= MAX_VALUE && ln >= MIN_VALUE) {
                l = ln;
                positionArray = PositionConversor(ln);
            }
        }
    }

    public LongPos(long ln){
        setLong(ln);
    }

    public void setAll(){
        l=-1;
        positionArray = PositionConversor(l);

    }

    public void clearAll(){
        l=0;
        size=0;
        positionArray = new int[MAX_ARRAY_SIZE];
    }

    public LongPos(int[] n) {
       if (n.length < MAX_ARRAY_SIZE_COMPARE) {
                positionArray = n;
                l = LongConverter();
            } else {
                size = 0;
                System.out.println(TAG +" ERROR!, MAX ARRAY SIZE="+MAX_ARRAY_SIZE);
       }
    }

    public boolean isCollision(float x,float y){
        int c=(int)((y-1)*8 + x);
        System.out.print(" COLLISION WITH: x=" +x+",y="+y+" : CONVERTED AT: "+c +" ");
        boolean collision=false;
        if(x>0 && x<9 && y>0 && y<9){
            long ll=1;
            ll = ll<<(c-1);
            return ((ll & l) == ll);
        }
        return collision;
    }




    private int[] PositionConversor (long longInteger){

        int[] j=new int[MAX_ARRAY_SIZE];int c=1;int k=0;

        while(longInteger!=0 && k<MAX_ARRAY_SIZE){
            if((longInteger & 1)==1) {j[k++]= c;} c++;
            longInteger=longInteger>>>1;
        }
        size=k;
        return j;
    }

    private long LongConverter(){
        long longinteger=0; int c=positionArray.length-1;int i=0;
        while((c>-1)){
            if(positionArray[c]!=0) {i=positionArray[c];break;}
            c--;
        }
        if(size==0) size=c+1;
        while(i>0){
            longinteger=longinteger<<1;
            if(c>-1 && positionArray[c]==i){ longinteger=longinteger|1; c--; }
            i--;

        }

        return longinteger;
    }


    public String toString(){
             return l+"";
    }

    public void setPos(int pos,boolean b){
        if(b)
            setPos(pos);
        else
            unsetPos(pos);
    }

    public void setPos(int pos, int valueToBoolean){
        setPos(pos,!(valueToBoolean==0));
    }

    public void setPos(int pos){
        long posMask=1;
        if(pos>0&& pos<MAX_ARRAY_SIZE_COMPARE) {
            posMask=posMask<<(pos-1);
            l = l | posMask;
            positionArray = PositionConversor(l);
        }
    }

    public void unsetPos(int pos){
        long posMask=1;
        if(pos<MAX_ARRAY_SIZE_COMPARE) {
            posMask=posMask<<(pos-1);
            posMask= ~posMask;
            l = l & posMask;
            positionArray = PositionConversor(l);
        }
    }

    public void setPos(String[] various){
        for(int i=0;i<various.length;i++){
            setPos(Integer.valueOf(various[i]));
        }
    }

    public void unsetPos(String[] various){
        for(int i=0;i<various.length;i++){
            unsetPos(Integer.valueOf(various[i]));
        }
    }

    public void setPos(int[] various){
        for(int i=0;i<various.length;i++){
            setPos(various[i]);
        }
    }

    public void unsetPos(int[] various){
        for(int i=0;i<various.length;i++){
            unsetPos(various[i]);
        }
    }

    public String toStringArray (){
        String s=""; int i=0;
        if (size > 0) {
                s += "[" + positionArray[0];
                while (i < size-1 ) {
                    i++;
                    s += "," + positionArray[i];
                }
                s += "]";
        }
        return s;
    }

    public String toPixelArray(){
        String s="";int c=1;int i=0;
        while (c < 65 ) {
            if(positionArray[i]==c) {s+="X"; i++;}
            else{s+="."; }
            if((c)%8==0 && c>1)s+="\n";
            c++;


        }

        return s;
    }


}

